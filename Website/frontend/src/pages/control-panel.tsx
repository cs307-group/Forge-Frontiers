import {motion} from "framer-motion";
import Head from "next/head";
import Link from "next/link";

import {Client} from "@/components/Client";
import {AppLayout, CONTROL_PANEL} from "@/components/Layout/AppLayout";
import {Spacer} from "@/components/Spacer";
import {requireAdminPageView} from "@/handlers/auth";
import {UserData, UserDataSecure} from "@/handlers/types";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import {useLocationHash} from "@/hooks/use-hash";

export default function ControlPanel({
  error,
  user,
  cookie,
}: {
  error?: string;
  user: UserDataSecure;
  cookie: any;
}) {
  useCookieSync(cookie);
  const active = useLocationHash() || "";
  if (error)
    return (
      <>
        <Head>
          <title>Control Panel | Forge Frontiers</title>
        </Head>
        <AppLayout active="control-panel">
          <div>Error: {error}</div>
        </AppLayout>
      </>
    );
  return (
    <>
      <Head>
        <title>Control Panel | Forge Frontiers</title>
      </Head>
      <AppLayout
        active="control-panel"
        extraNavItems={CONTROL_PANEL}
        title="Control Panel"
      >
        <Spacer y={50} />
        <Client fallback={<TabLinks active="" />}>
          <TabLinks active={active} />
        </Client>
      </AppLayout>
    </>
  );
}

export function TabLinks({active}: {active: string}) {
  return (
    <div className="flex items-center gap-8">
      <Link
        href="/control-panel/#/in-game-transactions"
        className={`${
          active === "/in-game-transactions" ? "" : "hover:text-white/60"
        } relative rounded-full px-3 py-1.5 text-sm font-medium text-white outline-sky-400 transition focus-visible:outline-2`}
        style={{
          WebkitTapHighlightColor: "transparent",
        }}
      >
        {active === "/in-game-transactions" && (
          <motion.span
            layoutId="bubble"
            className="absolute inset-0 z-10 bg-white mix-blend-difference"
            style={{borderRadius: 9999}}
            transition={{type: "spring", bounce: 0.2, duration: 0.6}}
          />
        )}
        inGame Transactions
      </Link>

      <Link
        href="/control-panel/#/stripe"
        className={`${
          active === "/stripe" ? "" : "hover:text-white/60"
        } relative rounded-full px-3 py-1.5 text-sm font-medium text-white outline-sky-400 transition focus-visible:outline-2`}
        style={{
          WebkitTapHighlightColor: "transparent",
        }}
      >
        {active === "/stripe" && (
          <motion.span
            layoutId="bubble"
            className="absolute inset-0 z-10 bg-white mix-blend-difference"
            style={{borderRadius: 9999}}
            transition={{type: "spring", bounce: 0.2, duration: 0.6}}
          />
        )}
        Stripe Logs
      </Link>
    </div>
  );
}

export const getServerSideProps = requireAdminPageView(
  async (c, user, cookie) => {
    return {props: {cookie, user}};
  }
);
