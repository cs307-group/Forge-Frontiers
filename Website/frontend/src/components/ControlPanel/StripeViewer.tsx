import {AnimatePresence, motion} from "framer-motion";
import Link from "next/link";
import {useState} from "react";
import Stripe from "stripe";

import {ExportJSON} from "../ExportJSON";
import {Spacer} from "../Spacer";

const columns = ["Amount", "Ranks Purchased", "User", "Date"];

const formatter = new Intl.DateTimeFormat(["en"], {
  dateStyle: "short",
  timeStyle: "medium",
});
const AP: any = AnimatePresence;

type TxData = Stripe.Checkout.Session & {
  metadata: {
    userId: string;
    items: (Stripe.Price & {product: Stripe.Product})[];
  };
};
export function StripeViewer({data}: {data: TxData[]}) {
  // data[0].
  const [selectedItem, setSelectedItem] = useState<TxData | null>(null);
  function closeModal() {
    setSelectedItem(null);
  }
  return (
    <div className="mx-auto max-w-[90%] overflow-auto">
      <Spacer y={30} />
      <table className="mx-auto w-full max-w-[90%] table-auto border-separate rounded-lg border border-gray-200 bg-white shadow-lg">
        <thead>
          <tr className="bg-gray-100 text-xs uppercase leading-normal text-gray-600">
            {columns.map((column) => (
              <th className="px-4 py-3 text-left" key={column}>
                {column}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="text-sm text-gray-600">
          {data.map((item) => (
            <tr
              onClick={(e) =>
                e.nativeEvent
                  .composedPath()
                  .find((x) => x instanceof HTMLAnchorElement) ||
                setSelectedItem(item)
              }
              key={item.id}
              className="cursor-pointer border-b border-gray-200 hover:bg-gray-50"
            >
              <td className="px-4 py-3">${item.amount_total! / 100}</td>

              <td className="px-4 py-3">
                {item.metadata.items
                  ? JSON.stringify(item.metadata.items.length)
                  : "Unknown"}
              </td>
              <td>
                <Link
                  className="underline"
                  href={"/profile/view/" + item.metadata.userId}
                >
                  {item.metadata.userId}
                </Link>
              </td>
              <td className="px-4 py-3">
                {formatter.format(item.created * 1000)}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="item-center mt-4 flex justify-end sm:mr-8">
        <ExportJSON data={data} name="stripe-purchases.json" />
      </div>
      <AP>
        {Boolean(selectedItem) &&
          ((
            <motion.div
              initial={{opacity: 0}}
              animate={{opacity: 1}}
              exit={{opacity: 0, pointerEvents: "none"}}
              className="fixed bottom-0 left-0 right-0 top-0 flex items-center justify-center bg-black bg-opacity-50"
              onClick={closeModal}
            >
              <motion.div
                initial={{scale: 0}}
                animate={{scale: 1}}
                exit={{scale: 0}}
                className="max-w-[500px] rounded-xl bg-white p-5 text-black shadow-lg"
                onClick={(e) => e.stopPropagation()}
              >
                <h2 className="mb-2 text-lg font-bold">
                  Transaction ID: {selectedItem!.id}
                </h2>
                <DetailedPurchaseInfo item={selectedItem!} />
              </motion.div>
            </motion.div>
          ) as any)}
      </AP>
    </div>
  );
}

function DetailedPurchaseInfo({item}: {item: TxData}) {
  return (
    <div className="border-separate rounded-lg border-gray-200 bg-white p-4 shadow-lg">
      <div className="mb-4 w-full table-auto">
        <h2 className="my-2 font-bold">Items Sold</h2>
        <dl className="text-sm text-gray-600">
          {item.metadata.items.map((ix, i) => (
            <KeyVal
              k={<>Item {i + 1}</>}
              v={`${ix.product.name} ($${ix.unit_amount! / 100})`}
            />
          ))}
        </dl>
        <h2 className="my-2 font-bold">Misc</h2>
        <dl className="text-sm text-gray-600">
          <KeyVal k="Status:" v={item.status} />
          <KeyVal
            k="Customer Email:"
            v={
              item.customer_email ||
              item.customer_details?.email ||
              "Not Provided"
            }
          />
        </dl>
      </div>
    </div>
  );
}

function KeyVal({k, v}: {k: any; v: any}) {
  return (
    <div className="flex items-center justify-between">
      <dt className="px-4 py-2 font-bold">{k}</dt>
      <dd className="px-4 py-2">{v}</dd>
    </div>
  );
}
