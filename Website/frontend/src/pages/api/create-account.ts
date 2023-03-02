import {NextRequest, NextResponse} from "next/server";

import {jsonRequest, routes} from "../../handlers/_util";

export const config = {
  runtime: "edge",
};

export default async function (req: NextRequest) {
  let {method} = req;
  method = method.toLowerCase();

  if (method === "post") {
    const body: {name: string; password: string; email: string} =
      await req.json();

    return jsonRequest(routes.register, {body});
  }
  return new NextResponse("", {status: 405});
}
