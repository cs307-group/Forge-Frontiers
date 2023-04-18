import Highcharts from "highcharts";
import {HighchartsReact} from "highcharts-react-official";
import Head from "next/head";
import Image from "next/image";
import {useMemo, useState} from "react";

import {AppLayout, CONTROL_PANEL} from "@/components/Layout/AppLayout";
import {Spacer} from "@/components/Spacer";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {getOrdersForSlotId} from "@/handlers/market";
import {BazaarLookup, MarketState, UserDataSecure} from "@/handlers/types";
import {fetchUserData} from "@/handlers/user-data";
import {useRefresh} from "@/hooks/use-refresh";
import {userResponseToCustomData} from "@/util/user-response-to-custom-data";
import {faker} from "@faker-js/faker";

const FAKE = false;
const HARD_CODE = true;
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
  Array.from({length: 10})
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
const fakeBUY = [
  {
    x: "2021-11-16T22:27:42.292Z",
    y: 324,
  },
  {
    x: "2022-01-14T20:47:53.377Z",
    y: 848,
  },
  {
    x: "2022-04-21T14:10:30.977Z",
    y: 936,
  },
  {
    x: "2022-06-13T22:23:55.707Z",
    y: 383,
  },
  {
    x: "2022-06-28T02:53:30.513Z",
    y: 573,
  },
  {
    x: "2022-07-16T08:15:15.198Z",
    y: 211,
  },
  {
    x: "2022-12-02T09:37:15.045Z",
    y: 421,
  },
  {
    x: "2023-01-16T14:03:03.049Z",
    y: 802,
  },
];
const fakeSell = [
  {
    x: "2020-12-14T08:35:18.592Z",
    y: 555,
  },
  {
    x: "2021-02-14T20:29:38.995Z",
    y: 506,
  },
  {
    x: "2021-03-03T08:25:07.475Z",
    y: 204,
  },
  {
    x: "2021-06-22T08:46:24.271Z",
    y: 254,
  },
  {
    x: "2022-01-28T09:12:11.069Z",
    y: 480,
  },
  {
    x: "2022-06-17T00:48:24.619Z",
    y: 497,
  },
  {
    x: "2022-09-09T11:15:06.903Z",
    y: 264,
  },
  {
    x: "2022-11-28T07:57:37.371Z",
    y: 679,
  },
  {
    x: "2022-12-11T06:37:59.108Z",
    y: 507,
  },
];
const dateFmt = (date: Date) => {
  if (!date) return undefined as any;
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const year = String(date.getFullYear()).padStart(4, "0");

  const formattedDate = `${year}-${month}-${day}`;
  return formattedDate;
};
export default function ViewBySlotIdGraph({
  data,
  user,
}: {
  data: {
    buy: MarketState[];
    sell: MarketState[];
    lookup: BazaarLookup;
  };
  user?: UserDataSecure;
}) {
  let _buyData = useParsedData(data.buy);
  let _sellData = useParsedData(data.sell);
  if (HARD_CODE) {
    _buyData = fakeBUY.map((x) => ({x: new Date(x.x), y: x.y}));
    _sellData = fakeSell.map((x) => ({x: new Date(x.x), y: x.y}));
  }
  const combined = useMemo(
    () => _buyData.concat(_sellData).sort((a, b) => +a.x - +b.x),
    []
  );
  const [start, setStart] = useState(() => {
    return combined[0]?.x;
  });
  const [end, setEnd] = useState(() => {
    return combined[(combined.length || 1) - 1]?.x;
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

  console.log(buyData, sellData);
  const options = useChartOptions(buyData, sellData);

  return (
    <>
      <Head>
        <title>{`Market | Forge Frontiers`}</title>
      </Head>
      <AppLayout
        active="market-viewer"
        title="Market Viewer"
        extraNavItems={user?.is_admin ? CONTROL_PANEL : {}}
      >
        <Spacer y={20} />
        <div className="flex items-center justify-center">
          <div
            key={data.lookup.slot_id}
            className="flex flex-col items-center justify-center rounded-md bg-[#171717] p-4"
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

        <div className="mx-auto h-96 w-[90%] rounded-md bg-white">
          <HighchartsReact highcharts={Highcharts} options={options} />
        </div>
        <Spacer y={100} />
        <div>
          <div className="mx-auto mb-4 text-center text-xl">Range</div>
          <div className="mx-auto flex flex-row items-center justify-center gap-4 text-black">
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
  const [resp, user] = await Promise.all([
    getOrdersForSlotId(c),
    fetchUserData(c),
  ]);

  resp.addCustomData(userResponseToCustomData(user));
  return resp.toSSPropsResult;
});
