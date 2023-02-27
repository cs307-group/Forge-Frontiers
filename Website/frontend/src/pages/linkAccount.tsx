import Link from "next/link";

import {Auth, AuthSideText, AuthSideType} from "@/components/Auth";
import {LoginToAccount} from "@/components/Auth/LoginToAccount";
import {LinkAccount} from "@/components/Auth/LinkAccount"
import {Spacer} from "@/components/Spacer";
import {Raleway} from "@next/font/google";

const raleway = Raleway({preload: false, variable: "--raleway"});

export default function LinkAcct() {
  return (
    <Auth>
      <AuthSideType>
        <div className="text-2xl md:text-5xl mt-4">Link Account</div>
        {/* <LoginToAccount /> */}
        <LinkAccount />
      </AuthSideType>
      <AuthSideText>
        <div className="w-full flex-1 flex items-center flex-col justify-between">
          <div className="text-2xl text-center md:text-5xl">Hello!</div>
          <Spacer y={30} />
          <p className="mt-4 text-lg md:text-2xl w-[80%] text-center">
            It looks like you haven't linked your Forge Frontier Profile to
            a Minecraft Account. Use the '/ls' command to generate a code. Link
            your account to continue!
          </p>
        <Spacer y={50} />
        </div>
      </AuthSideText>
    </Auth>
  );
}

export function getServerSideProps() {
  return {props: {ok: new Date().toString()}};
}
