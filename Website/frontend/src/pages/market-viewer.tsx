import Head from "next/head";
import Image from "next/image";
import Link from "next/link";
import {useRouter} from "next/router";
import {FormEvent, useState} from "react";

import {Button} from "@/components/Button";
import {BaseInput} from "@/components/Input/BaseInput";
import {AppLayout} from "@/components/Layout/AppLayout";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {getMarketState} from "@/handlers/market";
import {MarketStateFetch} from "@/handlers/types";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import {useRefresh} from "@/hooks/use-refresh";

export default function Market({
  data,
  cookie,
  error,
}: {
  data: MarketStateFetch;
  cookie?: object;
  error: string;
}) {
  useCookieSync(cookie);
  const {push, query} = useRouter();
  const [search, setSearch] = useState(() => {
    const qr = query.q;
    if (!qr || Array.isArray(qr)) return "";
    return qr;
  });
  useRefresh();
  function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const params = new URLSearchParams({q: search});
    push(`/market-viewer?${params.toString()}`);
  }
  if (error) {
    return (
      <AppLayout active="market-viewer" title="Market Viewer">
        An error occured
      </AppLayout>
    );
  }
  return (
    <>
      <Head>
        <title>{`${
          search ? `${search} - Search | ` : ""
        }Market | Forge Frontiers`}</title>
        <meta name="description" content="Forge Frontier Web Client" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <AppLayout active="market-viewer" title="Market Viewer">
        <form onSubmit={handleSubmit}>
          <div className="w-[95%] mx-auto max-w-[400px]">
            <BaseInput
              label="Items"
              name="q"
              value={search}
              onChange={(e) => {
                setSearch(e.currentTarget.value);
              }}
            />
            <div className="flex items-center justify-end">
              <Button className="p-2 my-2">Search</Button>
            </div>
          </div>
        </form>
        <div className="p-8 grid grid-cols-2 gap-4">
          {data.lookup.map((b) => (
            <div
              key={b.slot_id}
              className="bg-[#171717] rounded-md p-4 h-40 flex flex-col"
            >
              <div className="mx-auto text-center"> {b.item_name} </div>
              <div className="flex items-center justify-between flex-1">
                {data.cheapest[b.slot_id]?.amount ? (
                  <div>
                    <div>
                      <>
                        Buy x{data.cheapest[b.slot_id].amount}{" "}
                        {data.cheapest[b.slot_id].price}{" "}
                      </>
                    </div>
                    <div>x{data.counts[b.slot_id]} items for sale in shop</div>
                  </div>
                ) : (
                  <div>Not available</div>
                )}
                <Image
                  width={48}
                  height={48}
                  src={`/items/${b.item_material}.PNG`}
                  alt={b.item_name}
                />
              </div>
              <div className="flex items-center justify-between">
                <Link
                  href={`/listings/view/${b.slot_id}`}
                  className="text-[#4A7DFF]"
                >
                  View Listings
                </Link>
                <Link
                  href={`/listings/stats/${b.slot_id}`}
                  className="text-[#4A7DFF]"
                >
                  View Graph
                </Link>
              </div>
            </div>
          ))}
        </div>
      </AppLayout>
    </>
  );
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  if (Array.isArray(c.query.q)) return {props: {error: 1}};
  const resp = await getMarketState(c);
  return resp.toSSPropsResult;
});
