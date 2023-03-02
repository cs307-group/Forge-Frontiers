import {IncomingMessage} from "http";

import {GetServerSidePropsContext, GetServerSidePropsResult} from "next";

import {config} from "@/config";

import {getAuthenticationHeaders, jsonRequest, routes} from "./_util";
import {handleTokenRefresh, isRefreshSuccess} from "./auth";
import {Tokens, UserDataSecure} from "./types";

export class ErrorResponse {
  constructor(public resp: GetServerSidePropsResult<any>) {}
}
export class UserDataRespoonse {
  public get toSSPropsResult(): GetServerSidePropsResult<any> {
    if (this.cookie) {
      return {
        props: {userData: this.resp, cookie: this.cookie},
      };
    }
    return {props: {userData: this.resp}};
  }
  constructor(public resp: UserDataSecure, public cookie?: any) {}
}

export function isErrorResponse(x: unknown): x is ErrorResponse {
  return x instanceof ErrorResponse;
}

export async function fetchUserData(
  c: GetServerSidePropsContext<any, any> & {
    req: IncomingMessage & {
      cookies: {
        tokens: string;
      };
    };
  }
) {
  const {req} = c;
  const tokens: Tokens = JSON.parse(req.cookies.tokens);

  const getResponse = ($tokens: Tokens) =>
    jsonRequest(routes.userInfo, {
      method: "get",
      headers: getAuthenticationHeaders($tokens),
    });
  let resp = await getResponse(tokens);

  if (!resp.ok) {
    const refresh = await handleTokenRefresh(resp, tokens);
    if (isRefreshSuccess(refresh)) {
      const newTokens = refresh.tokens;
      const newResp = await getResponse(newTokens);

      if (!newResp.ok) {
        return new ErrorResponse({
          props: {
            error: (await resp.json()).error,
          },
        });
      }
      const userData: UserDataSecure = await newResp.json();
      if (config.REQUIRE_ACCOUNT_LINK_TO_PROCEED) {
        if (userData.mc_user == null) {
          return new ErrorResponse({
            redirect: {destination: "/link", statusCode: 302},
          });
        }
      }
      return new UserDataRespoonse(userData, {
        tokens: JSON.stringify(newTokens),
      });
    } else {
      return new ErrorResponse(refresh.response);
    }
  }
  const userData: UserDataSecure = await resp.json();
  if (config.REQUIRE_ACCOUNT_LINK_TO_PROCEED) {
    if (userData.mc_user == null) {
      return new ErrorResponse({
        redirect: {destination: "/link", statusCode: 302},
      });
    }
  }
  return new UserDataRespoonse(userData);
}
