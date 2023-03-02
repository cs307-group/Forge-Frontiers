import {IncomingMessage} from "http";
import type {ParsedUrlQuery} from "querystring";

import type {
  GetServerSideProps,
  GetServerSidePropsContext,
  GetServerSidePropsResult,
  PreviewData,
} from "next";

import {hasToken} from "@/util/const-has-token";

import {getAuthenticationHeaders, jsonRequest, routes} from "./_util";
import {Tokens} from "./types";

export const REQUEST_OK = {} as const;

export interface ServerSidePropsWrapper {
  (fn: GetServerSideProps): GetServerSideProps;
}

export type GSSPWithAuth<
  P extends {[key: string]: any} = {[key: string]: any},
  Q extends ParsedUrlQuery = ParsedUrlQuery,
  D extends PreviewData = PreviewData
> = (
  context: GetServerSidePropsContext<Q, D> & {
    req: IncomingMessage & {
      cookies: {
        tokens: string;
      };
    };
  }
) => Promise<GetServerSidePropsResult<P>>;

export interface RequiredAuthentication {
  (fn: GSSPWithAuth): GetServerSideProps;
}

export const requireAuthenticatedPageView: RequiredAuthentication = (fn) => {
  return async (c) => {
    const {req} = c;
    if (!hasToken(req.cookies)) {
      return {redirect: {destination: "/login", statusCode: 302}};
    }
    // @todo maybe use better type here
    return fn(c as any);
  };
};

export class RefreshSuccess {
  constructor(public tokens: Tokens) {}
}

export class RefreshFail {
  constructor(public response: GetServerSidePropsResult<any>) {}
}

export function isRefreshSuccess(x: unknown): x is RefreshSuccess {
  return x instanceof RefreshSuccess;
}

export async function handleTokenRefresh(
  resp: Response,
  previousTokens: Tokens
) {
  if (resp.headers.get("x-error-hint") === "refresh") {
    const refresh = await jsonRequest(routes.refreshToken, {
      method: "get",
      headers: getAuthenticationHeaders(previousTokens),
    });
    if (!refresh.ok) {
      return new RefreshFail({
        redirect: {destination: "/login?force", statusCode: 302},
      });
    }
    const accessToken = refresh.headers.get("x-access-token");
    const refreshToken = refresh.headers.get("x-refresh-token");
    if (!accessToken || !refreshToken) {
      return new RefreshFail({
        props: {
          error:
            "An unknown error occured while contacting the backend. (NO_TOKEN)",
        },
      });
    }
    return new RefreshSuccess({accessToken, refreshToken});
  } else {
    const propsObj = {
      props: {
        error: (await resp.json()).error,
      },
    };
    return new RefreshFail(
      resp.status === 404 ? {notFound: true, ...propsObj} : propsObj
    );
  }
}
