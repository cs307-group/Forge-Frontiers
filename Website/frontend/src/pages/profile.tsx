import Head from "next/head";
import {useState} from "react";

import {AppLayout} from "@/components/Layout/AppLayout";
import {ProfileViewer} from "@/components/Profile/Viewer";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {PlayerStats, UserDataSecure} from "@/handlers/types";
import {fetchUserData, getPlayerStats} from "@/handlers/user-data";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import {DEFAULT_STATS} from "@/util/default-stats";

export default function Profile({
  data,
  cookie,
  stats: stats_,
}: {
  stats: PlayerStats;
  data: UserDataSecure;
  cookie?: object;
}) {
  useCookieSync(cookie);
  if (!data) return <div>User not found!</div>;
  const [stats, setStats] = useState(
    () =>
      Object.fromEntries(
        Object.entries(stats_).filter((x) => x[0] !== "player_uuid")
      ) as any
  );

  return (
    <>
      <Head>
        <title>{`${data.name} | Forge Frontiers`}</title>
        <meta name="description" content="Forge Frontier Web Client" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <AppLayout active="profile" title={`${data.name}'s Profile`}>
        <ProfileViewer data={data} stats={stats} />
      </AppLayout>
    </>
  );
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  const userData = await fetchUserData(c);
  if (isErrorResponse(userData)) {
    return userData.resp;
  }

  const stats = await getPlayerStats(userData.resp.id_);
  let json: PlayerStats = (await stats.json())?.data;
  console.log(stats.status);
  if (!stats.ok && stats.status !== 404) {
    return {props: json || null};
  }
  if (stats.status === 404) {
    json = DEFAULT_STATS;
  }
  return userData.addCustomData({stats: json || null}).toSSPropsResult;
});
