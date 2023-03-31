import Highcharts from "highcharts";
import {HighchartsReact} from "highcharts-react-official";
import Head from "next/head";
import Image from "next/image";
import {useMemo} from "react";

import {AppLayout} from "@/components/Layout/AppLayout";
import {Spacer} from "@/components/Spacer";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {getOrdersForSlotId} from "@/handlers/market";
import {BazaarLookup, MarketState} from "@/handlers/types";
import {useRefresh} from "@/hooks/use-refresh";
import {faker} from "@faker-js/faker";

const FAKE = true;
function useChartOptions(buyData: any, sellData: any) {
  return useMemo(() => {
    const options: Highcharts.Options = {
      title: {
        text: "Market Prices",
      },
      xAxis: {
        type: "datetime",
        title: {
          text: "Date",
        },
      },
      yAxis: {
        title: {
          text: "Price",
        },
      },
      series: [
        {
          type: "line",
          name: "Buy",
          data: buyData as any,
        },
        {type: "line", name: "Sell", data: sellData},
      ],
    };
    return options;
  }, [buyData, sellData]);
}
const fakeData = () =>
  Array.from({length: 50})
    .map(() => {
      return {
        x: faker.date.between(new Date("2020-01-01T00:00:00.000Z"), new Date()), // .toISOString(),
        y: faker.datatype.number({min: 1, max: 1000}),
      };
    })
    .sort((a, b) => {
      const x = +new Date(a.x);
      const y = +new Date(b.x);
      return x - y;
    });

function useParsedData(data: MarketState[]) {
  return useMemo(
    () =>
      FAKE
        ? fakeData()
        : data.map((bid) => ({
            x: new Date(bid.listdate),
            y: bid.price,
          })),
    [data]
  );
}
export default function ViewBySlotIdGraph({
  data,
}: {
  data: {
    buy: MarketState[];
    sell: MarketState[];
    lookup: BazaarLookup;
  };
}) {
  const buyData = useParsedData(data.buy);
  const sellData = useParsedData(data.sell);
  const options = useChartOptions(buyData, sellData);
  useRefresh();
  return (
    <>
      <Head>
        <title>{`Market | Forge Frontiers`}</title>
        <meta name="description" content="Forge Frontier Web Client" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <AppLayout active="market-viewer" title="Market Viewer">
        <Spacer y={20} />
        <div className="flex items-center justify-center">
          <div
            key={data.lookup.slot_id}
            className="bg-[#171717] rounded-md p-4 flex flex-col items-center justify-center"
          >
            <div className="mx-auto text-center"> {data.lookup.item_name} </div>
            <Image
              width={48}
              height={48}
              src={`/items/${data.lookup.item_material}.PNG`}
              alt={data.lookup.item_name}
            />
          </div>
        </div>
        <Spacer y={30} />

        <div className="w-[90%] h-96 bg-white rounded-md mx-auto">
          <HighchartsReact highcharts={Highcharts} options={options} />
        </div>
      </AppLayout>
    </>
  );
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  if (!c.query.slot_id || Array.isArray(c.query.slot_id)) {
    return {props: {error: "Invalid"}};
  }
  const resp = await getOrdersForSlotId(c);

  return resp.toSSPropsResult;
});
