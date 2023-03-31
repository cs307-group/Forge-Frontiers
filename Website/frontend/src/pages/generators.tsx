import Head from "next/head";
import Image from "next/image";
import Link from "next/link";
import {useRouter} from "next/router";
import React, {FormEvent, useState, useEffect} from "react";
import Grid from "@mui/material/Grid";
import Moment from "react-moment";

import {AppLayout} from "@/components/Layout/AppLayout";
import {Spacer} from "@/components/Spacer";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {fetchGeneratorsByIsland} from "@/handlers/gens";
import {GeneratorStateFetch, UserDataSecure, GeneratorState} from "@/handlers/types";
import {fetchUserData} from "@/handlers/user-data";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import {Button} from "@/components/Button";
import {GenBlock} from "@/components/Gens/GenBlock"


export default function Generators({
  data: userData,
  gens: genData,
  cookie,
}: {
  data: UserDataSecure;
  gens: GeneratorStateFetch;
  cookie?: object;
}) {
  useCookieSync(cookie);
  // console.log(userData);
  console.log(genData.data.stashes);
  // 
  const [stash, setStash] = React.useState(genData.stashes);
  const [time, setTime] = React.useState(Date.now());
  // format data like this
  const [temp, updateTemp] = React.useState(
             [{type:"silver-gen", level:0, rate:3000, last_collected: 1680213062023, max: 256},
              {type:"coin-gen", level:1, rate:500, last_collected: 1680213062023, max: 10000},
              {type:"silver-gen", level:0, rate:6000, last_collected: 1680213062023, max: 256},
              {type:"coin-gen", level:1, rate:1000, last_collected: 1680213062023, max: 10000},
              {type:"silver-gen", level:0, rate:8000, last_collected: 1680213062023, max: 256},
              {type:"coin-gen", level:1, rate:100, last_collected: 1680213062023, max: 10000},
              {type:"silver-gen", level:0, rate:9000, last_collected: 1680213062023, max: 256},
              {type:"coin-gen", level:1, rate:400, last_collected: 1680213062023, max: 10000}]);

  var x = Date.now();
  console.log(x);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    var currTime = Date.now();

    setTime(currTime);

    console.log(currTime);
    updateTemp(prevState => {
      let prev = prevState;   

      prev.forEach(element => {element.last_collected = currTime});
      // console.log(prev)
      return prev ;  
    });

    // push changes for last_collected to server for all gens and update stash
  }
                            

  return (
    <>
      <AppLayout active="generators" title={`${userData.name}'s Generators`}>
        <div>
          {console.log(temp)}
          <Spacer y={60} />
          <Grid key={time} container spacing={{ sm: 4, md: 4 }} columns={{sm: 8, md: 12 }}>
            {temp.map((x, i) =>
              <Grid item sm={4} md={4} key={i}>
                <GenBlock
                  type={x.type} 
                  level={x.level}
                  rate={x.rate}
                  last_collected={x.last_collected}
                  curr_cap = {(time - x.last_collected)/x.rate < x.max ? (time - x.last_collected)/x.rate : x.max}
                  max={x.max}/>
              </Grid>
            )}
          </Grid>
          <Spacer y={60} />
          <form onSubmit={handleSubmit}>
            <Button className="w-full mt-4 p-2">Collect</Button>
          </form>
        </div>
      </AppLayout>
    </>
  );
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  console.log("hello");
  const userData = await fetchUserData(c);
  if (isErrorResponse(userData)) {
    return userData.resp;
  }
  const genData = await fetchGeneratorsByIsland(userData.resp.island_id);
  const res = userData.addCustomData({gens: genData.resp}).toSSPropsResult;
  return res;
});
