import {AppLayout, CONTROL_PANEL} from "@/components/Layout/AppLayout";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {UserData, UserDataSecure} from "@/handlers/types";
import {fetchUserData, searchUserData} from "@/handlers/user-data";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import {userResponseToCustomData} from "@/util/user-response-to-custom-data";

export default function Stash({
  data,
  cookie,
  user,
}: {
  data: UserData[];
  cookie: object;
  user?: UserDataSecure;
}) {
  useCookieSync(cookie);

	return <AppLayout 
	active="stash-viewer" 
	title="Stash Viewer"
	extraNavItems={user?.is_admin ? CONTROL_PANEL : {}}
	>ok</AppLayout>;
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  const [searchResults, userData] = await Promise.all([
    searchUserData(c),
    fetchUserData(c),
  ]);

  if (isErrorResponse(searchResults)) {
    // hacky but our api kinda sucks
    return {
      props: {
        ...searchResults.resp,
        ...userResponseToCustomData(userData),
      },
    };
  }
  searchResults.addCustomData(userResponseToCustomData(userData));

  return searchResults.toSSPropsResult;
});
