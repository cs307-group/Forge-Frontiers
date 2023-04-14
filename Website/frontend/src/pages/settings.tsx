import Head from "next/head";

import {AppLayout} from "@/components/Layout/AppLayout";
import {ProfileViewer} from "@/components/Profile/Viewer";
import {Spacer} from "@/components/Spacer";
import {Switch} from "@/components/Switch";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {PlayerStats, ShopData, UserDataSecure} from "@/handlers/types";
import {
  fetchUserData,
  getPlayerShop,
  getPlayerStats,
} from "@/handlers/user-data";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import {DEFAULT_STATS} from "@/util/default-stats";

export default function Settings({
  data,
  cookie,
  stats,
  shop,
}: {
  stats: PlayerStats;
  data: UserDataSecure;
  shop: ShopData[];
  cookie?: object;
}) {
  useCookieSync(cookie);
  if (!data) return <div>User not found!</div>;

  return (
    <>
      <Head>
        <title>{`${data.name}'s Settings | Forge Frontiers`}</title>
      </Head>
      <AppLayout active="profile" title={`${data.name}'s Settings`}>
        <Spacer y={30} />
        <Switch />
      </AppLayout>
    </>
  );
}
export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  const userData = await fetchUserData(c);
  if (isErrorResponse(userData)) {
    return userData.resp;
  }

  const [stats, shop] = await Promise.all([
    getPlayerStats(userData.resp.id_),
    getPlayerShop(userData.resp.mc_user)
      .then((shop) => shop.json())
      .then((x) => x.data),
  ]);
  let json: PlayerStats = (await stats.json())?.data;
  if (!stats.ok && stats.status !== 404) {
    return {props: json || null};
  }
  if (stats.status === 404) {
    json = DEFAULT_STATS;
  }
  return userData.addCustomData({stats: json || null, shop}).toSSPropsResult;
});
