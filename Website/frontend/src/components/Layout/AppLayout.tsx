import Link from "next/link";
import {useState} from "react";

import {useMedia} from "@/hooks/use-media";

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
}
export function AppLayout({children, active}: LayoutProps) {
  const isWide = useMedia.useMinWidth(640);
  if (isWide)
    return <WideScreenLayout active={active}>{children}</WideScreenLayout>;
  return <MobileLayout active={active}>{children}</MobileLayout>;
}

export function WideScreenLayout({active, children}: LayoutProps) {
  return (
    <div className="forge_background h-full w-full text-white border-[1px] border-transparent">
      <div className="w-full flex flex-col sm:flex-row flex-wrap sm:flex-nowrap h-full flex-grow">
        <div className="flex-shrink flex-grow-0 px-4 m-4 min-w-[200px] rounded-2xl bg-[#262C2C] flex flex-col items-center">
          <h1 className="text-ff-theme text-xl ff_shadow my-6">
            Forge Frontier
          </h1>
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
        <main
          role="main"
          className="w-full flex-grow pt-1 px-3 rounded-2xl bg-[#262C2C] m-4"
        >
          {children}
        </main>
      </div>
    </div>
  );
}

export function MobileLayout({active, children}: LayoutProps) {
  const [open, setOpen] = useState(false);

  return (
    <div className="forge_background h-full w-full text-white border-[1px] border-transparent">
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
      <header className="h-12 flex relative">
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
    </div>
  );
}
