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
  data: Stripe.Checkout.Session[];
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
        data: data.filter((x) => x.metadata && "items" in x.metadata),
      },
    };
  }
);
