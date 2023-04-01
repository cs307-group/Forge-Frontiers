import {motion} from "framer-motion";
import {Raleway} from "next/font/google";
import Link from "next/link";
import {PropsWithChildren} from "react";

const raleway = Raleway({subsets: ["latin"]});
export interface AuthProps extends PropsWithChildren {}
export function Auth({children}: AuthProps) {
  return (
    // don't ask me why the border-1 border-transparent is needed
    <main className="forge_background h-full w-full border-[1px] border-transparent text-white">
      <motion.section
        layout
        className="mx-auto mt-8 flex flex-col items-center justify-center"
      >
        <Link
          href="/"
          style={raleway.style}
          className="mb-4 text-5xl text-ff-theme"
        >
          Forge Frontier
        </Link>
        <motion.div
          layout
          style={raleway.style}
          className="mt-4 w-[90%] rounded-md border-2 border-white bg-white sm:w-7/12"
        >
          <motion.div
            style={raleway.style}
            layout
            className="grid-cols-2 sm:grid"
          >
            {children}
          </motion.div>
        </motion.div>
      </motion.section>
    </main>
  );
}

export function AuthSideText({children}: AuthProps) {
  return (
    <motion.div
      layout
      layoutId="auth-text"
      className="flex flex-col items-center justify-center rounded-br-md rounded-tr-md bg-[#FDF0AD] p-8 text-black"
    >
      {children}
    </motion.div>
  );
}

export function AuthSideType({children}: AuthProps) {
  return (
    <motion.div
      layout
      layoutId="auth-type"
      className="flex flex-col items-center justify-center rounded-bl-md rounded-tl-md bg-[#262C2C] p-4 text-white"
    >
      {children}
    </motion.div>
  );
}
