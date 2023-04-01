import {Raleway} from "next/font/google";
import Head from "next/head";

import {Auth, AuthSideText, AuthSideType} from "@/components/Auth";
import {LinkAccount} from "@/components/Auth/LinkAccount";
import {Spacer} from "@/components/Spacer";

const raleway = Raleway({preload: false, variable: "--raleway"});

export default function LinkAcct() {
  return (
    <>
      <Head>
        <title>{`Link | Forge Frontiers`}</title>
        <meta name="description" content="Forge Frontier Web Client" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Auth>
        <AuthSideType>
          <div className="mt-4 text-2xl md:text-5xl">Link Account</div>
          <LinkAccount />
        </AuthSideType>
        <AuthSideText>
          <div className="flex w-full flex-1 flex-col items-center justify-between">
            <div className="text-center text-2xl md:text-5xl">Hello!</div>
            <Spacer y={30} />
            <p className="mt-4 w-[80%] text-center text-lg md:text-2xl">
              It looks like you haven't linked your Forge Frontier Profile to a
              Minecraft Account. Use the '/link' command to generate a code.
              Link your account to continue!
            </p>
            <Spacer y={50} />
          </div>
        </AuthSideText>
      </Auth>
    </>
  );
}

export function getServerSideProps() {
  return {props: {ok: new Date().toString()}};
}
