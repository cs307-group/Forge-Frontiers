import {useRouter} from "next/router";
import {useCallback} from "react";

import {useInterval} from "./use-interval";

export function useRefresh(interval = 2000) {
  const refresh = useManualRefresh();
  useInterval(refresh, interval);
}

export function useManualRefresh() {
  const {replace, asPath} = useRouter();
  return useCallback(
    function () {
      replace(asPath);
    },
    [asPath]
  );
}
