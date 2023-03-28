import {config} from "@/config";

import {getAuthenticationHeaders, jsonRequest, routes} from "./_util";
import {
  AuthReqContext,
  EdgeFunctionResponse,
  ErrorResponse,
  handleAuthRefresh,
} from "./fetch-util";
import {Tokens, UserData, UserDataSecure} from "./types";

export async function searchUserData(c: AuthReqContext) {
  const {
    req,
    query: {q},
  } = c;
  if (!q || Array.isArray(q)) {
    return new ErrorResponse({props: {error: "Invalid search"}});
  }
  const getResponse = ($tokens: Tokens) =>
    jsonRequest(routes.searchUser(q), {
      method: "get",
      headers: getAuthenticationHeaders($tokens),
    });
  const result = await handleAuthRefresh(req, getResponse);
  if (Array.isArray(result)) {
    const [newResp, newTokens] = result;
    const searchResults: UserData[] = (await newResp.json()).data;
    const ret = new EdgeFunctionResponse(searchResults);
    if (newTokens == null) {
      return ret;
    } else {
      return ret.addCustomData({cookie: {tokens: JSON.stringify(newTokens)}});
    }
  }
  return result;
}

export async function fetchUserData(c: AuthReqContext) {
  const {req} = c;

  const getResponse = ($tokens: Tokens) =>
    jsonRequest(routes.userInfo, {
      method: "get",
      headers: getAuthenticationHeaders($tokens),
    });
  // let resp = await getResponse(tokens);
  const result = await handleAuthRefresh(req, getResponse);
  if (Array.isArray(result)) {
    const [newResp, newTokens] = result;
    if (newTokens == null) {
      const userData: UserDataSecure = (await newResp.json()).data.user_data;
      if (config.REQUIRE_ACCOUNT_LINK_TO_PROCEED) {
        if (userData.mc_user == null) {
          return new ErrorResponse({
            redirect: {destination: "/link", statusCode: 302},
          });
        }
      }
      return new EdgeFunctionResponse(userData);
    }
    const userData: UserDataSecure = (await newResp.json()).data.user_data;
    if (config.REQUIRE_ACCOUNT_LINK_TO_PROCEED) {
      if (userData.mc_user == null) {
        return new ErrorResponse({
          redirect: {destination: "/link", statusCode: 302},
        });
      }
    }
    return new EdgeFunctionResponse(userData).addCustomData({
      cookie: {
        tokens: JSON.stringify(newTokens),
      },
    });
  }
  if (result instanceof ErrorResponse) {
    return result;
  }
  throw new Error("unreachable");
}

export async function getPlayerById(id: string) {
  const getResponse = () =>
    jsonRequest(routes.userById(id), {
      method: "get",
    });
  const resp = await getResponse();
  if (!resp.ok) {
    return new ErrorResponse({props: await resp.json()});
  }
  const userData: UserData = (await resp.json()).data.user_data;
  return new EdgeFunctionResponse(userData);
}

export function getPlayerStats(id: string) {
  return jsonRequest(routes.mcStats(id), {
    method: "get",
  });
}
