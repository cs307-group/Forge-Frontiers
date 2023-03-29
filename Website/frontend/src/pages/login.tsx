import Head from "next/head";
import Link from "next/link";

import {Auth, AuthSideText, AuthSideType} from "@/components/Auth";
import {LoginToAccount} from "@/components/Auth/LoginToAccount";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import {redirToProf} from "@/util/redir-to-prof";

export default function Login() {
  useCookieSync({});
  return (
    <>
      <Head>
        <title>Login | Forge Frontiers</title>
        <meta name="description" content="Forge Frontier Web Client" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Auth>
        <AuthSideType>
          <div className="text-2xl md:text-5xl mt-4">Login</div>
          <LoginToAccount />
        </AuthSideType>
        <AuthSideText>
          <div className="w-full flex-1 flex items-center flex-col justify-between">
            <div className="text-2xl text-center md:text-5xl">
              Welcome Back!
            </div>
            <p className="mt-4 text-lg md:text-2xl w-[80%] text-center">
              Good to see you again. The world of Forge Frontier eagerly awaits
              your return.
            </p>
            <div className="text-center">
              <div>Don't have an account?</div>
              <Link className="underline" href={"/register"}>
                Create an account
              </Link>
            </div>
          </div>
        </AuthSideText>
      </Auth>
    </>
  );
}

export const getServerSideProps = redirToProf;
