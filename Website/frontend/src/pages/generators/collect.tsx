import {requireAuthenticatedPageView} from "@/handlers/auth";
import {collectGenerators} from "@/handlers/gens";

export default function ({error}: {error: string}) {
  return <div>{error}</div>;
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  const id = c.query.island_id;
  if (!id || Array.isArray(id)) {
    return {props: {error: "Invalid"}};
  }
  const resp = await collectGenerators(c);
  return resp;
});
