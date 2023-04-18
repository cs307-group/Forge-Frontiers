import clsx from "clsx";
import {parse} from "cookie";
import {
  default as Doc,
  DocumentContext,
  Head,
  Html,
  Main,
  NextScript,
} from "next/document";

import {isErrorResponse} from "@/handlers/fetch-util";
import {fetchUserData} from "@/handlers/user-data";
import {hasToken} from "@/util/const-has-token";

export default function Document({lightMode}: any) {
  return (
    <Html lang="en" className={clsx(lightMode ? "light" : "dark")}>
      <Head />
      <body>
        <Main />
        <NextScript />
      </body>
    </Html>
  );
}

Document.getInitialProps = async function (ctx: DocumentContext) {
  let result = Doc.getInitialProps(ctx);
  const {req} = ctx;
  const {headers} = req || {};
  const {cookie = ""} = headers || {};
  const cookies = parse(cookie);
  if (!hasToken(cookies)) return result;
  const uData = await fetchUserData({req: {cookies}} as any);
  if (isErrorResponse(uData)) {
    return result;
  }
  const cfg = uData.resp?.secure?.config || {};
  const darkMode = cfg["dark-mode"] ?? true;
  const res = await result;
  return {
    ...res,
    lightMode: !darkMode,
  };
};
