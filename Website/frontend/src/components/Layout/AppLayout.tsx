import Link from "next/link";
import {useState} from "react";

import {useMedia} from "@/hooks/use-media";

import {Client} from "../Client";
import {MenuIcon} from "../icons/menu";

const NAV_ITEMS = {
  profile: "My Profile",
  generators: "Generators",
  search: "Player Lookup",
  "market-viewer": "Market Viewer",
} as const;

export interface LayoutProps {
  children?: any;
  active: keyof typeof NAV_ITEMS | null;
  title?: string;
  extraNavItems?: Record<string, string>;
}
function Menu({active, extraNavItems}: LayoutProps) {
  const isWide = useMedia.useMinWidth(640);
  if (isWide)
    return (
      <DesktopSideBar active={active} extraNavItems={extraNavItems || {}} />
    );
  return <MobileHeader active={active} extraNavItems={extraNavItems || {}} />;
}

export function AppLayout({
  children,
  active,
  title,
  extraNavItems = {},
}: LayoutProps) {
  return (
    <div className="forge_background flex h-full w-full flex-col border-[1px] border-black text-black dark:border-transparent dark:text-white sm:block ">
      <div className="sm:flex sm:h-fit sm:min-h-full sm:w-full sm:flex-grow sm:flex-row sm:flex-nowrap">
        <Client
          fallback={<div data-ssr-skeleton className="h-12 sm:w-[200px]"></div>}
        >
          <Menu active={active} extraNavItems={extraNavItems} />
        </Client>
        <main
          role="main"
          className="flex-1 border-2 border-black dark:border-0 dark:border-transparent sm:m-4 sm:w-full sm:flex-grow sm:rounded-2xl sm:px-8 sm:pt-1 dark:sm:bg-[#262C2C]"
        >
          {title && (
            <div className="mt-4 text-center text-2xl sm:text-left">
              <span className="border-b-2 border-ff-theme">{title}</span>
            </div>
          )}
          {children}
        </main>
      </div>
    </div>
  );
}

function MobileHeader({
  active,
  extraNavItems,
}: {
  active: string | null;
  extraNavItems: Record<string, string>;
}) {
  const [open, setOpen] = useState(false);
  return (
    <header className="relative flex h-12">
      <MobileNav
        extraNavItems={extraNavItems}
        open={open}
        setOpen={setOpen}
        active={active}
      />
      <div className="flex flex-1 items-center justify-center text-xl text-ff-theme">
        Forge Frontier
      </div>
      <button
        className="absolute bottom-0 right-4 top-0"
        onClick={() => setOpen(!open)}
      >
        <MenuIcon />
      </button>
    </header>
  );
}
function DesktopSideBar({
  active,
  extraNavItems,
}: {
  active: string | null;
  extraNavItems: Record<string, string>;
}) {
  return (
    <div className="m-4 flex min-w-[200px] flex-shrink flex-grow-0 flex-col items-center rounded-2xl border-2 border-black px-4 dark:border-transparent dark:bg-[#262C2C]">
      <Link href="/" className="ff_shadow my-6 text-xl text-ff-theme">
        Forge Frontier
      </Link>
      <nav className="flex flex-col items-center gap-6 text-lg">
        {Object.entries(NAV_ITEMS).map(([k, v]) => (
          <Link
            key={k}
            href={`/${k}`}
            className={active === k ? "border-b-2 border-[#f6cd47]" : ""}
          >
            {v}
          </Link>
        ))}
        {Object.entries(extraNavItems).map(([k, v]) => (
          <Link
            key={k}
            href={`/${k}`}
            className={active === k ? "border-b-2 border-[#f6cd47]" : ""}
          >
            {v}
          </Link>
        ))}
      </nav>
      <div className="mb-4 flex flex-1 items-end text-lg">
        <Link href="/settings">Account Settings</Link>
      </div>
    </div>
  );
}

function MobileNav({
  open,
  setOpen,
  active,
  extraNavItems,
}: {
  active: string | null;
  open: any;
  setOpen: any;
  extraNavItems: Record<string, string>;
}) {
  return (
    <div
      onClick={(e) => {
        if (
          !e.nativeEvent
            .composedPath()
            .some((x) => (x as HTMLElement).dataset?.clickgrab === "1")
        ) {
          setOpen(false);
        }
      }}
      className={
        "fixed z-[1000] m-auto flex h-screen w-screen flex-col items-center justify-end bg-[#3030307a] transition " +
        (open ? "" : "user-select-none pointer-events-none opacity-0")
      }
    >
      <nav
        data-clickgrab="1"
        className={
          "flex w-[98%] flex-col items-center gap-6 rounded-t-lg bg-[#262C2C] py-4 text-lg transition " +
          (open ? "" : "translate-y-64")
        }
      >
        {Object.entries(NAV_ITEMS).map(([k, v]) => (
          <Link
            key={k}
            href={`/${k}`}
            className={active === k ? "border-b-2 border-[#f6cd47]" : ""}
          >
            {v}
          </Link>
        ))}
        {Object.entries(extraNavItems).map(([k, v]) => (
          <Link
            key={k}
            href={`/${k}`}
            className={active === k ? "border-b-2 border-[#f6cd47]" : ""}
          >
            {v}
          </Link>
        ))}
      </nav>
    </div>
  );
}

export const CONTROL_PANEL = {"control-panel": "Control Panel"};
