import Image from "next/image";

import { PlayerStats, UserData } from "@/handlers/types";
import avatarImage from "@/images/avatar.png";

const keyToTableMap: Record<keyof PlayerStats, string> = {
  ATK: "ATK",
  current_health: "Health",
  CDMG: "CDMG",
  CRATE: "CRATE",
  DEF: "DEF",
  DEX: "DEX",
  HP: "HP",
  player_uuid: "uuid",
  STR: "STR",
};
export function ProfileViewer({
  data,
  stats,
}: {
  data: UserData;
  stats: PlayerStats;
}) {
  return (
    <div>
      <div className="sm:block flex items-center justify-center">
        <Image
          className="h-80 w-40 mt-4"
          width={316}
          priority
          height={512}
          src={
            data.mc_user
              ? `https://visage.surgeplay.com/full/512/${data.mc_user}`
              : avatarImage.src
          }
          alt="Avatar"
        />
      </div>
      <div className="flex-1">
        <table className="border-collapse table-auto w-full max-w-[600px] mx-auto">
          <thead>
            <tr className="border-b border-slate-600 font-medium p-4 pl-8 pt-0 pb-3 text-slate-200 text-left">
              {Object.keys(stats).map((x) => (
                <th key={x}>{keyToTableMap[x as keyof PlayerStats]}</th>
              ))}
            </tr>
          </thead>
          <tbody className="bg-slate-800">
            <tr className="border-b border-slate-700 p-4 pl-8 text-slate-400">
              {Object.entries(stats).map((x) => (
                <td key={`${x[0]}-${x[1]}`}>
                  <span>{x[1] as any}</span>
                </td>
              ))}
            </tr>
          </tbody>
        </table>
      </div>
      <div className="flex item-center justify-between">
        <h2 className="text-xl">Inventory</h2>
        <h2 className="text-xl">Market Listings</h2>
      </div>
    </div>
  );
}
