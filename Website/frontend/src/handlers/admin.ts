import {getAuthenticationHeaders, jsonRequest, routes} from "./_util";
import {
  AuthReqContext,
  EdgeFunctionResponse,
  handleAuthRefresh,
} from "./fetch-util";
import {ShopData, Tokens} from "./types";

export async function fetchShopStatus(c: AuthReqContext) {
  const {req} = c;

  const getResponse = ($tokens: Tokens) =>
    jsonRequest(routes.adminViewShops, {
      method: "get",
      headers: getAuthenticationHeaders($tokens),
    });
  const result = await handleAuthRefresh(req, getResponse);
  if (Array.isArray(result)) {
    const [newResp, newTokens] = result;
    const shop: ShopData[] = (await newResp.json()).data;
    const ret = new EdgeFunctionResponse(shop);
    if (newTokens == null) {
      return ret;
    } else {
      return ret.addCustomData({cookie: {tokens: JSON.stringify(newTokens)}});
    }
  }
  return result;
}
