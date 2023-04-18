import {NextRequest, NextResponse} from "next/server";
import Stripe from "stripe";

export const config = {runtime: "edge"};

const stripe = new Stripe(process.env.STRIPE_API_KEY!, {
  apiVersion: "2022-11-15",
  httpClient: Stripe.createFetchHttpClient(),
});

export default async function (req: NextRequest) {
  let {method} = req;
  method = method.toLowerCase();

  if (method !== "post")
    return new Response("Invalid request to payment gateway", {status: 400});
  try {
    const fd = await req.formData();
    const session = await stripe.checkout.sessions.create({
      line_items: JSON.parse((fd.get("items") as string) || "[]"),
      mode: "payment",
      success_url: `${req.headers.get("origin")}/payments/success`,
      cancel_url: `${req.headers.get("origin")}/payments/cancel`,
    });
    if (!session.url)
      return new Response("Could not create a checkout request!", {
        status: 501,
      });
    return NextResponse.redirect(session.url, 303);
  } catch (e) {
    console.warn(e);
    return new Response("An error occured while processing your payment", {
      status: 500,
    });
  }
}
