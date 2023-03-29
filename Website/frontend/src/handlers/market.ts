import {jsonRequest, routes} from "./_util";
import {AuthReqContext, EdgeFunctionResponse} from "./fetch-util";
import {BazaarLookup, MarketState, MarketStateFetch} from "./types";

export async function getMarketState(c: AuthReqContext) {
  const getResponse = () =>
    jsonRequest(routes.marketState, {
      method: "get",
    });
  const result = await getResponse();
  return new EdgeFunctionResponse(
    ((await result.json()).data || {}) as MarketStateFetch
  );
}
