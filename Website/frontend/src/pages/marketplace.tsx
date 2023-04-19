import Head from "next/head";
import {useRef, useState} from "react";
import Stripe from "stripe";

import {Button} from "@/components/Button";
import {AppLayout, CONTROL_PANEL} from "@/components/Layout/AppLayout";
import {Spacer} from "@/components/Spacer";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {StripeProductResponse, UserDataSecure} from "@/handlers/types";
import {fetchUserData} from "@/handlers/user-data";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import {userResponseToCustomData} from "@/util/user-response-to-custom-data";

if (![].at) {
  Array.prototype.at = function (pos) {
    return this.slice(pos, pos + 1)[0];
  };
}
export default function Marketplace({
  data,
  user,
  cookie,
  purchased_ranks,
}: {
  data: StripeProductResponse;
  user: UserDataSecure;
  cookie: object;
  purchased_ranks: string[];
}) {
  useCookieSync(cookie);
  const ref = useRef<HTMLFormElement>();
  const [count, s] = useState<number>(0);

  return (
    <>
      <Head>
        <title>{`Buy Ranks | Forge Frontiers`}</title>
      </Head>
      <AppLayout
        active="marketplace"
        title="Buy Ranks"
        extraNavItems={user?.is_admin ? CONTROL_PANEL : {}}
      >
        <Spacer y={30} />
        <form ref={ref as any} method="post" action="/api/checkout-stripe">
          <div>
            <div className="text-2xl">Your ranks</div>
            <ul>
              {purchased_ranks.map((x) => (
                <li className="ml-8 list-disc" key={x}>
                  {x}
                </li>
              ))}
            </ul>
          </div>
          <div className="gap-4 sm:grid sm:grid-cols-3">
            {data.map((product) => (
              <div
                key={product.id}
                className="flex max-w-sm items-center overflow-hidden rounded-lg p-2 shadow-lg"
              >
                <div className="px-6 py-4">
                  <div className="mb-2 text-xl font-bold">{product.name}</div>
                  <p className="text-sm">{product.description}</p>
                  <p className="text-bold mt-2 text-base">
                    {purchased_ranks.includes(product.name) ? (
                      <span className="font-mono text-xs">PURCHASED</span>
                    ) : (
                      <> ${product.default_price?.unit_amount! / 100}</>
                    )}
                  </p>
                </div>
                {purchased_ranks.includes(product.name) ? (
                  "✅"
                ) : (
                  <input
                    onChange={() => {
                      s(Array.from(new FormData(ref.current).keys()).length);
                    }}
                    name={product.default_price?.id}
                    type="checkbox"
                    className="h-6 w-6"
                  />
                )}
              </div>
            ))}
          </div>
          <div className="flex items-center justify-end">
            {count > 0 && <Button className="p-2">Checkout</Button>}
          </div>
        </form>
      </AppLayout>
    </>
  );
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  const stripe = new Stripe(process.env.STRIPE_SECRET_KEY!, {
    apiVersion: "2022-11-15",
  });

  let [products, user] = await Promise.all([
    stripe.products.list({
      expand: ["data.default_price"],
      limit: 100,
      active: true,
    }),

    fetchUserData(c),
  ]);
  if (isErrorResponse(user)) {
    return user.resp;
  }
  const {purchased_ranks} = user.resp;
  let data = products.data;
  while (products.has_more) {
    console.log("fetching again");
    products = await stripe.products.list({
      active: true,
      expand: ["data.default_price"],
      starting_after: data.at(-1)?.id,
      limit: 100,
    });
    data = data.concat(products.data);
  }
  return {
    props: {
      ...userResponseToCustomData(user),
      data: data.filter((x) => x.default_price != null),
      purchased_ranks,
    },
  };
});
