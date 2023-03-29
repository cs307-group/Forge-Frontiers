import Head from "next/head";
import Image from "next/image";
import Link from "next/link";
import {useRouter} from "next/router";
import {FormEvent, useState} from "react";

import {Button} from "@/components/Button";
import {BaseInput} from "@/components/Input/BaseInput";
import {AppLayout} from "@/components/Layout/AppLayout";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {UserData} from "@/handlers/types";
import {searchUserData} from "@/handlers/user-data";
import avatarImage from "@/images/avatar.png";

const DUP_RES = 1;
export default function Search({data}: {data: UserData[]}) {
  const {push, query} = useRouter();
  const [q, setQ] = useState(() => {
    const qr = query.q;
    if (!qr || Array.isArray(qr)) return "";
    return qr;
  });
  function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const params = new URLSearchParams({q});
    push(`/search?${params.toString()}`);
  }
  return (
    <>
      <Head>
        <title>{`${q ? `${q} - ` : ""}Search | Forge Frontiers`}</title>
        <meta name="description" content="Forge Frontier Web Client" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <AppLayout active="search" title="Search">
        <form onSubmit={handleSubmit}>
          <div className="w-[95%] mx-auto max-w-[400px]">
            <BaseInput
              label="Player"
              name="q"
              value={q}
              onChange={(e) => {
                setQ(e.currentTarget.value);
              }}
            />
            <div className="flex items-center justify-end">
              <Button className="p-2 my-2">Search</Button>
            </div>
          </div>
        </form>
        <ul className="mx-auto max-w-[400px] w-[95%]">
          {data?.length > 0 &&
            (DUP_RES ? data.concat(data).concat(data).concat(data) : data).map(
              (item) => (
                <li key={item.id_} className="w-full rounded-md py-2">
                  <Link
                    href={`/profile/view/${item.id_}`}
                    className="flex items-center w-full gap-2"
                  >
                    <Image
                      className="h-10 w-10"
                      width={40}
                      height={40}
                      src={
                        item.mc_user
                          ? `https://visage.surgeplay.com/head/128/${item.mc_user}`
                          : avatarImage.src
                      }
                      alt="Avatar"
                    />
                    <span>{item.name}</span>
                  </Link>
                  <hr />
                </li>
              )
            )}
        </ul>
      </AppLayout>
    </>
  );
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  const searchResults = await searchUserData(c);
  if (isErrorResponse(searchResults)) {
    return searchResults.resp;
  }
  return searchResults.toSSPropsResult;
});
