import {GetServerSideProps} from "next";

import {hasToken} from "./const-has-token";

export const redirToProf: GetServerSideProps = async (c) => {
  const {req, query} = c;

  if (hasToken(req.cookies) && !("force" in query)) {
    return {redirect: {destination: "/profile?auth", statusCode: 302}};
  }
  return {props: {}};
};
