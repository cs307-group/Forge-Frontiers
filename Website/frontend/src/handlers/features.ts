import {getAuthenticationHeaders, jsonRequest, routes} from "./_util";
import {
  EdgeFunctionResponse,
  ErrorResponse,
  ReqWithCookies,
  handleAuthRefresh,
} from "./fetch-util";
import {FeatureList, ShopData, Tokens} from "./types";

export async function fetchFeatures(req: ReqWithCookies) {
  const getResponse = ($tokens: Tokens) =>
    jsonRequest(routes.getFeatures, {
      method: "get",
      headers: getAuthenticationHeaders($tokens),
    });


  const result = await handleAuthRefresh(req, getResponse);
  if (Array.isArray(result)) {
    const [newResp, newTokens] = result;
    const features: any[] = (await newResp.json()).data;
    const ret = new EdgeFunctionResponse(features);
    if (newTokens == null) {
      return ret;
    } else {
      return ret.addCustomData({cookie: {tokens: JSON.stringify(newTokens)}});
    }
  }
  return result;
}
