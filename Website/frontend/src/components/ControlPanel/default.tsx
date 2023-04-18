import {ControlPanelRenderer} from "@/components/ControlPanel";
import {InGameTransactionsViewer} from "@/components/ControlPanel/InGameTransactionsViewer";
import {ShopData, UserDataSecure} from "@/handlers/types";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import {useRefresh} from "@/hooks/use-refresh";

import {Spacer} from "../Spacer";

export default function ControlPanel({
  error,
  user,
  cookie,
  data: shop,
}: {
  error?: string;
  user: UserDataSecure;
  cookie: any;
  data: ShopData[];
}) {
  useRefresh(5000);
  useCookieSync(cookie);
  return (
    <ControlPanelRenderer error={error}>
      <Spacer y={30} />
      <InGameTransactionsViewer shop={shop} />
    </ControlPanelRenderer>
  );
}
