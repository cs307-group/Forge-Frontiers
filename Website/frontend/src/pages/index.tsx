import Head from "next/head";
import Image from "next/image";
import Link from "next/link";

import Logo from "@/images/logo.png";
import {Raleway} from "next/font/google";

const raleway = Raleway({subsets: ["latin"]});
export default function Home() {
  return (
    <>
      <Head>
        <title>Forge Frontiers</title>
        <meta name="description" content="Forge Frontier Web Client" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <main
        style={raleway.style}
        className="mx-auto text-center pt-8 forge_background h-full w-full text-white border-[1px] border-transparent"
      >
        <Image
          className="mx-auto rounded-md"
          alt="Logo"
          src={Logo.src}
          width={Logo.width}
          height={Logo.height}
        />
        <div>Welcome to Forge Frontiers</div>
        <br />
        <div className="flex items-center justify-center gap-4 mt-6">
          <div>
            <Link
              className="bg-white inline-block w-24 hover:bg-gray-100 text-gray-800 font-semibold py-2 px-4 border border-gray-400 rounded shadow"
              href="/login"
            >
              Login
            </Link>
          </div>

          <div>
            <Link
              className="bg-white inline-block w-24 hover:bg-gray-100 text-gray-800 font-semibold py-2 px-4 border border-gray-400 rounded shadow"
              href="/register"
            >
              Register
            </Link>
          </div>
        </div>
      </main>
    </>
  );
}
