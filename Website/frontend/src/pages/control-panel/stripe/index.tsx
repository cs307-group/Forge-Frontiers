import Stripe from "stripe";

import {ControlPanelRenderer} from "@/components/ControlPanel";
import {StripeViewer} from "@/components/ControlPanel/StripeViewer";
import {requireAdminPageView} from "@/handlers/auth";
import {UserDataSecure} from "@/handlers/types";
import {useCookieSync} from "@/hooks/use-cookie-sync";

export default function ControlPanel({
  error,
  user,
  cookie,
  data,
}: {
  data: (Stripe.Checkout.Session & {
    metadata: {
      userId: string;
      items: (Stripe.Price & {product: Stripe.Product})[];
    };
  })[];
  error?: string;
  user: UserDataSecure;
  cookie: any;
}) {
  useCookieSync(cookie);
  return (
    <ControlPanelRenderer error={error} active="/stripe">
      <StripeViewer data={data} />
    </ControlPanelRenderer>
  );
}

const cache: Record<string, Stripe.Price> = {};

export const getServerSideProps = requireAdminPageView(
  async (c, user, cookie) => {
    const stripe = new Stripe(process.env.STRIPE_SECRET_KEY!, {
      apiVersion: "2022-11-15",
    });
    let products = await stripe.checkout.sessions.list({
      limit: 100,
      expand: ["data.payment_intent"],
    });
    let data = products.data;
    while (products.has_more) {
      console.log("fetching again");
      products = await stripe.checkout.sessions.list({
        starting_after: data.at(-1)?.id,
        limit: 100,
        expand: ["data.payment_intent"],
      });
      data = data.concat(products.data);
    }

    return {
      props: {
        cookie,
        user,
        data: await Promise.all(
          data
            .filter((x) => x.metadata && "items" in x.metadata)
            .map(async (item) => {
              if (!item.metadata) item.metadata = {};
              const items: string[] = JSON.parse(item.metadata!.items);
              item.metadata.items = (await Promise.all(
                items.map(
                  async (i) =>
                    cache[i] ||
                    stripe.prices
                      .retrieve(i, {expand: ["product"]})
                      .then((price) => (cache[i] = price))
                )
              )) as any;
              return item;
            })
        ),
      },
    };
  }
);
