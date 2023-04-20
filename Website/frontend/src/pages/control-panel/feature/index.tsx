import {ControlPanelRenderer} from "@/components/ControlPanel";
import {FeatureToggleViewer} from "@/components/ControlPanel/FeatureToggleViewer";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {fetchFeatures} from "@/handlers/features";
import {isErrorResponse} from "@/handlers/fetch-util";
import {UserDataSecure} from "@/handlers/types";
import {useCookieSync} from "@/hooks/use-cookie-sync";

export default function ControlPanel({
  data: featData,
  error,
  user,
  cookie,
}: {
  data: string;
  error?: string;
  user: UserDataSecure;
  cookie: any;
}) {
  useCookieSync(cookie);

  return (
    <ControlPanelRenderer error={error} active="/feature">
      <FeatureToggleViewer />
    </ControlPanelRenderer>
  );
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  const featData = await fetchFeatures();
  if (isErrorResponse(featData)) {
    return featData.resp;
  }
  return featData;
});

