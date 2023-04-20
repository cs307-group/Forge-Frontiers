import {AppLayout} from "@/components/Layout/AppLayout";
import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {searchUserData} from "@/handlers/user-data";

export default function ({error}: {error: string}) {
  return (
    <AppLayout active={null} title="User not found">
      {error}
    </AppLayout>
  );
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  const searchResults = await searchUserData(c);

  if (isErrorResponse(searchResults)) {
    // hacky but our api kinda sucks
    return searchResults.resp;
  }
  const result = searchResults.resp.find((x) => x.mc_user === c.query.q);
  if (!result) return {props: {error: "user not found"}};
  return {
    redirect: {
      destination: `/profile/view/${result.id_}`,
      permanent: false,
    },
  };
});
