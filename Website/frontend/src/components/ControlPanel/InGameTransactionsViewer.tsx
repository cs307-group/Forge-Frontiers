import {AnimatePresence, motion} from "framer-motion";
import Link from "next/link";
import {useRouter} from "next/router";

import {ShopData} from "@/handlers/types";

import {Button} from "../Button";
import {Spacer} from "../Spacer";

const AP: any = AnimatePresence;

export function InGameTransactionsViewer({shop}: {shop: ShopData[]}) {
  const {query, push} = useRouter();
  function closeModal() {
    push("/control-panel");
  }
  const selectedTransaction = shop.find((tx) => tx.id_ === query.id);
  return (
    <>
      <div className="mx-auto w-[90%] overflow-auto">
        <table className="mx-auto w-full max-w-[1000px] table-auto border-separate rounded-lg border border-gray-200 bg-white shadow-lg">
          <thead>
            <tr className="bg-gray-100 text-xs uppercase leading-normal text-gray-600">
              <th className="px-4 py-3 text-left">Date</th>
              <th className="px-4 py-3 text-left">Item Price</th>
              <th className="px-4 py-3 text-left">Seller ID</th>
              <th className="px-4 py-3 text-left">Buyer ID</th>
            </tr>
          </thead>
          <tbody className="text-sm text-gray-600">
            {shop.map((sale) => (
              <tr
                key={sale.id_}
                className="border-b border-gray-200 hover:bg-gray-50"
              >
                <td className="px-4 py-3">
                  <Link
                    href={`/control-panel/in-game-transactions/${sale.id_}`}
                  >
                    {sale.date_sold == -1 ? "Not Sold Yet" : sale.date_sold}
                  </Link>
                </td>
                <td className="px-4 py-3">
                  <Link
                    href={`/control-panel/in-game-transactions/${sale.id_}`}
                  >
                    {sale.item_name} x{sale.amount} - {sale.price}
                  </Link>
                </td>
                <td className="px-4 py-3">
                  <Link
                    href={"/search?q=" + sale.lister_player_id}
                    className="underline"
                  >
                    {sale.lister_player_id}
                  </Link>
                </td>
                <td className="px-4 py-3">
                  {sale.buyer_id ? (
                    <Link
                      href={"/search?q=" + sale.buyer_id}
                      className="underline"
                    >
                      {sale.buyer_id}
                    </Link>
                  ) : (
                    "-"
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <Spacer y={24} />
      {/* progressive enhancement, this will work even without js */}
      <form
        action="/api/export-csv"
        method="post"
        onSubmit={(e) => {
          // do this on the frontend
          e.preventDefault();
          const blob = new Blob([JSON.stringify(shop)], {
            type: "application/json",
          });
          const u = URL.createObjectURL(blob);
          const a = document.createElement("a");
          a.href = u;
          a.download = "export.json";
          a.click();
          setTimeout(() => {
            URL.revokeObjectURL(u);
          }, 3000);
        }}
      >
        <noscript>
          <input type="hidden" name="data" value={JSON.stringify(shop)} />
        </noscript>
        <Button className="p-2">Export</Button>
      </form>
      <AP>
        {Boolean(query.id) &&
          ((
            <motion.div
              initial={{opacity: 0}}
              animate={{opacity: 1}}
              exit={{opacity: 0}}
              className="fixed bottom-0 left-0 right-0 top-0 flex items-center justify-center bg-black bg-opacity-50"
              onClick={closeModal}
            >
              {selectedTransaction ? (
                <motion.div
                  initial={{scale: 0}}
                  animate={{scale: 1}}
                  exit={{scale: 0}}
                  className="rounded-xl bg-white p-5 text-black shadow-lg"
                  onClick={(e) => e.stopPropagation()}
                >
                  <h2 className="mb-2 text-lg font-bold">
                    Transaction ID: {selectedTransaction.id_}
                  </h2>
                  <DetailedSaleInfo shop={selectedTransaction} />
                </motion.div>
              ) : (
                <div>Transaction not found</div>
              )}
            </motion.div>
          ) as any)}
      </AP>
    </>
  );
}

function DetailedSaleInfo({shop}: {shop: ShopData}) {
  const {
    id_,
    item_name,
    item_material,
    item_lore,
    price,
    amount,
    lister_player_id,
    buyer_id,
    date_sold,
    custom_data,
  } = shop;

  return (
    <div className="border-separate rounded-lg border-gray-200 bg-white p-4 shadow-lg">
      <table className="mb-4 w-full table-auto">
        <tbody className="text-sm text-gray-600">
          <tr>
            <td className="px-4 py-2 font-bold">Item Name:</td>
            <td className="px-4 py-2">{item_name}</td>
          </tr>
          <tr>
            <td className="px-4 py-2 font-bold">Item Material:</td>
            <td className="px-4 py-2">{item_material}</td>
          </tr>
          <tr>
            <td className="px-4 py-2 font-bold">Item Lore:</td>
            <td className="px-4 py-2">{item_lore || "-"}</td>
          </tr>
          <tr>
            <td className="px-4 py-2 font-bold">Price:</td>
            <td className="px-4 py-2">{price}</td>
          </tr>
          <tr>
            <td className="px-4 py-2 font-bold">Amount:</td>
            <td className="px-4 py-2">{amount}</td>
          </tr>
          <tr>
            <td className="px-4 py-2 font-bold">Seller ID:</td>
            <td className="px-4 py-2">{lister_player_id}</td>
          </tr>
          <tr>
            <td className="px-4 py-2 font-bold">Buyer ID:</td>
            <td className="px-4 py-2">{buyer_id || "-"}</td>
          </tr>
          <tr>
            <td className="px-4 py-2 font-bold">Date Sold:</td>
            <td className="px-4 py-2">{date_sold}</td>
          </tr>
          <tr>
            <td className="px-4 py-2 font-bold">Custom Data:</td>
            <td className="px-4 py-2">{custom_data || "-"}</td>
          </tr>
        </tbody>
      </table>
      <Link href="/control-panel">Close</Link>
    </div>
  );
}

export default DetailedSaleInfo;
