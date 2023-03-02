import {useEffect} from "react";

export function useCookieSync(o?: object) {
  useEffect(() => {
    if (o) {
      fetch("/api/sync-auth", {
        body: JSON.stringify(o),
        headers: {"content-type": "application/json"},
        method: "post",
      });
    }
  }, [o]);
}
