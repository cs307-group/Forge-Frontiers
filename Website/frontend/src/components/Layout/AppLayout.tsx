import Link from "next/link";
import {useState} from "react";

import {useMedia} from "@/hooks/use-media";

import {Client} from "../Client";
import {MenuIcon} from "../icons/menu";

const NAV_ITEMS = {
  profile: "My Profile",
  generators: "Generators",
  leaderboard: "Leaderboard",
  search: "Player Lookup",
  "market-viewer": "Market Viewer",
  faq: "FAQ",
  "stash-viewer": "Stash Viewer",
} as const;

export interface LayoutProps {
  children?: any;
  active: keyof typeof NAV_ITEMS | null;
  title?: string;
}
function Menu({active}: LayoutProps) {
  const isWide = useMedia.useMinWidth(640);
  if (isWide) return <DesktopSideBar active={active} />;
  return <MobileHeader active={active} />;
}

export function AppLayout({children, active, title}: LayoutProps) {
  return (
    <div className="forge_background flex h-full w-full flex-col border-[1px] border-transparent text-white sm:block">
      <div className="sm:flex sm:h-fit sm:min-h-full sm:w-full sm:flex-grow sm:flex-row sm:flex-nowrap">
        <Client
          fallback={<div data-ssr-skeleton className="h-12 sm:w-[200px]"></div>}
        >
          <Menu active={active} />
        </Client>
        <main
          role="main"
          className="flex-1 sm:m-4 sm:w-full sm:flex-grow sm:rounded-2xl sm:bg-[#262C2C] sm:px-8 sm:pt-1"
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

function MobileHeader({active}: {active: string | null}) {
  const [open, setOpen] = useState(false);
  return (
    <header className="relative flex h-12">
      <MobileNav open={open} setOpen={setOpen} active={active} />
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
function DesktopSideBar({active}: {active: string | null}) {
  return (
    <div className="m-4 flex min-w-[200px] flex-shrink flex-grow-0 flex-col items-center rounded-2xl bg-[#262C2C] px-4">
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
}: {
  active: string | null;
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
      </nav>
    </div>
  );
}
