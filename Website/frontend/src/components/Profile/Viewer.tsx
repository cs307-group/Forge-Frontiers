import Image from "next/image";

import {PlayerStats, ShopData, UserData} from "@/handlers/types";
import {useRefresh} from "@/hooks/use-refresh";
import avatarImage from "@/images/avatar.png";

import {MCToPlainText} from "../MCToPlainText";
import {Spacer} from "../Spacer";

const formatter = new Intl.DateTimeFormat(["en"], {
  dateStyle: "medium",
  timeStyle: "short",
});
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
      <div className="flex items-center justify-center sm:block">
        <Image
          className="mt-4 h-80 w-40"
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
        <table className="mx-auto w-full max-w-[600px] divide-y divide-gray-200">
          <thead>
            <tr>
              {Object.keys(stats).map(
                (x) =>
                  x in keyToTableMap && (
                    <th
                      className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500"
                      key={x}
                    >
                      {keyToTableMap[x as keyof PlayerStats]}
                    </th>
                  )
              )}
            </tr>
          </thead>
          <tbody>
            <tr>
              {Object.entries(stats).map(
                (x) =>
                  x[0] in keyToTableMap && (
                    <td
                      className="whitespace-nowrap px-6 py-4"
                      key={`${x[0]}-${x[1]}`}
                    >
                      <span>{x[1] as any}</span>
                    </td>
                  )
              )}
            </tr>
          </tbody>
        </table>
      </div>
      <div className="item-center flex justify-between">
        <h2 className="text-xl">Inventory</h2>
      </div>
      <div className="item-center flex justify-between">
        <div></div>
      </div>
      <Spacer y={100} />
      <h2 className="bold my-8 text-center text-xl">Market Listings</h2>
      <div>
        <table className="mx-auto w-full max-w-[600px] divide-y divide-gray-200">
          <thead>
            <tr>
              {Object.keys(shopDataKeys).map((x) => (
                <th
                  className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500"
                  key={x}
                >
                  {shopDataKeys[x as keyof typeof shopDataKeys]}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {(shop || []).map((x) => (
              <tr key={x.id_}>
                <td className="whitespace-nowrap px-6 py-4">
                  <MCToPlainText text={x.item_name} />
                </td>
                {/* <td className="px-6 py-4 whitespace-nowrap">
                  {x.item_material}
                </td> */}
                <td className="whitespace-nowrap px-6 py-4">
                  <MCToPlainText text={x.item_lore || "unknown"} />
                </td>
                <td className="whitespace-nowrap px-6 py-4">{x.price}</td>
                <td className="whitespace-nowrap px-6 py-4">{x.amount}</td>
                <td className="whitespace-nowrap px-6 py-4">
                  {x.date_sold == -1
                    ? "Not Sold Yet"
                    : formatter.format(x.date_sold)}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
