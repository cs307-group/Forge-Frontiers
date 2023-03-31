import Head from "next/head";
import {useState} from "react";

import {AppLayout} from "@/components/Layout/AppLayout";
import {ProfileViewer} from "@/components/Profile/Viewer";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {PlayerStats, UserDataSecure} from "@/handlers/types";
import {ShopData} from "@/handlers/types";
import {
  fetchUserData,
  getPlayerShop,
  getPlayerStats,
} from "@/handlers/user-data";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import {DEFAULT_STATS} from "@/util/default-stats";

export default function Profile({
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
        <title>{`${data.name} | Forge Frontiers`}</title>
        <meta name="description" content="Forge Frontier Web Client" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <AppLayout active="profile" title={`${data.name}'s Profile`}>
        <ProfileViewer
          data={data}
          shop={shop}
          stats={
            Object.fromEntries(
              Object.entries(stats).filter(([a]) => a !== "player_uuid")
            ) as any
          }
        />
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
