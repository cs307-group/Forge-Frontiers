import Head from "next/head";

import {Button} from "@/components/Button";
import {GenBlock} from "@/components/Gens/GenBlock";
import {AppLayout, CONTROL_PANEL} from "@/components/Layout/AppLayout";
import {Spacer} from "@/components/Spacer";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {fetchGeneratorConfig, fetchGeneratorsByIsland} from "@/handlers/gens";
import {
  GeneratorConfigStatic,
  GeneratorStateFetch,
  UserDataSecure,
} from "@/handlers/types";
import {fetchUserData} from "@/handlers/user-data";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import {useRefresh} from "@/hooks/use-refresh";

export default function Generators({
  data: userData,
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

  if (error)
    return <div className="flex items-center justify-center">{error}</div>;
  const updateActionURL = "/api/generator-collect";

  useRefresh(5000);
  return (
    <>
      <Head>
        <title>{`Generators | Forge Frontiers`}</title>
      </Head>
      <AppLayout
        active="generators"
        title={`${userData.name}'s Generators`}
        extraNavItems={userData?.is_admin ? CONTROL_PANEL : {}}
      >
        <div>
          <Spacer y={60} />
          <div className="grid grid-cols-3 gap-4">
            {gens.generators.map((x) => (
              <GenBlock
                key={x.id_}
                {...x}
                config={config[x.generator_id as "silver-gen"]}
              />
            ))}
          </div>
          <Spacer y={60} />
          <form action={updateActionURL}>
            <input type="hidden" name="island_id" value={userData.island_id} />
            <div className="flex items-center justify-center">
              <Button className="mt-4 w-36 p-2">Collect</Button>
            </div>
          </form>
        </div>
      </AppLayout>
    </>
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
