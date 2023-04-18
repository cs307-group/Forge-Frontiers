import {jsonRequest, routes} from "./_util";
import {EdgeFunctionResponse, ErrorResponse} from "./fetch-util";
import {GeneratorStateFetch} from "./types";

export async function fetchGeneratorsByIsland(id: string) {
  const getResponse = () =>
    jsonRequest(routes.generatorsByIsland(id), {
      method: "get",
    });
  const resp = await getResponse();
  if (!resp.ok) {
    return new ErrorResponse({props: await resp.json()});
  }
  const genData: GeneratorStateFetch = (await resp.json()).data;
  // console.log(genData);
  return new EdgeFunctionResponse(genData);
}

export async function fetchGeneratorConfig() {
  const getResponse = () =>
    jsonRequest(routes.generatorConfig, {
      method: "get",
    });
  const resp = await (await getResponse()).json();
  return resp.data;
}
