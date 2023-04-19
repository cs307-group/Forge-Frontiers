import {motion} from "framer-motion";
import Head from "next/head";
import Link from "next/link";

import {AppLayout, CONTROL_PANEL} from "../Layout/AppLayout";
import {Spacer} from "../Spacer";

export function TabLinks({active}: {active: string}) {
  return (
    <div className="flex items-center gap-8">
      <Tab id="/in-game-transactions" active={active}>
        inGame Transactions
      </Tab>
      <Tab id="/stripe" active={active}>
        Stripe Logs
      </Tab>
      <Tab id="/feature" active={active}>
        Feature Toggle
      </Tab>
      <Tab id="/rank-editor" active={active}>
        Rank Editor
      </Tab>
    </div>
  );
}

function Tab({
  id,
  active,
  children,
  href,
}: {
  active: string;
  id: string;
  children?: any;
  href?: string;
}) {
  href = href || `/control-panel${id}`;
  return (
    <Link
      href={href}
      className={`${
        active === id ? "" : "hover:text-white/60"
      } relative rounded-full px-3 py-1.5 text-sm font-medium text-white outline-sky-400 transition focus-visible:outline-2`}
      style={{
        WebkitTapHighlightColor: "transparent",
      }}
    >
      {active === id && (
        <motion.span
          layoutId="bubble"
          className="absolute inset-0 z-10 bg-white mix-blend-difference"
          style={{borderRadius: 9999}}
          transition={{type: "spring", bounce: 0.2, duration: 0.6}}
        />
      )}
      {children}
    </Link>
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
