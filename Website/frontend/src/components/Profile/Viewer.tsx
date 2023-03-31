import Image from "next/image";

import {PlayerStats, ShopData, UserData} from "@/handlers/types";
import {useRefresh} from "@/hooks/use-refresh";
import avatarImage from "@/images/avatar.png";

import {Spacer} from "../Spacer";

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
const shopDataKeys = {
  item_name: "Name",
  // item_material: "Material",
  item_lore: "Lore",
  price: "Price",
  amount: "Amount",
  date_sold: "Date sold",
};
export function ProfileViewer({
  data,
  stats,
  shop,
}: {
  data: UserData;
  stats: PlayerStats;
  shop: ShopData[];
}) {
  useRefresh(5000);
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
      </div>
      <div className="flex item-center justify-between">
        <div></div>
      </div>
      <Spacer y={100} />
      <h2 className="text-xl text-center bold my-8">Market Listings</h2>
      <div>
        <table className="mx-auto divide-y divide-gray-200 w-full max-w-[600px]">
          <thead>
            <tr>
              {Object.keys(shopDataKeys).map((x) => (
                <th
                  className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  key={x}
                >
                  {shopDataKeys[x as keyof typeof shopDataKeys]}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {shop.map((x) => (
              <tr key={x.id_}>
                <td className="px-6 py-4 whitespace-nowrap">{x.item_name}</td>
                {/* <td className="px-6 py-4 whitespace-nowrap">
                  {x.item_material}
                </td> */}
                <td className="px-6 py-4 whitespace-nowrap">
                  {x.item_lore || "unknown"}
                </td>
                <td className="px-6 py-4 whitespace-nowrap">{x.price}</td>
                <td className="px-6 py-4 whitespace-nowrap">{x.amount}</td>
                <td className="px-6 py-4 whitespace-nowrap">
                  {x.date_sold == -1 ? "Not Sold Yet" : ""}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
