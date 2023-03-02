import {GetServerSideProps} from "next";

import {getAuthenticationHeaders, jsonRequest, routes} from "@/handlers/_util";
import {
  handleTokenRefresh,
  isRefreshSuccess,
  requireAuthenticatedPageView,
} from "@/handlers/auth";
import {Tokens, UserDataSecure} from "@/handlers/types";
import {useCookieSync} from "@/hooks/use-cookie-sync";

export default function Profile({
  userData,
  cookie,
}: {
  userData: UserDataSecure;
  cookie?: object;
}) {
  useCookieSync(cookie);
  return <>{JSON.stringify({userData, cookie})}</>;
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  const {req} = c;
  const tokens: Tokens = JSON.parse(req.cookies.tokens);

  const getResponse = ($tokens: Tokens) =>
    jsonRequest(routes.userInfo, {
      method: "get",
      headers: getAuthenticationHeaders($tokens),
    });
  let resp = await getResponse(tokens);

  if (!resp.ok) {
    const refresh = await handleTokenRefresh(resp, tokens);
    if (isRefreshSuccess(refresh)) {
      const newTokens = refresh.tokens;
      const newResp = await getResponse(newTokens);

      if (!newResp.ok) {
        return {
          props: {
            error: (await resp.json()).error,
          },
        };
      }

      return {
        props: {
          userData: await newResp.json(),
          cookie: {tokens: JSON.stringify(newTokens)},
        },
      };
    } else {
      return refresh.response;
    }
  }

  return {
    props: {userData: await resp.json()},
  };
});
