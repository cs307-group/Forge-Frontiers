import {ControlPanelRenderer} from "@/components/ControlPanel";
import {StripeViewer} from "@/components/ControlPanel/StripeViewer";
import {requireAdminPageView} from "@/handlers/auth";
import {UserDataSecure} from "@/handlers/types";
import {useCookieSync} from "@/hooks/use-cookie-sync";

export default function ControlPanel({
  error,
  user,
  cookie,
}: {
  error?: string;
  user: UserDataSecure;
  cookie: any;
}) {
  useCookieSync(cookie);
  return (
    <ControlPanelRenderer error={error}>
      <StripeViewer />
    </ControlPanelRenderer>
  );
}

export const getServerSideProps = requireAdminPageView(
  async (c, user, cookie) => {
    return {props: {cookie, user}};
  }
);
