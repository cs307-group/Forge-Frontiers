import {jsonRequest, routes} from "./_util";
import {EdgeFunctionResponse, ErrorResponse} from "./fetch-util";
import {FeatureList} from "./types";

export async function fetchFeatures() {
  const getResponse = () =>
    jsonRequest(routes.getFeatures, {
      method: "get",
    });

  const resp = await getResponse();
  if (!resp.ok) {
    return new ErrorResponse({props: await resp.json()});
  }

  const featData: FeatureList = (await resp.json()).data;
  console.log(featData);
  return new EdgeFunctionResponse(featData);
}
