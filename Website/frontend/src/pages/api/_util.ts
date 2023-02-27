const {IS_DEV, API_HOST_PROD, API_HOST_DEVELOPMENT} = process.env;

const API_URL = IS_DEV === "1" ? API_HOST_DEVELOPMENT : API_HOST_PROD;

export function getApiURL(url: string) {
  return new URL(url, API_URL).href;
}

export const routes = {
  register: getApiURL("/users/-/register"),
};

/**
 *
 * @param url
 * @param body
 * @returns
 * this function has an x-debug-from header
 * in the future we could add a security token to the project environment
 * that would prevent the python server to be accessed by any other way than through this tiny proxy
 * this is great as it gives us free security advantage of having a serverless function sanitize requests
 * and make sure nothing awkward is sent to the python server
 * we can also take advantage of the fact that this is an edge function so we don't hurt latency too much
 */
export function jsonRequest(url: string, body: object, method = "post") {
  console.log(url);
  return fetch(url, {
    method,
    body: JSON.stringify(body),
    headers: {
      "content-type": "application/json",
      "x-debug-from": "serverless-functions", // maybe make it a secret?
    },
  });
}
