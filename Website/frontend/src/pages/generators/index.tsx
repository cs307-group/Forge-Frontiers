import Head from "next/head";
import {useRouter} from "next/router";
import {FormEvent} from "react";

import {Button} from "@/components/Button";
import {GenBlock} from "@/components/Gens/GenBlock";
import {AppLayout} from "@/components/Layout/AppLayout";
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
  const {push} = useRouter();
  if (error)
    return <div className="flex items-center justify-center">{error}</div>;
  const updateActionURL = `/generators/collect?island_id=${encodeURIComponent(
    userData.island_id
  )}`;
  function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    push(updateActionURL);
  }
  return (
    <>
      <Head>
        <title>{`Generators | Forge Frontiers`}</title>
        <meta name="description" content="Forge Frontier Web Client" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <AppLayout active="generators" title={`${userData.name}'s Generators`}>
        <div>
          <Spacer y={60} />
          <div className="grid grid-cols-3">
            {gens.generators.map((x) => (
              <GenBlock
                key={x.id_}
                {...x}
                config={config[x.generator_id as "silver-gen"]}
              />
            ))}
          </div>
          <Spacer y={60} />
          <form action={updateActionURL} onSubmit={handleSubmit}>
            <div className="flex items-center justify-center">
              <Button className="w-36 mt-4 p-2">Collect</Button>
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
