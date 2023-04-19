import {useRouter} from "next/router";
import {useCallback} from "react";

import {useInterval} from "./use-interval";

function shouldAutoSync() {
  const doc = document.documentElement;
  const {config} = doc.dataset;
  const json = JSON.parse(config || "{}");
  return json.autoSync ?? true;
}

let cachedShouldAutoSync = (() => {
  let res;
  if (typeof document !== "undefined") {
    res = shouldAutoSync();
  } else {
    res = true;
  }
  return res;
})();

export function useRefresh(interval = 2000) {
  const refresh = useManualRefresh();
  useInterval(refresh, cachedShouldAutoSync ? interval : null);
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
