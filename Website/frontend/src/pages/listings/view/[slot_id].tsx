import {AppLayout} from "@/components/Layout/AppLayout";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {getOrdersForSlotId} from "@/handlers/market";
import {MarketState} from "@/handlers/types";

export default function ViewBySlotId({
  data,
}: {
  data: {
    buy: MarketState[];
    sell: MarketState[];
  };
}) {
  console.log(data);
  return <AppLayout active="market-viewer" title="Market Viewer"></AppLayout>;
}
export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  if (!c.query.slot_id || Array.isArray(c.query.slot_id)) {
    return {props: {error: "Invalid"}};
  }
  const resp = await getOrdersForSlotId(c);

  return resp.toSSPropsResult;
});
