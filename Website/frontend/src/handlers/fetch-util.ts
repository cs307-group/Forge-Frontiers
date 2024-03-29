import {IncomingMessage} from "http";

import {GetServerSidePropsContext, GetServerSidePropsResult} from "next";

import {noBrowser} from "@/util/no-browser";

import {handleTokenRefresh, isRefreshSuccess} from "./auth";
import {Tokens} from "./types";

noBrowser();
export interface ReqWithCookies extends IncomingMessage {
  cookies: {
    tokens: string;
  };
}
export interface AuthReqContext extends GetServerSidePropsContext<any, any> {
  req: ReqWithCookies;
}

export class EdgeFunctionResponse<T extends Record<any, any>> {
  private customData: Record<any, any> = {};
  public get toSSPropsResult(): GetServerSidePropsResult<any> {
    return {props: {data: this.resp, ...this.customData}};
  }

  constructor(public resp: T) {}
  public addCustomData(obj: Record<any, any>) {
    this.customData = {...this.customData, ...obj};
    return this;
  }

  public extractCookie() {
    return this.customData?.cookie ?? null;
  }
  public extractTokens(): Tokens | null {
    try {
      return JSON.parse(this.extractCookie()?.tokens) as Tokens;
    } catch (_) {
      return null;
    }
  }
}
export class ErrorResponse {
  constructor(public resp: GetServerSidePropsResult<any>) {}
}

export function isErrorResponse(x: unknown): x is ErrorResponse {
  return x instanceof ErrorResponse;
}

export async function handleAuthRefresh(
  req: ReqWithCookies,
  __getResponse: ($tokens: Tokens) => Promise<Response>
): Promise<[Response, Tokens | null] | ErrorResponse> {
  const tokens: Tokens = JSON.parse(req.cookies.tokens);
  let resp = await __getResponse(tokens);
  if (!resp.ok) {
    console.log(await resp.clone().text());
    const refresh = await handleTokenRefresh(resp, tokens);
    console.log("refreshed:", isRefreshSuccess(refresh));
    if (isRefreshSuccess(refresh)) {
      const newTokens = refresh.tokens;
      const newResp = await __getResponse(newTokens);

      if (!newResp.ok) {
        return new ErrorResponse({
          props: {
            error: (await resp.json()).error,
          },
        });
      }
      return [newResp, newTokens];
    } else {
      return new ErrorResponse(refresh.response);
    }
  }
  return [resp, null];
}
