import {requireAuthenticatedPageView} from "@/handlers/auth";

export default function ({error}: {error: string}) {
  return <div>{error}</div>;
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  const error = c.query.error;
  if (error) return {props: {error}};
  return {props: {}};
});
