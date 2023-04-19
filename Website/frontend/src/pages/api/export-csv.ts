import {NextRequest} from "next/server";

import {Tokens} from "@/handlers/types";

export const config = {
  runtime: "edge",
};

export default async function (req: NextRequest) {
  const body = await req.formData();
  const data = body.get("data");
  if (!data) return new Response("Invalid form!", {status: 400});
  return new Response(data, {
    headers: {
      "content-disposition": "attachment;filename=export.json",
      "content-type": "application/json",
    },
  });
}
