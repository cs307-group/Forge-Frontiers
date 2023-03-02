import {UserDataSecure} from "../types";

export type ResponseType<T> = {error: string} | T;
function parseJsonResponse<T>(json: any): ResponseType<T> {
  if (!json || "error" in json) {
    return json;
  }
  if (!("data" in json)) {
    console.error(
      "Failed to automatically parse response with parseJsonResponse. got: ",
      json
    );
    throw new Error("parseJsonResponseFailed");
  }
  const {data} = json;
  return data as T;
}

export function isError<T extends object>(
  resp: ResponseType<T>
): resp is {error: string} {
  return "error" in resp;
}

export async function createAccount(obj: {
  name: string;
  password: string;
  email: string;
}) {
  const resp = await fetch("/api/create-account", {
    body: JSON.stringify(obj),
    credentials: "include",
    method: "POST",
    headers: {"content-type": "application/json"},
  });
  return parseJsonResponse<UserDataSecure>(await resp.json());
}

export async function login(obj: {password: string; email: string}) {
  const resp = await fetch("/api/login", {
    credentials: "include",
    body: JSON.stringify(obj),
    method: "POST",
    headers: {"content-type": "application/json"},
  });
  return parseJsonResponse<UserDataSecure>(await resp.json());
}

export async function linkAccount(obj: {code: string}) {
  const resp = await fetch("/api/link-account", {
    body: JSON.stringify(obj),
    method: "post",
    credentials: "include",
    headers: {"content-type": "application/json"},
  });
  return parseJsonResponse<UserDataSecure>(await resp.json());
}
