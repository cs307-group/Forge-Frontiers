import {noBrowser} from "@/util/no-browser";

import {Tokens} from "./types";

noBrowser();
export const {
  IS_DEV: _isDev,
  API_HOST_PROD,
  API_HOST_DEVELOPMENT,
} = process.env;

export const IS_DEV = _isDev === "1";
const API_URL = IS_DEV ? API_HOST_DEVELOPMENT : API_HOST_PROD;

export function getApiURL(url: string) {
  return new URL(url, API_URL).href;
}

export const routes = {
  register: getApiURL("/users/-/register"),
  login: getApiURL("/users/-/login"),
  userInfo: getApiURL("/users/me"),
  refreshToken: getApiURL("/users/-/token/refresh"),
  linkAccount: getApiURL("/users/-/link"),
  mcStats: getApiURL("/users/-/stats"),
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
  const reqHeaders = new Headers(headers);
  reqHeaders.set("content-type", "application/json");
  reqHeaders.set("x-debug-from", "serverless-functions"); // maybe make it a secret?)
  return fetch(url, {
    credentials: "include",
    method: method || "post",
    body: body ? JSON.stringify(body) : undefined,
    headers: reqHeaders,
    ...rest,
  });
}

export function getAuthenticationHeaders(h: Tokens) {
  return {
    "x-access-token": h.accessToken,
    Authorization: `Bearer ${h.accessToken}`,
    "x-refresh-token": h.refreshToken,
  };
}
