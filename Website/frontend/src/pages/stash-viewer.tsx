import Head from "next/head";

import {AppLayout, CONTROL_PANEL} from "@/components/Layout/AppLayout";
import {Spacer} from "@/components/Spacer";
import {TBody, TD, TH, THead, TR, Table} from "@/components/Table";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {fetchGeneratorConfig, fetchGeneratorsByIsland} from "@/handlers/gens";
import {
  GeneratorConfigStatic,
  GeneratorStateFetch,
  UserData,
  UserDataSecure,
} from "@/handlers/types";
import {fetchUserData} from "@/handlers/user-data";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import {useRefresh} from "@/hooks/use-refresh";

export default function Stash({
  data: user,
  gens,
  cookie,
  config,
  error,
}: {
  data: UserDataSecure;
  gens: GeneratorStateFetch;
  cookie?: object;
  config: GeneratorConfigStatic;
  error: string;
}) {
  useCookieSync(cookie);
  useRefresh(3000);
  const {stashes} = gens;
  return (
    <AppLayout
      active="stash-viewer"
      title="Stash Viewer"
      extraNavItems={user?.is_admin ? CONTROL_PANEL : {}}
    >
      <Head>
        <title>Stash Viewer | Forge Frontiers</title>
      </Head>
      <div>
        <Spacer y={50} />
        <Table>
          <THead>
            <TH>Island id</TH>
            <TH>Location World</TH>
            <TH>Contents Info</TH>
            <TH>Coordinates</TH>
          </THead>
          <TBody>
            {stashes.map((stash) => (
              <TR key={stash.id_}>
                <TD>{stash.island_id}</TD>
                <TD>{stash.location_world}</TD>
                <TD>{stash.contents_json}</TD>
                <TD>
                  {stash.location_x}, {stash.location_y}, {stash.location_z}
                </TD>
              </TR>
            ))}
          </TBody>
        </Table>
      </div>
    </AppLayout>
  );
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  const config = fetchGeneratorConfig();
  const userData = await fetchUserData(c);
  if (isErrorResponse(userData)) {
    return userData.resp;
  }
  const genData = await fetchGeneratorsByIsland(userData.resp.island_id);
  if (isErrorResponse(genData)) {
    return genData.resp;
  }
  const res = userData.addCustomData({
    gens: genData.resp,
    config: await config,
  }).toSSPropsResult;
  return res;
});
