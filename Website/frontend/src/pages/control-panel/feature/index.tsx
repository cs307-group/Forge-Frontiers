import {ControlPanelRenderer} from "@/components/ControlPanel";
import {FeatureToggleViewer} from "@/components/ControlPanel/FeatureToggleViewer";
import {requireAdminPageView} from "@/handlers/auth";
import {fetchFeatures} from "@/handlers/features";
import {isErrorResponse} from "@/handlers/fetch-util";
import {FeatureList, UserDataSecure} from "@/handlers/types";
import {useCookieSync} from "@/hooks/use-cookie-sync";

export default function ControlPanel({
  data,
  error,
  user,
  cookie,
}: {
  data: FeatureList;
  error?: string;
  user: UserDataSecure;
  cookie: any;
}) {
  useCookieSync(cookie);
  console.log(data);
  return (
    <ControlPanelRenderer error={error} active="/feature">
      <FeatureToggleViewer />
    </ControlPanelRenderer>
  );
}

export const getServerSideProps = requireAdminPageView(async (c) => {
  const data = await fetchFeatures(c.req);
  if (isErrorResponse(data)) {
    return data.resp;
  }
  console.log(data.resp);
  return data.toSSPropsResult;
});
