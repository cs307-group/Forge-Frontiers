import {NextApiRequest, NextApiResponse} from "next";
import Stripe from "stripe";

import {isErrorResponse} from "@/handlers/fetch-util";
import {Tokens} from "@/handlers/types";
import {fetchUserData} from "@/handlers/user-data";

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

  if (method !== "post")
    return res.status(405).send({error: "Invalid request to payment gateway"});
  try {
    const user = await fetchUserData({req} as any);
    if (isErrorResponse(user)) {
      return LOGIN_AGAIN();
    }
    if (!user.resp.is_admin) {
      return res.status(401).send({error: "Not authorized"});
    }
    let {product_id, name, description, price, active} = req.body;
    active = Boolean(active == "on" ? true : active == "off" ? false : active);
    price = +price * 100; // price is in cents for stripe
    if (!product_id) {
      await stripe.products.create({
        name,
        description,
        default_price_data: {
          currency: "usd",
          unit_amount: price,
        },
      });
      return res.redirect("/control-panel/rank-editor");
    }
    const currentProduct = await stripe.products.retrieve(product_id, {
      expand: ["default_price"],
    });
    const params: Record<string, string> = {};
    if (currentProduct.name != name) params.name = name;
    if (currentProduct.active != active) params.active = active;
    if (currentProduct.description != description)
      params.description = description;
    if ((currentProduct.default_price as Stripe.Price).unit_amount != price) {
      const newPrice = await stripe.prices.create({
        currency: "usd",
        unit_amount: price,
        product: currentProduct.id,
        nickname: `API Created Price for ${currentProduct.id}`,
      });
      params.default_price = newPrice.id;
    }
    if (Object.keys(params).length) {
      await stripe.products.update(product_id, params);
      return res.redirect("/control-panel/rank-editor");
    }
    return res.send({user: user.resp, body: req.body});
  } catch (e) {
    console.warn(e);
    return res
      .status(500)
      .send({error: "An error occured while editing the rank data"});
  }
}
