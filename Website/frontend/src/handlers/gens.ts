import {jsonRequest, routes} from "./_util";
import {AuthReqContext, EdgeFunctionResponse} from "./fetch-util";
import {GeneratorStateFetch} from "./types";

export async function getGeneratorsByIsland(c: AuthReqContext) {
  const getResponse = () =>
    jsonRequest(routes.generatorsByIsland(c.query.island_id as any as string), {
      method: "get",
    });
  const result = await getResponse();
  return new EdgeFunctionResponse(
    ((await result.json()).data || {}) as GeneratorStateFetch
  );
}
