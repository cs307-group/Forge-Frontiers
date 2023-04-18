import Head from "next/head";

import {AppLayout, CONTROL_PANEL} from "@/components/Layout/AppLayout";
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
import {Button} from "@/components/Button";

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
  const config = data.secure?.config || {};

  const currentSettings = {
    "disable-autosync": config["disable-autosync"] ?? false,
    "dark-mode": config["dark-mode"] || false,
  } satisfies UserDataSecure["secure"]["config"];

  return (
    <>
      <Head>
        <title>{`${data.name}'s Settings | Forge Frontiers`}</title>
      </Head>
      <AppLayout
        active="profile"
        title={`${data.name}'s Settings`}
        extraNavItems={data.is_admin ? CONTROL_PANEL : {}}
      >
        <Spacer y={30} />
        <form
          action="/api/settings-update"
          method="post"
          onSubmit={(e) => {
            // e.preventDefault();
            const fd = new FormData(e.currentTarget);
            console.log(Object.fromEntries(Array.from(fd.entries())));
          }}
        >
          <div className="mx-auto flex max-w-[80%] flex-col gap-4">
            <div className="flex items-center justify-between">
              <div>Enable Dark Mode</div>
              <Switch
                name="dark-mode"
                defaultValue={currentSettings["dark-mode"]}
              />
            </div>
            <div className="flex items-center justify-between">
              <div>Disable autosync (advanced)</div>
              <Switch
                name="disable-autosync"
                defaultValue={currentSettings["disable-autosync"]}
              />
            </div>
          </div>
          <div className="flex items-center justify-center">
            <Button className="mt-6 w-36 p-2">Save</Button>
          </div>
        </form>
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
