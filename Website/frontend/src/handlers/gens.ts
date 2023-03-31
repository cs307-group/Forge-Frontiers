import {jsonRequest, routes} from "./_util";
import {AuthReqContext, EdgeFunctionResponse, ErrorResponse} from "./fetch-util";
import {GeneratorStateFetch} from "./types";

// export async function fetchGeneratorsByIsland(island_id: str) {
//   const getResponse = () =>
//     jsonRequest(routes.generatorsByIsland(island_id), {
//       method: "get",
//     });
//   const result = await getResponse();
//   return new EdgeFunctionResponse(
//     ((await result.json()).data || {}) as GeneratorStateFetch
//   );
// }

export async function fetchGeneratorsByIsland(id: string) {
  const getResponse = () =>
    jsonRequest(routes.generatorsByIsland(id), {
      method: "get",
    });
  const resp = await getResponse();
  if (!resp.ok) {
    return new ErrorResponse({props: await resp.json()});
  }
  const genData: GeneratorStateFetch = (await resp.json());
  console.log(genData);
  return new EdgeFunctionResponse(genData);
}

