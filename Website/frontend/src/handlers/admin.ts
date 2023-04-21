import {getAuthenticationHeaders, jsonRequest, routes} from "./_util";
import {
  AuthReqContext,
  EdgeFunctionResponse,
  handleAuthRefresh,
} from "./fetch-util";
import {FishingRoll, ShopData, Tokens} from "./types";

async function authenticatedGetRequest<T extends object>(
  c: AuthReqContext,
  route: string
) {
  const {req} = c;

  const getResponse = ($tokens: Tokens) =>
    jsonRequest(route, {
      method: "get",
      headers: getAuthenticationHeaders($tokens),
    });
  const result = await handleAuthRefresh(req, getResponse);
  if (Array.isArray(result)) {
    const [newResp, newTokens] = result;
    const r: T = (await newResp.json()).data;
    const ret = new EdgeFunctionResponse(r);
    if (newTokens == null) {
      return ret;
    } else {
      return ret.addCustomData({cookie: {tokens: JSON.stringify(newTokens)}});
    }
  }
  return result;
}

export async function fetchShopStatus(c: AuthReqContext) {
  return authenticatedGetRequest<ShopData[]>(c, routes.adminViewShops);
}

export function fetchFishingRolls(c: AuthReqContext) {
  return authenticatedGetRequest<FishingRoll[]>(c, routes.viewFishingRollStats);
}
