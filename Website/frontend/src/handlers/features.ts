import {jsonRequest, routes} from "./_util";

export async function fetchFeatures() {
  const getResponse = () =>
    jsonRequest(routes.getFeatures, {
      method: "get",
    });
	const resp = await (await getResponse()).json();
	console.log(resp);
  return resp.data;
}
