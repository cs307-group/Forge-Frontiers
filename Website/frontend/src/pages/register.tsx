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

      </Head>
      <Auth>
        <AuthSideText>
          <div className="flex w-full flex-1 flex-col items-center justify-between">
            <div className="text-center text-2xl md:text-5xl">Welcome!</div>
            <p className="mt-4 w-[80%] text-center text-lg md:text-2xl">
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
          <div className="mt-4 text-2xl md:text-5xl">Register</div>
          <CreateAccount />
        </AuthSideType>
      </Auth>
    </>
  );
}

export const getServerSideProps = redirToProf;
