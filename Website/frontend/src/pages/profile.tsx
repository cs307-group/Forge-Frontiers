import Image from "next/image";

import {AppLayout} from "@/components/Layout/AppLayout";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {UserDataSecure} from "@/handlers/types";
import {fetchUserData, isErrorResponse} from "@/handlers/user-data";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import avatarImage from "@/images/avatar.png";
import skillsImage from "@/images/skills.png";

export default function Profile({
  userData,
  cookie,
}: {
  userData: UserDataSecure;
  cookie?: object;
}) {
  useCookieSync(cookie);
  return (
    <AppLayout active="profile" title={`${userData.name}'s Profile`}>
      <div>
        {/* TODO DYNAMIC IMAGE */}
        <Image
          className="h-80 w-40 mt-4"
          width={316}
          height={512}
          src={
            userData.mc_user
              ? `https://visage.surgeplay.com/full/512/${userData.mc_user}`
              : avatarImage.src
          }
          alt="Avatar"
        />
        <div className="flex-1">
          <Image
            className="h-80 w-full mt-4 hidden sm:block"
            src={skillsImage.src}
            height={skillsImage.height}
            width={skillsImage.width}
            alt="Skills"
          />
          <span className="italic">Skills data needs to be fetched</span>
        </div>
        <div className="flex item-center justify-between">
          <h2 className="text-xl">Inventory</h2>
          <h2 className="text-xl">Market Listings</h2>
        </div>
      </div>
    </AppLayout>
  );
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  const userData = await fetchUserData(c);
  if (isErrorResponse(userData)) {
    return userData.resp;
  }
  return userData.toSSPropsResult;
});
