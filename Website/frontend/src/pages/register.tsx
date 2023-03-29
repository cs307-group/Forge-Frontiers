import Head from "next/head";
import Link from "next/link";

import {Auth, AuthSideText, AuthSideType} from "@/components/Auth";
import {CreateAccount} from "@/components/Auth/CreateAccount";
import {redirToProf} from "@/util/redir-to-prof";

export default function Register() {
  return (
    <>
      <Head>
        <title>Register | Forge Frontiers</title>
        <meta name="description" content="Forge Frontier Web Client" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Auth>
        <AuthSideText>
          <div className="w-full flex-1 flex items-center flex-col justify-between">
            <div className="text-2xl text-center md:text-5xl">Welcome!</div>
            <p className="mt-4 text-lg md:text-2xl w-[80%] text-center">
              A new journey begins. It is time to explore the world of Forge
              Frontier.
            </p>
            <div className="text-center">
              <div>Already have an account?</div>
              <Link className="underline" href={"/login"}>
                Login
              </Link>
            </div>
          </div>
        </AuthSideText>
        <AuthSideType>
          <div className="text-2xl md:text-5xl mt-4">Register</div>
          <CreateAccount />
        </AuthSideType>
      </Auth>
    </>
  );
}

export const getServerSideProps = redirToProf;
