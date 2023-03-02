import {NextRequest, NextResponse} from "next/server";

export const config = {
  runtime: "edge",
};

export default async function (req: NextRequest) {
  const body = await req.json();
  const resp = new NextResponse("");

  resp.cookies.set("tokens", body.tokens, {
    httpOnly: true,
    secure: true,
  });
  return resp;
}
