import {NextRequest, NextResponse} from "next/server";

import {jsonRequest, routes} from "../../handlers/_util";

export const config = {
  runtime: "edge",
};

export default async function (req: NextRequest) {
  let {method} = req;
  method = method.toLowerCase();

  if (method === "post") {
    const body: {password: string; email: string} = await req.json();
    const resp = await jsonRequest(routes.login, {
      body: {
        password: body.password,
        user: body.email,
      },
    });
    if (!resp.ok) return resp;
    const accessToken = resp.headers.get("x-access-token");
    const refreshToken = resp.headers.get("x-refresh-token");

    if (!accessToken || !refreshToken) {
      return new NextResponse(
        JSON.stringify({error: "Invalid response from backend"}),
        {status: 400}
      );
    }
    const nextRespnse = new NextResponse(await resp.blob(), {
      headers: resp.headers,
    });

    nextRespnse.cookies.set(
      "tokens",
      JSON.stringify({accessToken, refreshToken}),
      {
        httpOnly: true,
        secure: true,
      }
    );

    return nextRespnse;
  }
  return new NextResponse("", {status: 405});
}
