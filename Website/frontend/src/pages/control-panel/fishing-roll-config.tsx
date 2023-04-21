import {ControlPanelRenderer} from "@/components/ControlPanel";
import {FishingRollConfig} from "@/components/ControlPanel/FishingRollConfig";
import {InGameTransactionsViewer} from "@/components/ControlPanel/InGameTransactionsViewer";
import {Spacer} from "@/components/Spacer";
import {fetchFishingRolls} from "@/handlers/admin";
import {requireAdminPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {FishingRoll, ShopData, UserDataSecure} from "@/handlers/types";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import {useRefresh} from "@/hooks/use-refresh";

export default function FishingRollConfigPage({
  error,
  user,
  cookie,
  data,
}: {
  error?: string;
  user: UserDataSecure;
  cookie: any;
  data: FishingRoll[];
}) {
  useRefresh(5000);
  useCookieSync(cookie);
  return (
    <ControlPanelRenderer error={error} active="/fishing-roll-config">
      <Spacer y={30} />
      <FishingRollConfig data={data}></FishingRollConfig>
    </ControlPanelRenderer>
  );
}

export const getServerSideProps = requireAdminPageView(
  async (c, user, cookie) => {
    const fr = await fetchFishingRolls(c);
    if (isErrorResponse(fr)) {
      return fr.resp;
    }
    return fr.addCustomData({cookie, user}).toSSPropsResult;
  }
);
