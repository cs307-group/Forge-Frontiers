import Head from "next/head";

import {AppLayout, CONTROL_PANEL} from "@/components/Layout/AppLayout";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {getMarketState} from "@/handlers/market";
import {UserDataSecure} from "@/handlers/types";
import {fetchUserData} from "@/handlers/user-data";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import {userResponseToCustomData} from "@/util/user-response-to-custom-data";

export default function Marketplace({
  data,
  user,
  cookie,
}: {
  data: any;
  user: UserDataSecure;
  cookie: object;
}) {
  useCookieSync(cookie);
  return (
    <>
      <Head>
        <title>{`Buy Ranks | Forge Frontiers`}</title>
      </Head>
      <AppLayout
        active="marketplace"
        title="Buy Ranks"
        extraNavItems={user?.is_admin ? CONTROL_PANEL : {}}
      >
        
      </AppLayout>
    </>
  );
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  if (Array.isArray(c.query.q)) return {props: {error: 1}};
  const [resp, user] = await Promise.all([getMarketState(c), fetchUserData(c)]);
  resp.addCustomData(userResponseToCustomData(user));

  return resp.toSSPropsResult;
});
