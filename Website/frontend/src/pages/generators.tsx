import Head from "next/head";
import Image from "next/image";
import Link from "next/link";
import {useRouter} from "next/router";
import {FormEvent, useState} from "react";

import {AppLayout} from "@/components/Layout/AppLayout";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {fetchGeneratorsByIsland} from "@/handlers/gens";
import {GeneratorStateFetch, UserDataSecure, GeneratorState} from "@/handlers/types";
import {fetchUserData} from "@/handlers/user-data";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import {Button} from "@/components/Button";
import {GenBlock} from "@/components/Gens/GenBlock"


export default function Profile({
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
  console.log(genData);
  // 
  var temp = [{type:"silver-gen", level:0, rate:3000, last_collected: 1680213062023}, 
              {type:"coin-gen", level:1, rate:500, last_collected: 1680213062023}];

  return (
    <>
      <AppLayout
        active="generators"
        title={`${userData.name}'s Generators`}>
      </AppLayout>
      {temp.map((x, i) =>
        <GenBlock
        type={x.type} 
        level={x.level}
        rate={x.rate}
        last_collected={x.last_collected}/>
      )}
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
  //console.log(genData);
  // if (isErrorResponse(genData)) {
  //   return genData.resp;
  // }

  const res = userData.addCustomData({gens: genData.resp}).toSSPropsResult;
  return res;
});
