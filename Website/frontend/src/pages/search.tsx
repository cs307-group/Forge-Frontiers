import Head from "next/head";
import Image from "next/image";
import Link from "next/link";
import {useRouter} from "next/router";
import {FormEvent, useState} from "react";

import {Button} from "@/components/Button";
import {BaseInput} from "@/components/Input/BaseInput";
import {AppLayout, CONTROL_PANEL} from "@/components/Layout/AppLayout";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {UserData, UserDataSecure} from "@/handlers/types";
import {fetchUserData, searchUserData} from "@/handlers/user-data";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import avatarImage from "@/images/avatar.png";

const DUP_RES = 0;
export default function Search({
  data,
  cookie,
  user,
}: {
  data: UserData[];
  cookie: object;
  user?: UserDataSecure;
}) {
  useCookieSync(cookie);
  const {push, query} = useRouter();
  const [q, setQ] = useState(() => {
    const qr = query.q;
    if (!qr || Array.isArray(qr)) return "";
    return qr;
  });
  const isQueryStale = query.q == q;
  function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const params = new URLSearchParams({q});
    push(`/search?${params.toString()}`);
  }
  return (
    <>
      <Head>
        <title>{`${q ? `${q} - ` : ""}Search | Forge Frontiers`}</title>
      </Head>
      <AppLayout
        active="search"
        title="Search"
        extraNavItems={user?.is_admin ? CONTROL_PANEL : {}}
      >
        <form onSubmit={handleSubmit}>
          <div className="mx-auto w-[95%] max-w-[400px]">
            <BaseInput
              label="Player"
              name="q"
              value={q}
              onChange={(e) => {
                setQ(e.currentTarget.value);
              }}
            />
            <div className="flex items-center justify-end">
              <Button className="my-2 p-2">Search</Button>
            </div>
          </div>
        </form>
        <ul className="mx-auto w-[95%] max-w-[400px]">
          {data?.length > 0
            ? (DUP_RES
                ? data.concat(data).concat(data).concat(data)
                : data
              ).map((item) => (
                <li key={item.id_} className="w-full rounded-md py-2">
                  <Link
                    href={`/profile/view/${item.id_}`}
                    className="flex w-full items-center gap-2"
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
              ))
            : q &&
              isQueryStale && (
                <div>
                  <div>
                    Your search did not return any results. You could try:
                  </div>
                  <ul>
                    <li className="ml-8 list-disc">
                      Ensuring you did not make any typos
                    </li>
                    <li className="ml-8 list-disc">
                      Searching a part of the username
                    </li>
                  </ul>
                </div>
              )}
        </ul>
      </AppLayout>
    </>
  );
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  const [searchResults, userData] = await Promise.all([
    searchUserData(c),
    fetchUserData(c),
  ]);

  if (isErrorResponse(searchResults)) {
    // hacky but our api kinda sucks
    if (!isErrorResponse(userData)) {
      return {
        props: {
          ...searchResults.resp,
          user: userData.resp,
        },
      };
    }
    return searchResults.resp;
  }
  if (isErrorResponse(userData)) {
    searchResults.addCustomData({user: null});
  } else {
    searchResults.addCustomData({user: userData.resp});
  }
  return searchResults.toSSPropsResult;
});
