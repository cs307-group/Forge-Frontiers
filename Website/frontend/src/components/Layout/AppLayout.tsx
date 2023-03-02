import Link from "next/link";
import {useState} from "react";

import {useMedia} from "@/hooks/use-media";

import {Client} from "../Client";
import {MenuIcon} from "../icons/menu";

const NAV_ITEMS = {
  profile: "My Profile",
  generators: "Generators",
  leaderboard: "Leaderboard",
  "player-lookup": "Player Lookup",
  "market-viewer": "Market Viewer",
} as const;

export interface LayoutProps {
  children?: any;
  active: keyof typeof NAV_ITEMS;
  title?: string;
}
function Menu({active}: LayoutProps) {
  const isWide = useMedia.useMinWidth(640);
  if (isWide) return <DesktopSideBar active={active} />;
  return <MobileHeader active={active} />;
}

export function AppLayout({children, active, title}: LayoutProps) {
  return (
    <div className="forge_background h-full w-full text-white border-[1px] border-transparent flex flex-col sm:block">
      <div className="sm:w-full sm:flex sm:flex-row sm:flex-nowrap sm:h-full sm:flex-grow">
        <Client
          fallback={<div data-ssr-skeleton className="sm:w-[200px] h-12"></div>}
        >
          <Menu active={active} />
        </Client>
        <main
          role="main"
          className="flex-1 sm:w-full sm:flex-grow sm:pt-1 sm:px-3 sm:rounded-2xl sm:bg-[#262C2C] sm:m-4"
        >
          {title && (
            <div className="text-2xl mt-4">
              <span className="border-b-2 border-ff-theme">{title}</span>
            </div>
          )}
          {children}
        </main>
      </div>
    </div>
  );
}

function MobileHeader({active}: {active: string}) {
  const [open, setOpen] = useState(false);
  return (
    <header className="h-12 flex relative">
      <MobileNav open={open} setOpen={setOpen} active={active} />
      <div className="text-ff-theme text-xl flex-1 items-center justify-center flex">
        Forge Frontier
      </div>
      <button
        className="absolute top-0 right-4 bottom-0"
        onClick={() => setOpen(!open)}
      >
        <MenuIcon />
      </button>
    </header>
  );
}
function DesktopSideBar({active}: {active: string}) {
  return (
    <div className="flex-shrink flex-grow-0 px-4 m-4 min-w-[200px] rounded-2xl bg-[#262C2C] flex flex-col items-center">
      <Link href="/" className="text-ff-theme text-xl ff_shadow my-6">
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
      </nav>
      <div className="flex-1 flex items-end mb-4 text-lg">
        <Link href="/settings">Account Settings</Link>
      </div>
    </div>
  );
}

function MobileNav({
  open,
  setOpen,
  active,
}: {
  active: string;
  open: any;
  setOpen: any;
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
        "h-screen w-screen transition flex-col m-auto flex fixed z-[1000] items-center justify-end bg-[#3030307a] " +
        (open ? "" : "pointer-events-none user-select-none opacity-0")
      }
    >
      <nav
        data-clickgrab="1"
        className={
          "flex flex-col items-center gap-6 text-lg py-4 bg-[#262C2C] rounded-t-lg w-[98%] transition " +
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
      </nav>
    </div>
  );
}
