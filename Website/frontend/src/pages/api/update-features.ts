import {NextApiRequest, NextApiResponse} from "next";

import {getAuthenticationHeaders, jsonRequest, routes} from "@/handlers/_util";
import {isErrorResponse} from "@/handlers/fetch-util";
import {Tokens} from "@/handlers/types";
import {fetchUserData} from "@/handlers/user-data";

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

    const {swsConfig} = req.body;
    const data = JSON.parse(swsConfig);
    tokens = user.extractTokens() || tokens;
    const getResponse = () =>
      jsonRequest(routes.updateFeatures, {
        method: "post",
        body: data,
        headers: getAuthenticationHeaders(tokens),
      });
    const resp = await getResponse();
    if (!resp.ok) {
      return res.send(await resp.json());
    }
    return res.redirect(302, "/control-panel/feature");
  } catch (e) {
    console.warn(e);
    return res
      .status(500)
      .send({error: "An error occured while editing the rank data"});
  }
}
