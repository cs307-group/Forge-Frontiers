import {Raleway} from "next/font/google";
import Head from "next/head";
import Image from "next/image";
import Link from "next/link";

import Logo from "@/images/logo.png";

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
        className="forge_background mx-auto h-full w-full border-[1px] border-transparent pt-8 text-center text-white"
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
        <div className="mt-6 flex items-center justify-center gap-4">
          <div>
            <Link
              className="inline-block w-24 rounded border border-gray-400 bg-white px-4 py-2 font-semibold text-gray-800 shadow hover:bg-gray-100"
              href="/login"
            >
              Login
            </Link>
          </div>

          <div>
            <Link
              className="inline-block w-24 rounded border border-gray-400 bg-white px-4 py-2 font-semibold text-gray-800 shadow hover:bg-gray-100"
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
