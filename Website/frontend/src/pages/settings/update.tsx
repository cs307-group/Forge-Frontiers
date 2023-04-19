import {AppLayout} from "@/components/Layout/AppLayout";

export default function ({error}: {error: string}) {
  return (
    <AppLayout active="generators" title="Failed to collect">
      <div>{error}</div>
    </AppLayout>
  );
}
