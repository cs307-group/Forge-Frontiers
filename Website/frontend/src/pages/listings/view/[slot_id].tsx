import Head from "next/head";
import Image from "next/image";
import Link from "next/link";
import {useRouter} from "next/router";
import {useMemo} from "react";

import {Client} from "@/components/Client";
import {AppLayout, CONTROL_PANEL} from "@/components/Layout/AppLayout";
import {Spacer} from "@/components/Spacer";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {getOrdersForSlotId} from "@/handlers/market";
import {BazaarLookup, MarketState, UserDataSecure} from "@/handlers/types";
import {fetchUserData} from "@/handlers/user-data";
import {useRefresh} from "@/hooks/use-refresh";
import {ChevronIcon} from "@/icons/ChevronIcon";
import {_collectors, arrayIter} from "@hydrophobefireman/lazy";

const {ARRAY_COLLECTOR} = _collectors;
const intl = new Intl.DateTimeFormat("en", {
  dateStyle: "medium",
  timeStyle: "medium",
});

export default function ViewBySlotId({
  data,
  user,
}: {
  data: {
    buy: MarketState[];
    sell: MarketState[];
    lookup: BazaarLookup;
  };
  user?: UserDataSecure;
}) {
  useRefresh();
  return (
    <>
      <Head>
        <title>{`Market | Forge Frontiers`}</title>
      </Head>
      <AppLayout
        active="market-viewer"
        title="Market Viewer"
        extraNavItems={user?.is_admin ? CONTROL_PANEL : {}}
      >
        <Spacer y={20} />
        <div className="flex items-center justify-center">
          <div
            key={data.lookup.slot_id}
            className="flex flex-col items-center justify-center rounded-md p-4 dark:bg-[#171717]"
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
  const maxPriceKey = `maxPrice-${mode}`;
  const {query, push} = useRouter();
  const sortMode = sort_mode(query[sortKey]);
  const amountSort = sortMode.amount || "down";
  const priceSort = sortMode.price || "up";
  const listSort = sortMode.listDate || "down";
  const maxPrice = +((query[maxPriceKey] as any) || 0);
  const sortedData = arrayIter(
    data.slice().sort((x, y) => {
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
    })
  ).filter((f) => {
    if (maxPrice == 0) return true;
    return f.price <= maxPrice;
  });
  const memoDefaultMax = useMemo(
    () =>
      arrayIter(data).max_by((a, b) =>
        a.price > b.price ? -1 : b.price > a.price ? 1 : 0
      ),
    [data]
  );

  return (
    <section>
      <h1 className="mx-auto text-center text-3xl font-bold">
        Offers to {mode}
      </h1>
      <Spacer y={20} />
      <div className="flxe flex items-center justify-center gap-2">
        Show listings cheaper than:{" "}
        <form
          id={`form-listings-${mode}`}
          action={`/listings/view/${query.slot_id}?${new URLSearchParams(
            query as any
          )}`}
        >
          <select
            name={maxPriceKey}
            className="text-black"
            defaultValue={maxPrice || "select"}
            onChange={(e) => {
              push({query: {...query, [maxPriceKey]: e.currentTarget.value}});
            }}
          >
            <option value="select" disabled>
              select
            </option>
            {Array.from(new Set(data.map((x) => x.price))).map((x) => (
              <option key={x} value={x}>
                {x}
              </option>
            ))}
          </select>
          <noscript>
            <button>search</button>
          </noscript>
        </form>
      </div>
      {data?.length ? (
        <table className="w-[95% ] mx-auto max-w-[600px] divide-y divide-gray-200">
          <thead>
            <tr className="">
              <th className="cursor-pointer px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                <Link
                  className="flex items-center justify-center hover:underline"
                  href={{
                    pathname: `/listings/view/${query.slot_id}`,
                    query: {
                      ...query,
                      [sortKey]: JSON.stringify({amount: toggle(amountSort)}),
                    },
                  }}
                >
                  Amount{" "}
                  <span className={amountSort === "up" ? "" : "-scale-y-100"}>
                    <ChevronIcon />
                  </span>
                </Link>
              </th>
              <th className="cursor-pointer px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                <Link
                  className="flex items-center justify-center hover:underline"
                  href={{
                    pathname: `/listings/view/${query.slot_id}`,
                    query: {
                      ...query,
                      [sortKey]: JSON.stringify({price: toggle(priceSort)}),
                    },
                  }}
                >
                  Price{" "}
                  <span className={priceSort === "up" ? "" : "-scale-y-100"}>
                    <ChevronIcon />
                  </span>
                </Link>
              </th>
              <th className="hidden cursor-pointer px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500 sm:table-cell">
                <Link
                  className="flex items-center justify-center hover:underline"
                  href={{
                    pathname: `/listings/view/${query.slot_id}`,
                    query: {
                      ...query,
                      [sortKey]: JSON.stringify({listDate: toggle(listSort)}),
                    },
                  }}
                >
                  Listed on{" "}
                  <span className={listSort === "up" ? "" : "-scale-y-100"}>
                    <ChevronIcon />
                  </span>
                </Link>
              </th>
            </tr>
          </thead>
          <tbody>
            {sortedData
              .map((x) => (
                <tr key={x.order_id} className="">
                  <td className="whitespace-nowrap px-6 py-4">{x.amount}</td>
                  <td className="whitespace-nowrap px-6 py-4">{x.price}</td>
                  <td className="hidden whitespace-nowrap px-6 py-4 sm:table-cell">
                    <Client fallback={x.listdate}>
                      {intl.format(new Date(x.listdate))}
                    </Client>
                  </td>
                </tr>
              ))
              .collect(ARRAY_COLLECTOR)}
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
  const [resp, user] = await Promise.all([
    getOrdersForSlotId(c),
    fetchUserData(c),
  ]);

  if (isErrorResponse(user)) {
    resp.addCustomData({user: null});
  } else {
    resp.addCustomData({user: user.resp});
  }

  return resp.toSSPropsResult;
});
