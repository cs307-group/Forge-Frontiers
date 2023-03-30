import {GetServerSideProps} from "next";

import {isErrorResponse} from "@/handlers/fetch-util";
import {PlayerStats, UserData} from "@/handlers/types";
import {getPlayerById, getPlayerStats} from "@/handlers/user-data";
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
}: {
  error: string;
  data: UserData;
  stats: PlayerStats;
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

export const getServerSideProps = (async (c) => {
  if (!c.query.id || Array.isArray(c.query.id)) {
    return {props: {error: "Invalid"}};
  }

  const resp = getPlayerById(c.query.id);
  const _stats = getPlayerStats(c.query.id);
  const stats = await _stats;
  let json: PlayerStats = (await stats.json())?.data;
  if (!stats.ok && stats.status !== 404) {
    return {props: json || null};
  }
  if (stats.status === 404) {
    json = {
      ATK: 0,
      CDMG: 0,
      CRATE: 0,
      current_health: 100,
      DEF: 0,
      DEX: 0,
      HP: 0,
      player_uuid: "unknown",
      STR: 0,
    };
  }
  if (stats.status === 404) {
    json = DEFAULT_STATS;
  }
  const userResponse = await resp;
  if (isErrorResponse(userResponse)) {
    return userResponse.resp;
  }

  return userResponse.addCustomData({stats: json}).toSSPropsResult;
}) satisfies GetServerSideProps;
