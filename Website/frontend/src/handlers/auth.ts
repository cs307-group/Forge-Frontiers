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
import {isErrorResponse} from "./fetch-util";
import {Tokens} from "./types";
import {fetchUserData} from "./user-data";

export interface ServerSidePropsWrapper {
  (fn: GetServerSideProps): GetServerSideProps;
}

const LOGIN_REDIRECT = {
  redirect: {destination: "/login", statusCode: 302},
} satisfies GetServerSidePropsResult<any>;

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
      return LOGIN_REDIRECT;
    }
    // @todo maybe use better type here
    return fn(c as any);
  };
};

export const requireAdminPageView: RequiredAuthentication = (fn) => {
  return async (c) => {
    const {req} = c;
    if (!hasToken(req.cookies)) {
      return LOGIN_REDIRECT;
    }
    // @todo maybe use better type here
    const userData = await fetchUserData(c as any);
    if (isErrorResponse(userData)) {
      return LOGIN_REDIRECT;
    }
    if (!userData.resp.is_admin) {
      return {props: {error: "Access Denied"}};
    }
    return fn(c as any);
  };
};

export class RefreshSuccess {
  constructor(public tokens: Tokens) {}
}

export class RefreshFail {
  constructor(
    public response: GetServerSidePropsResult<any>,
    public serverResponse: Response
  ) {}
}

export function isRefreshSuccess(x: unknown): x is RefreshSuccess {
  return x instanceof RefreshSuccess;
}

export async function handleTokenRefresh(
  resp: Response,
  previousTokens: Tokens
) {
  const clone = resp.clone();
  if (resp.headers.get("x-error-hint") === "refresh") {
    const refresh = await jsonRequest(routes.refreshToken, {
      method: "get",
      headers: getAuthenticationHeaders(previousTokens),
    });
    const clone = refresh.clone();
    if (!refresh.ok) {
      return new RefreshFail(
        {
          redirect: {destination: "/login?force", statusCode: 302},
        },
        clone
      );
    }
    const accessToken = refresh.headers.get("x-access-token");
    const refreshToken = refresh.headers.get("x-refresh-token");
    if (!accessToken || !refreshToken) {
      return new RefreshFail(
        {
          props: {
            error:
              "An unknown error occured while contacting the backend. (NO_TOKEN)",
          },
        },
        clone
      );
    }
    return new RefreshSuccess({accessToken, refreshToken});
  } else {
    const propsObj = {
      props: {
        error: (await resp.json()).error,
      },
    };
    return new RefreshFail(
      resp.status === 404 ? {notFound: true, ...propsObj} : propsObj,
      clone
    );
  }
}
