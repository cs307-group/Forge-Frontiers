import {AnimatePresence, motion} from "framer-motion";
import Link from "next/link";
import {useRouter} from "next/router";

import {ShopData} from "@/handlers/types";

import {ExportJSON} from "../ExportJSON";
import {MCToPlainText} from "../MCToPlainText";
import {Spacer} from "../Spacer";
import {TBody, TD, TH, THead, TR, Table} from "../Table";

const AP: any = AnimatePresence;
const formatter = new Intl.DateTimeFormat(["en"], {
  dateStyle: "medium",
  timeStyle: "short",
});
export function InGameTransactionsViewer({shop}: {shop: ShopData[]}) {
  const {query, push} = useRouter();
  function closeModal() {
    push("/control-panel");
  }
  const selectedTransaction = shop.find((tx) => tx.id_ === query.id);
  return (
    <>
      <div className="mx-auto w-[90%] overflow-auto">
        <Table>
          <THead>
            <TH>Date</TH>
            <TH>Item Price</TH>
            <TH>Seller ID</TH>
            <TH>Buyer ID</TH>
          </THead>
          <TBody>
            {shop.map((sale) => (
              <TR key={sale.id_}>
                <TD>
                  <Link
                    href={`/control-panel/in-game-transactions/${sale.id_}`}
                  >
                    {sale.date_sold == -1
                      ? "Not Sold Yet"
                      : formatter.format(sale.date_sold)}
                  </Link>
                </TD>
                <TD>
                  <Link
                    href={`/control-panel/in-game-transactions/${sale.id_}`}
                  >
                    <MCToPlainText text={sale.item_name} /> x{sale.amount} -{" "}
                    {sale.price}
                  </Link>
                </TD>
                <TD>
                  <Link
                    href={"/profile/mc?q=" + sale.lister_player_id}
                    className="underline"
                  >
                    {sale.lister_player_id}
                  </Link>
                </TD>
                <TD>
                  {sale.buyer_id ? (
                    <Link
                      href={"/profile/mc?q=" + sale.buyer_id}
                      className="underline"
                    >
                      {sale.buyer_id}
                    </Link>
                  ) : (
                    "-"
                  )}
                </TD>
              </TR>
            ))}
          </TBody>
        </Table>
        <table className="mx-auto w-full max-w-[1000px] table-auto border-separate rounded-lg border border-gray-200 bg-white shadow-lg">
          <thead></thead>
          <tbody className="text-sm text-gray-600"></tbody>
        </table>
      </div>
      <Spacer y={24} />
      {/* progressive enhancement, this will work even without js */}
      <ExportJSON data={shop} name="ingame-transactions.json" />
      <AP>
        {Boolean(query.id) &&
          ((
            <motion.div
              initial={{opacity: 0}}
              animate={{opacity: 1}}
              exit={{opacity: 0, pointerEvents: "none"}}
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
            <td className="px-4 py-2">
              <MCToPlainText text={item_name} />
            </td>
          </tr>
          <tr>
            <td className="px-4 py-2 font-bold">Item Material:</td>
            <td className="px-4 py-2">
              <MCToPlainText text={item_material} />
            </td>
          </tr>
          <tr>
            <td className="px-4 py-2 font-bold">Item Lore:</td>
            <td className="px-4 py-2">
              <MCToPlainText text={item_lore || "-"} />
            </td>
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
