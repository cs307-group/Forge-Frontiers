import {useRouter} from "next/router";

import {IS_BROWSER} from "@/util/no-browser";

export const useLocationHash = IS_BROWSER
  ? function useLocationHash() {
      useRouter();
      const hash = location.hash.substring(1);
      return hash;
    }
  : function () {
      return null;
    };
