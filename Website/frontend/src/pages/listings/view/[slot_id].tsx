import Head from "next/head";
import Image from "next/image";
import Link from "next/link";
import {useRouter} from "next/router";
import {useState} from "react";

import {Client} from "@/components/Client";
import {AppLayout} from "@/components/Layout/AppLayout";
import {Spacer} from "@/components/Spacer";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {getOrdersForSlotId} from "@/handlers/market";
import {BazaarLookup, MarketState} from "@/handlers/types";

const intl = new Intl.DateTimeFormat("en", {
  dateStyle: "medium",
  timeStyle: "medium",
});

export default function ViewBySlotId({
  data,
}: {
  data: {
    buy: MarketState[];
    sell: MarketState[];
    lookup: BazaarLookup;
  };
}) {
  return (
    <>
      <Head>
        <title>{`Market | Forge Frontiers`}</title>
        <meta name="description" content="Forge Frontier Web Client" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <AppLayout active="market-viewer" title="Market Viewer">
        <Spacer y={20} />
        <div className="flex items-center justify-center">
          <div
            key={data.lookup.slot_id}
            className="bg-[#171717] rounded-md p-4 flex flex-col items-center justify-center"
          >
            <div className="mx-auto text-center"> {data.lookup.item_name} </div>
            <Image
              width={48}
              height={48}
              src={`/items/${data.lookup.item_material}.PNG`}
              alt={data.lookup.item_name}
            />
          </div>
        </div>
        <Spacer y={30} />
        <OfferViewer data={data?.sell} mode="Sell" />
        <Spacer y={60} />
        <OfferViewer data={data?.buy} mode="Buy" />
      </AppLayout>
    </>
  );
}

type SortAscDesc = "up" | "down";
const toggle = (x: SortAscDesc): SortAscDesc => (x == "up" ? "down" : "up");
function sort_mode(
  q: any
): Record<"amount" | "price" | "listDate", SortAscDesc> {
  try {
    return JSON.parse(q);
  } catch (e) {
    return {} as any;
  }
}
function OfferViewer({mode, data}: {mode: string; data: MarketState[]}) {
  const sortKey = `sort-${mode}`;
  const {query} = useRouter();
  const sortMode = sort_mode(query[sortKey]);
  const amountSort = sortMode.amount || "down";
  const priceSort = sortMode.price || "up";
  const listSort = sortMode.listDate || "down";
  const sortedData = data.slice().sort((x, y) => {
    const {amount: amountX, price: priceX, listdate: listDateX} = x;
    const {amount: amountY, price: priceY, listdate: listDateY} = y;

    if (sortMode.amount) {
      if (amountSort === "up") {
        if (amountX < amountY) return -1;
        if (amountX > amountY) return 1;
      } else {
        if (amountX > amountY) return -1;
        if (amountX < amountY) return 1;
      }
    }

    if (sortMode.price) {
      if (priceSort === "up") {
        if (priceX < priceY) return -1;
        if (priceX > priceY) return 1;
      } else {
        if (priceX > priceY) return -1;
        if (priceX < priceY) return 1;
      }
    }
    if (listSort === "up") {
      if (listDateX < listDateY) return -1;
      if (listDateX > listDateY) return 1;
    } else {
      if (listDateX > listDateY) return -1;
      if (listDateX < listDateY) return 1;
    }

    return 0;
  });
  return (
    <section>
      <h1 className="font-bold text-3xl mx-auto text-center">
        Offers to {mode}
      </h1>
      <Spacer y={20} />
      {data?.length ? (
        <table className="mx-auto divide-y divide-gray-200 w-[95% ] max-w-[600px]">
          <thead>
            <tr className="">
              <th className="cursor-pointer px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                <Link
                  href={{
                    pathname: `/listings/view/${query.slot_id}`,
                    query: {
                      ...query,
                      [sortKey]: JSON.stringify({amount: toggle(amountSort)}),
                    },
                  }}
                >
                  Amount
                </Link>
              </th>
              <th className="cursor-pointer px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                <Link
                  href={{
                    pathname: `/listings/view/${query.slot_id}`,
                    query: {
                      ...query,
                      [sortKey]: JSON.stringify({price: toggle(priceSort)}),
                    },
                  }}
                >
                  Price
                </Link>
              </th>
              <th className="cursor-pointer px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider hidden sm:table-cell">
                <Link
                  href={{
                    pathname: `/listings/view/${query.slot_id}`,
                    query: {
                      ...query,
                      [sortKey]: JSON.stringify({listDate: toggle(listSort)}),
                    },
                  }}
                >
                  Listed on
                </Link>
              </th>
            </tr>
          </thead>
          <tbody>
            {sortedData.map((x) => (
              <tr key={x.order_id} className="">
                <td className="px-6 py-4 whitespace-nowrap">{x.amount}</td>
                <td className="px-6 py-4 whitespace-nowrap">{x.price}</td>
                <td className="px-6 py-4 whitespace-nowrap hidden sm:table-cell">
                  <Client fallback={x.listdate}>
                    {intl.format(new Date(x.listdate))}
                  </Client>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <div className="text-center">None so far</div>
      )}
    </section>
  );
}
export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  if (!c.query.slot_id || Array.isArray(c.query.slot_id)) {
    return {props: {error: "Invalid"}};
  }
  const resp = await getOrdersForSlotId(c);

  return resp.toSSPropsResult;
});
