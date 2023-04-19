import Stripe from "stripe";

import {ControlPanelRenderer} from "@/components/ControlPanel";
import {RankEditor} from "@/components/ControlPanel/RankEditor";
import {requireAdminPageView} from "@/handlers/auth";
import {UserDataSecure} from "@/handlers/types";
import {useCookieSync} from "@/hooks/use-cookie-sync";

export default function ControlPanelRankEditor({
  error,
  user,
  cookie,
  products,
}: {
  error?: string;
  user: UserDataSecure;
  cookie: any;
  products: (Stripe.Product & {default_price: Stripe.Price})[];
}) {
  useCookieSync(cookie);
  return (
    <ControlPanelRenderer error={error} active="/rank-editor">
      <RankEditor products={products} />
    </ControlPanelRenderer>
  );
}

export const getServerSideProps = requireAdminPageView(
  async (c, user, cookie) => {
    const stripe = new Stripe(process.env.STRIPE_SECRET_KEY!, {
      apiVersion: "2022-11-15",
    });

    let products = await stripe.products.list({
      expand: ["data.default_price"],
      limit: 100,
    });

    let data = products.data;
    while (products.has_more) {
      console.log("fetching again");
      products = await stripe.products.list({
        expand: ["data.default_price"],
        starting_after: data.at(-1)?.id,
        limit: 100,
      });
      data = data.concat(products.data);
    }

    return {
      props: {
        cookie,
        user,
        products: data.filter((x) => x.default_price != null),
      },
    };
  }
);
