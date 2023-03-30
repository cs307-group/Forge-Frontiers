import {jsonRequest, routes} from "./_util";
import {AuthReqContext, EdgeFunctionResponse} from "./fetch-util";
import {MarketStateFetch} from "./types";

export async function getMarketState() {
  const getResponse = () =>
    jsonRequest(routes.marketState, {
      method: "get",
    });
  const result = await getResponse();
  return new EdgeFunctionResponse(
    ((await result.json()).data || {}) as MarketStateFetch
  );
}

export async function getOrdersForSlotId(c: AuthReqContext) {
  const getResponse = () =>
    jsonRequest(routes.ordersBySlotId(c.query.slot_id as any as number), {
      method: "get",
    });
  const result = await getResponse();
  return new EdgeFunctionResponse(
    ((await result.json()).data || {}) as MarketStateFetch
  );
}
