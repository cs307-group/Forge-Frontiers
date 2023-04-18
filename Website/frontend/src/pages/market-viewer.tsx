import Head from "next/head";
import Image from "next/image";
import Link from "next/link";
import {useRouter} from "next/router";
import {FormEvent, useState} from "react";

import {Button} from "@/components/Button";
import {BaseInput} from "@/components/Input/BaseInput";
import {AppLayout, CONTROL_PANEL} from "@/components/Layout/AppLayout";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {getMarketState} from "@/handlers/market";
import {MarketStateFetch, UserDataSecure} from "@/handlers/types";
import {fetchUserData} from "@/handlers/user-data";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import {useRefresh} from "@/hooks/use-refresh";
import {userResponseToCustomData} from "@/util/user-response-to-custom-data";

export default function Market({
  data,
  cookie,
  error,
  user,
}: {
  data: MarketStateFetch;
  cookie?: object;
  error: string;
  user?: UserDataSecure;
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
      </Head>
      <AppLayout
        active="market-viewer"
        title="Market Viewer"
        extraNavItems={user?.is_admin ? CONTROL_PANEL : {}}
      >
        <form onSubmit={handleSubmit}>
          <div className="mx-auto w-[95%] max-w-[400px]">
            <BaseInput
              label="Items"
              name="q"
              value={search}
              onChange={(e) => {
                setSearch(e.currentTarget.value);
              }}
            />
            <div className="flex items-center justify-end">
              <Button className="my-2 p-2">Search</Button>
            </div>
          </div>
        </form>
        <div className="grid grid-cols-2 gap-4 p-8">
          {data?.lookup.map((b) => (
            <div
              key={b.slot_id}
              className="flex h-40 flex-col rounded-md border-2 p-4 dark:bg-[#171717]"
            >
              <div className="mx-auto text-center"> {b.item_name} </div>
              <div className="flex flex-1 items-center justify-between">
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
  const [resp, user] = await Promise.all([getMarketState(c), fetchUserData(c)]);
  resp.addCustomData(userResponseToCustomData(user));

  return resp.toSSPropsResult;
});
