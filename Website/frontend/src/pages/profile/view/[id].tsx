import {GetServerSideProps} from "next";

import {isErrorResponse} from "@/handlers/fetch-util";
import {PlayerStats, ShopData, UserData} from "@/handlers/types";
import {
  getPlayerById,
  getPlayerShop,
  getPlayerStats,
} from "@/handlers/user-data";
import {DEFAULT_STATS} from "@/util/default-stats";
import Head from "next/head";
import {AppLayout} from "@/components/Layout/AppLayout";
import {ProfileViewer} from "@/components/Profile/Viewer";
import {useCookieSync} from "@/hooks/use-cookie-sync";

export default function ViewProfile({
  error,
  data,
  stats,
  cookie,
  shop,
}: {
  error: string;
  data: UserData;
  stats: PlayerStats;
  shop: ShopData[];
  cookie: object;
}) {
  useCookieSync(cookie);
  return (
    <>
      <Head>
        <title>{`${error ? "Not Found" : data.name} | Forge Frontiers`}</title>
        <meta name="description" content="Forge Frontier Web Client" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <AppLayout
        active={null}
        title={data ? `${data.name}'s Profile` : "Not found"}
      >
        {error ? (
          <>
            Could not find profile. Encountered the following error:{" "}
            <b>{error}</b>
          </>
        ) : (
          <ProfileViewer
            data={data}
            shop={shop}
            stats={
              Object.fromEntries(
                Object.entries(stats).filter((x) => x[0] !== "player_uuid")
              ) as any
            }
          />
        )}
      </AppLayout>
    </>
  );
}

export const getServerSideProps: GetServerSideProps = (async (c) => {
  if (!c.query.id || Array.isArray(c.query.id)) {
    return {props: {error: "Invalid"}};
  }

  const resp = getPlayerById(c.query.id);
  const [stats, shop] = await Promise.all([
    getPlayerStats(c.query.id),
    resp.then((x) =>
      !isErrorResponse(x)
        ? getPlayerShop(x.resp.mc_user)
            .then((shop) => shop.json())
            .then((x) => x.data)
        : []
    ),
  ]);
  let json: PlayerStats = (await stats.json())?.data;
  if (!stats.ok && stats.status !== 404) {
    return {props: json || null};
  }

  if (stats.status === 404) {
    json = DEFAULT_STATS;
  }
  const userResponse = await resp;
  if (isErrorResponse(userResponse)) {
    return userResponse.resp;
  }

  return userResponse.addCustomData({stats: json, shop}).toSSPropsResult;
}) satisfies GetServerSideProps;
