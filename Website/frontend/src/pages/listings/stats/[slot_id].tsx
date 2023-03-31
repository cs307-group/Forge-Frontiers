import Highcharts from "highcharts";
import {HighchartsReact} from "highcharts-react-official";
import Head from "next/head";
import Image from "next/image";
import {useMemo, useState} from "react";

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
const dateFmt = (date: Date) => {
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const year = String(date.getFullYear()).padStart(4, "0");

  const formattedDate = `${year}-${month}-${day}`;
  return formattedDate;
};
export default function ViewBySlotIdGraph({
  data,
}: {
  data: {
    buy: MarketState[];
    sell: MarketState[];
    lookup: BazaarLookup;
  };
}) {
  const _buyData = useParsedData(data.buy);
  const _sellData = useParsedData(data.sell);
  const combined = useMemo(
    () => _buyData.concat(_sellData).sort((a, b) => +a.x - +b.x),
    []
  );
  const [start, setStart] = useState(() => {
    return combined[0].x;
  });
  const [end, setEnd] = useState(() => {
    return combined[combined.length - 1].x;
  });

  useRefresh();
  const buyData = useMemo(() => {
    return _buyData.filter((x) => {
      return +x.x >= +start && +x.x <= +end;
    });
  }, [start, end, _buyData]);
  const sellData = useMemo(() => {
    return _sellData.filter((x) => +x.x >= +start && +x.x <= +end);
  }, [start, end, _sellData]);

  const options = useChartOptions(buyData, sellData);

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
        <Spacer y={100} />
        <div>
          <div className="text-xl mx-auto text-center mb-4">Range</div>
          <div className="mx-auto flex items-center flex-row justify-center gap-4 text-black">
            <input
              type="date"
              value={dateFmt(start)}
              onChange={(e) => {
                const [y, m, d] = e.currentTarget.value.split("-");
                setStart(new Date(+y, +m - 1, +d));
              }}
            />{" "}
            -{" "}
            <input
              type="date"
              value={dateFmt(end)}
              onChange={(e) => {
                const [y, m, d] = e.currentTarget.value.split("-");
                setEnd(new Date(+y, +m - 1, +d));
              }}
            />
          </div>
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
