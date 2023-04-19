import {motion} from "framer-motion";
import Head from "next/head";
import Link from "next/link";
import {useRouter} from "next/router";

import {UserDataSecure} from "@/handlers/types";
import {useCookieSync} from "@/hooks/use-cookie-sync";

import {Client} from "../Client";
import {AppLayout, CONTROL_PANEL} from "../Layout/AppLayout";
import {Spacer} from "../Spacer";

export function TabLinks({active}: {active: string}) {
  return (
    <div className="flex items-center gap-8">
      <Link
        href="/control-panel/in-game-transactions"
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
        href="/control-panel/stripe"
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
      
      <Link
        href="/control-panel/featureToggle"
        className={`${
          active === "/featureToggle" ? "" : "hover:text-white/60"
        } relative rounded-full px-3 py-1.5 text-sm font-medium text-white outline-sky-400 transition focus-visible:outline-2`}
        style={{
          WebkitTapHighlightColor: "transparent",
        }}
      >
        {active === "/featureToggle" && (
          <motion.span
            layoutId="bubble"
            className="absolute inset-0 z-10 bg-white mix-blend-difference"
            style={{borderRadius: 9999}}
            transition={{type: "spring", bounce: 0.2, duration: 0.6}}
          />
        )}
        Feature Toggle
      </Link>
    </div>
  );
}

export function ControlPanelRenderer({
  error,
  children,
  active,
}: {
  error?: string;
  active: string;
  children?: any;
}) {
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
        <TabLinks active={active} />
        {children}
      </AppLayout>
    </>
  );
}
