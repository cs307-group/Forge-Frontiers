import {AppLayout} from "@/components/Layout/AppLayout";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {UserDataSecure, GeneratorStateFetch} from "@/handlers/types";
import {fetchUserData} from "@/handlers/user-data";
import {fetchGeneratorsByIsland} from "@/handlers/gens";
import {useCookieSync} from "@/hooks/use-cookie-sync";

export default function Profile({
  data: userData,
  gens: genData,
  cookie,
}: {
  data: UserDataSecure;
  gens: GeneratorStateFetch;
  cookie?: object;
}) {
  useCookieSync(cookie);
  // console.log(userData);
  // console.log(genData);
  return (
    <AppLayout active="generators" title={`${userData.name}'s Generators`}>
    </AppLayout>
  );
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  const userData = await fetchUserData(c);
  if (isErrorResponse(userData)) {
    return userData.resp;
  }
  return userData.toSSPropsResult;
});

export const getServerSidePropsGens = requireAuthenticatedPageView(async (c) => {
  console.log(c);
  console.log("hello");
  const genData = await fetchGeneratorsByIsland(c);
  // if (isErrorResponse(genData)) {
  //   return genData.resp;
  // }
  return genData.toSSPropsResult;
});
