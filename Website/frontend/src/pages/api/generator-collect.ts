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

async function collectGenerators(t: Tokens, island_id: string, abs: string) {
  if (!island_id)
    return NextResponse.redirect(
      new URL("/generators/collect?error=invalid+island", abs),
      302
    );
  const getResponse = ($tokens: Tokens) =>
    jsonRequest(routes.collectGenerator(island_id as any), {
      method: "get",
      headers: getAuthenticationHeaders($tokens),
    });
  const req = await getResponse(t);
  if (!req.ok) {
    const h = await handleTokenRefresh(req, t);
    if (!isRefreshSuccess(h))
      return NextResponse.redirect(
        new URL("/generators/collect?error=Could+not+collect+generators", abs),
        302
      );
    if (!(await getResponse(h.tokens)).ok) {
      return NextResponse.redirect(
        new URL("/login?error=refresh-failed@generator-collect", abs),
        302
      );
    }
  }
  return NextResponse.redirect(new URL("/generators", abs), 302);
}

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

  return collectGenerators(
    tokens,
    new URL(req.url).searchParams.get("island_id")!,
    new URL(process.env.BASE_URL!).href
  );
}
