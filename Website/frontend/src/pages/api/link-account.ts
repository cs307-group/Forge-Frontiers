import {NextRequest, NextResponse} from "next/server";

import {handleTokenRefresh, isRefreshSuccess} from "@/handlers/auth";
import {Tokens} from "@/handlers/types";

import {
  getAuthenticationHeaders,
  jsonRequest,
  routes,
} from "../../handlers/_util";

export const config = {
  runtime: "edge",
};

const LOGIN_AGAIN = new NextResponse(JSON.stringify({error: "Login again.."}), {
  status: 401,
});
export default async function (req: NextRequest) {
  let {method} = req;
  method = method.toLowerCase();
  let tokens: Tokens;
  try {
    tokens = JSON.parse(req.cookies.get("tokens")?.value!);
  } catch (e) {
    console.warn(e);
    return LOGIN_AGAIN;
  }
  if (method === "post") {
    const body: {code: string} = await req.json();
    const getResp = ($tokens: Tokens) =>
      jsonRequest(routes.linkAccount, {
        body: {
          code: body.code,
        },
        headers: getAuthenticationHeaders($tokens),
      });
    const resp = await getResp(tokens);
    if (!resp.ok) {
      const refresh = await handleTokenRefresh(resp, tokens);
      if (isRefreshSuccess(refresh)) {
        const newTokens = refresh.tokens;
        const newResp = await getResp(newTokens);
        if (!newResp.ok) return newResp;
        return newResp;
      } else {
        return refresh.serverResponse.clone();
      }
    }

    const nextRespnse = new NextResponse(await resp.blob(), {
      headers: resp.headers,
    });

    return nextRespnse;
  }
  return new NextResponse("", {status: 405});
}
