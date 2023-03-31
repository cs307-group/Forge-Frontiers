import {AppLayout} from "@/components/Layout/AppLayout";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {fetchGeneratorsByIsland} from "@/handlers/gens";
import {GeneratorStateFetch, UserDataSecure} from "@/handlers/types";
import {fetchUserData} from "@/handlers/user-data";
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
    <AppLayout
      active="generators"
      title={`${userData.name}'s Generators`}
    ></AppLayout>
  );
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  console.log("hello");
  const userData = await fetchUserData(c);
  if (isErrorResponse(userData)) {
    return userData.resp;
  }
  const genData = await fetchGeneratorsByIsland(c);
  // if (isErrorResponse(genData)) {
  //   return genData.resp;
  // }
  const res = userData.addCustomData({gens: genData.resp}).toSSPropsResult;
  return res;
});
