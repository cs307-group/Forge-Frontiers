import {AppLayout} from "@/components/Layout/AppLayout";
import {requireAdminPageView} from "@/handlers/auth";
import {UserData, UserDataSecure} from "@/handlers/types";
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
  if (error)
    return (
      <AppLayout active="control-panel">
        <div>Error: {error}</div>
      </AppLayout>
    );
  return (
    <AppLayout active="control-panel">
      <div>Error: {error}</div>
    </AppLayout>
  );
}

export const getServerSideProps = requireAdminPageView(
  async (c, user, cookie) => {
    return {props: {cookie, user}};
  }
);
