import {NextApiRequest, NextApiResponse} from "next";
import Stripe from "stripe";

import {getAuthenticationHeaders, jsonRequest, routes} from "@/handlers/_util";
import {handleTokenRefresh, isRefreshSuccess} from "@/handlers/auth";
import {Tokens} from "@/handlers/types";

const stripe = new Stripe(process.env.STRIPE_SECRET_KEY!, {
  apiVersion: "2022-11-15",
  httpClient: Stripe.createFetchHttpClient(),
});

export default async function (req: NextApiRequest, res: NextApiResponse) {
  const LOGIN_AGAIN = () => res.status(401).send({error: "Login Again..."});
  let {method} = req;
  method = method!.toLowerCase();
  let tokens: Tokens;
  try {
    tokens = JSON.parse(req.cookies.tokens!);
  } catch (e) {
    console.warn(e);
    return LOGIN_AGAIN();
  }
  const getResponse = ($tokens: Tokens) =>
    jsonRequest(routes.decodeToken, {
      method: "get",
      headers: getAuthenticationHeaders($tokens),
    });

  if (method !== "post")
    return new Response("Invalid request to payment gateway", {status: 400});
  try {
    let resp = await getResponse(tokens);
    if (!resp.ok) {
      const h = await handleTokenRefresh(resp, tokens);
      if (!isRefreshSuccess(h)) {
        return LOGIN_AGAIN();
      }
      resp = await getResponse(h.tokens);
      if (!resp.ok) return LOGIN_AGAIN();
    }
    const json = await resp.json();
    // {console.log(json)}
    const userId = json.data.userId;
    if (!userId) return LOGIN_AGAIN();
    const fd = Object.keys(req.body);
    console.log("[stripe] Purchasing:", fd);
    const session = await stripe.checkout.sessions.create({
      line_items: fd.map((x: string) => ({
        price: x,
        quantity: 1,
      })),
      mode: "payment",
      success_url: `${req.headers.origin}/payments/success`,
      cancel_url: `${req.headers.origin}/payments/cancel`,
      metadata: {VERSION: 1, userId, items: JSON.stringify(fd)},
    });
    if (!session.url)
      return res
        .status(501)
        .send({error: "Could not create a checkout request!"});
    return res.redirect(303, session.url);
  } catch (e) {
    console.warn(e);
    return res
      .status(500)
      .send({error: "An error occured while processing your payment"});
  }
}
