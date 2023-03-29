import Head from "next/head";

import {Auth, AuthSideText, AuthSideType} from "@/components/Auth";
import {LinkAccount} from "@/components/Auth/LinkAccount";
import {requireAuthenticatedPageView} from "@/handlers/auth";

export default function Links() {
  return (
    <>
      <Head>
        <title>Link | Forge Frontiers</title>
        <meta name="description" content="Forge Frontier Web Client" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Auth>
        <AuthSideType>
          <div className="text-2xl md:text-5xl mt-4">Link</div>
          <LinkAccount />
        </AuthSideType>
        <AuthSideText>
          <div className="w-full flex items-center flex-col">
            <div className="text-2xl text-center md:text-5xl">Hello!</div>
          </div>
          <p className="mt-4 text-lg md:text-2xl w-[80%] text-center flex-1 flex items-center justify-center">
            It looks like you haven't linked your Forge Frontier Profile to a
            Minecraft account. To generate a code, use the `/link` command. Link
            your account to continue!
          </p>
        </AuthSideText>
      </Auth>
    </>
  );
}

export const getServerSideProps = requireAuthenticatedPageView(async (_) => {
  return {props: {now: new Date().toString()}};
});
