import {AppLayout} from "@/components/Layout/AppLayout";
import {config} from "@/config";
import {getAuthenticationHeaders, jsonRequest, routes} from "@/handlers/_util";
import {
  handleTokenRefresh,
  isRefreshSuccess,
  requireAuthenticatedPageView,
} from "@/handlers/auth";
import {Tokens, UserDataSecure} from "@/handlers/types";
import {fetchUserData, isErrorResponse} from "@/handlers/user-data";
import {useCookieSync} from "@/hooks/use-cookie-sync";

export default function Profile({
  userData,
  cookie,
}: {
  userData: UserDataSecure;
  cookie?: object;
}) {
  useCookieSync(cookie);

  return <AppLayout active="profile">ok</AppLayout>;
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  const userData = await fetchUserData(c);
  if (isErrorResponse(userData)) {
    return userData.resp;
  }
  return userData.toSSPropsResult;
});
