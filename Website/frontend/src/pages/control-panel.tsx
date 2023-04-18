import {AppLayout} from "@/components/Layout/AppLayout";
import {requireAdminPageView} from "@/handlers/auth";

export default function ControlPanel({error}: {error?: string}) {
  if (error)
    return (
      <AppLayout active="control-panel">
        <div>You are not an Admin {error}</div>
      </AppLayout>
    );
  return (
    <AppLayout active="control-panel">
      <div>You are an admin{error}</div>
    </AppLayout>
  );
}

export const getServerSideProps = requireAdminPageView(async (c) => {
  return {props: {}};
});
