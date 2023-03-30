import Image from "next/image";
import Link from "next/link";

import {AppLayout} from "@/components/Layout/AppLayout";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {getMarketState} from "@/handlers/market";
import {MarketStateFetch} from "@/handlers/types";
import {useCookieSync} from "@/hooks/use-cookie-sync";

export default function Market({
  data,
  cookie,
}: {
  data: MarketStateFetch;
  cookie?: object;
}) {
  useCookieSync(cookie);
  return (
    <AppLayout active="market-viewer" title="Market Viewer">
      <div className="p-8 grid grid-cols-2 gap-4">
        {data.lookup.map((b) => (
          <div
            key={b.slot_id}
            className="bg-[#171717] rounded-md p-4 h-40 flex flex-col"
          >
            <div className="mx-auto text-center"> {b.item_name} </div>
            <div className="flex items-center justify-between flex-1">
              {data.counts[b.slot_id] > 0 ? (
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
  );
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  const resp = await getMarketState();
  return resp.toSSPropsResult;
});
