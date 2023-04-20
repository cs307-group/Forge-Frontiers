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

const tf = (x: any) => (x == "on" ? true : x == "off" ? false : x);
async function settingsUpdate(t: Tokens, _data: any, abs: string) {
  const data = {
    "disable-autosync": tf(_data["disable-autosync"] || false),
    "dark-mode": tf(_data["dark-mode"] || false),
  };
  console.log(data);
  if (!data)
    return NextResponse.redirect(
      new URL("/settings?error=invalid+request", abs),
      302
    );
  const getResponse = ($tokens: Tokens) =>
    jsonRequest(routes.updateUser, {
      method: "post",
      body: data,
      headers: getAuthenticationHeaders($tokens),
    });
  const req = await getResponse(t);
  if (!req.ok) {
    const h = await handleTokenRefresh(req, t);
    if (!isRefreshSuccess(h))
      return NextResponse.redirect(
        new URL("/profile?error=Could+not+refresh+token@settings-update", abs),
        302
      );
    if (!(await getResponse(h.tokens)).ok) {
      return NextResponse.redirect(
        new URL("/profile?error=refresh-failed@settings-update", abs),
        302
      );
    }
  }
  await new Promise((resolve) => setTimeout(resolve, 800));
  return NextResponse.redirect(new URL("/settings", abs), 302);
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

  return settingsUpdate(
    tokens,
    Object.fromEntries((await req.formData()).entries()),
    new URL(process.env.BASE_URL!).href
  );
}
