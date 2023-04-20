import {noBrowser} from "@/util/no-browser";

import {Tokens} from "./types";

noBrowser();
export const {API_URL, IS_DEV: _isDev, IS_FLY} = process.env;

export const IS_DEV = _isDev === "1";
export function getApiURL(url: string) {
  return new URL(url, IS_FLY ? "https://ff-api.fly.dev" : API_URL).href;
}

export const routes = {
  register: getApiURL("/users/-/register"),
  login: getApiURL("/users/-/login"),
  userInfo: getApiURL("/users/me"),
  searchUser: (x: string) =>
    getApiURL(`/users/search?q=${encodeURIComponent(x)}`),
  refreshToken: getApiURL("/users/-/token/refresh"),
  linkAccount: getApiURL("/users/-/link"),
  mcStats: (id: string) => getApiURL(`/users/-/stats/${id}`),
  mcShop: (id: string) => getApiURL(`/users/id/mc/${id}/shop`),
  userById: (id: string) => getApiURL(`/users/id/${id}`),
  marketState: (q: string) => getApiURL(`/market?q=${encodeURIComponent(q)}`),
  ordersBySlotId: (id: number) => getApiURL(`/market/${id}`),
  generatorsByIsland: (island_id: string) =>
    getApiURL(`/generators/${island_id}`),
  generatorConfig: getApiURL("/generators/config"),
  collectGenerator: (x: string) => getApiURL(`/generators/update/${x}`),
  updateUser: getApiURL("/users/-/update"),
  adminViewShops: getApiURL("/admin/shops"),
  decodeToken: getApiURL("/users/-/decode-token"),
};

/**
 *
 * @param url
 * @param body
 * @returns
 * this function has an x-debug-from header
 * in the future we could add a security token to the project environment
 * that would prevent the python server to be accessed b/-/token/refreshy any other way than through this tiny proxy
 * this is great as it gives us free security advantage of having a serverless function sanitize requests
 * and make sure nothing awkward is sent to the python server
 * we can also take advantage of the fact that this is an edge function so we don't hurt latency too much
 */
export function jsonRequest(
  url: string,
  {
    method,
    body,
    headers,
    ...rest
  }: Omit<RequestInit, "body"> & {body?: object} = {
    method: "POST",
    body: null as any,
  }
) {
  method = method || "post";
  console.log("[fetch] ", method, url);
  const reqHeaders = new Headers(headers);
  reqHeaders.set("content-type", "application/json");
  reqHeaders.set("x-debug-from", "serverless-functions"); // maybe make it a secret?)
  return fetch(url, {
    credentials: "include",
    body: body ? JSON.stringify(body) : undefined,
    headers: reqHeaders,
    ...rest,
    method,
  });
}

export function getAuthenticationHeaders(h: Tokens) {
  return {
    "x-access-token": h.accessToken,
    Authorization: `Bearer ${h.accessToken}`,
    "x-refresh-token": h.refreshToken,
  };
}
