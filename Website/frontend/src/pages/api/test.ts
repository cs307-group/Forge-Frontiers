export const config = {runtime: "edge"};

export default function handler(req: Request) {
  const q = new URL(req.url);
  const url = q.searchParams.get("u");
  const method = q.searchParams.get("m");
  let b = q.searchParams.get("body");
  const body = b ? JSON.parse(b) : undefined;
  return fetch(url!, {body, method: method!});
}
