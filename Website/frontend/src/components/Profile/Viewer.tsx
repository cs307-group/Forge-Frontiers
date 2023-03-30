import Image from "next/image";

import {PlayerStats, UserData} from "@/handlers/types";
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
        <table className="mx-auto divide-y divide-gray-200 w-full max-w-[600px]">
          <thead>
            <tr>
              {Object.keys(stats).map((x) => (
                <th
                  className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  key={x}
                >
                  {keyToTableMap[x as keyof PlayerStats]}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            <tr>
              {Object.entries(stats).map((x) => (
                <td
                  className="px-6 py-4 whitespace-nowrap"
                  key={`${x[0]}-${x[1]}`}
                >
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
