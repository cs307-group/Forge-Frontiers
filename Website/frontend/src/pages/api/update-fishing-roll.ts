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
    return res.status(405).send({error: "Invalid request"});
  try {
    const user = await fetchUserData({req} as any);
    if (isErrorResponse(user)) {
      return LOGIN_AGAIN();
    }
    if (!user.resp.is_admin) {
      return res.status(401).send({error: "Not authorized"});
    }

    const obj = Object.fromEntries(
      Object.entries(req.body).map(([a, b]) => [a, +(b as any)])
    );
    if (Object.values(obj).some((x) => isNaN(x))) {
      return res.send({error: "Invalid request"});
    }
    tokens = user.extractTokens() || tokens;
    const getResponse = () =>
      jsonRequest(routes.updateFishingRollStats, {
        method: "post",
        body: obj,
        headers: getAuthenticationHeaders(tokens),
      });
    const resp = await getResponse();
    if (!resp.ok) {
      return res.send(await resp.json());
    }
    return res.redirect(302, "/control-panel/fishing-roll-config");
  } catch (e) {
    console.warn(e);
    return res
      .status(500)
      .send({error: "An error occured while editing the rank data"});
  }
}
