import {useEffect, useMemo, useState} from "react";
function toPx(value: any) {
  return typeof value === "number" ? `${value}px` : value;
}

export function useMedia(query: string) {
  const q = useMemo(
    () =>
      typeof window !== "undefined"
        ? window.matchMedia(query)
        : ({matches: false} as MediaQueryList),
    [query]
  );
  const [matches, setMatches] = useState(() => q.matches);
  useEffect(() => {
    setMatches(q.matches);
    function listener(e: MediaQueryListEvent) {
      setMatches(e.matches);
    }
    q.addEventListener("change", listener);
    return () => q.removeEventListener("change", listener);
  }, [q]);
  return matches;
}

useMedia.usePrefersReducedMotion = function () {
  return useMedia("(prefers-reduced-motion)");
};

useMedia.usePrefersDarkTheme = function () {
  return useMedia("(prefers-color-scheme: dark)");
};

useMedia.useMinWidth = function (width: number | string) {
  return useMedia(`(min-width:${toPx(width)})`);
};

useMedia.useMaxWidth = function (width: number | string) {
  return useMedia(`(max-width:${toPx(width)})`);
};
useMedia.useMinHeight = function (height: number | string) {
  return useMedia(`(min-height:${toPx(height)})`);
};

useMedia.useMaxHeight = function (height: number | string) {
  return useMedia(`(max-height:${toPx(height)})`);
};
