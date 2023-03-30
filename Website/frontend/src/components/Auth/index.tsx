import {motion} from "framer-motion";
import {Raleway} from "next/font/google";
import Link from "next/link";
import {PropsWithChildren} from "react";

const raleway = Raleway({subsets: ["latin"]});
export interface AuthProps extends PropsWithChildren {}
export function Auth({children}: AuthProps) {
  return (
    // don't ask me why the border-1 border-transparent is needed
    <main className="forge_background h-full w-full text-white border-[1px] border-transparent">
      <motion.section
        layout
        className="mx-auto mt-8 flex items-center justify-center flex-col"
      >
        <Link
          href="/"
          style={raleway.style}
          className="text-ff-theme text-5xl mb-4"
        >
          Forge Frontier
        </Link>
        <motion.div
          layout
          style={raleway.style}
          className="border-2 border-white w-[90%] sm:w-7/12 mt-4 rounded-md bg-white"
        >
          <motion.div
            style={raleway.style}
            layout
            className="sm:grid grid-cols-2"
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
      className="bg-[#FDF0AD] flex items-center flex-col justify-center text-black rounded-tr-md rounded-br-md p-8"
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
      className="bg-[#262C2C] flex items-center flex-col justify-center text-white rounded-tl-md rounded-bl-md p-4"
    >
      {children}
    </motion.div>
  );
}
