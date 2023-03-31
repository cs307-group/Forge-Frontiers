import {GetServerSidePropsResult} from "next";

import {getAuthenticationHeaders, jsonRequest, routes} from "./_util";
import {
  AuthReqContext,
  EdgeFunctionResponse,
  ErrorResponse,
  handleAuthRefresh,
  isErrorResponse,
} from "./fetch-util";
import {GeneratorStateFetch, Tokens} from "./types";

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

export async function collectGenerators(
  c: AuthReqContext
): Promise<GetServerSidePropsResult<any>> {
  const {req} = c;

  const getResponse = ($tokens: Tokens) =>
    jsonRequest(routes.collectGenerator(c.query.island_id as any), {
      method: "get",
      headers: getAuthenticationHeaders($tokens),
    });
  const h = await handleAuthRefresh(req, getResponse);
  if (isErrorResponse(h)) return h.resp;
  return {redirect: {destination: "/generators", statusCode: 302}};
}
