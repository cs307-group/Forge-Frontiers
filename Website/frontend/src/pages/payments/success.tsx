import {AppLayout} from "@/components/Layout/AppLayout";
import {requireAuthenticatedPageView} from "@/handlers/auth";

export default function Success({}: {}) {
  return (
    <AppLayout active="faq" title="FAQ">
      Your payment succeeded!
    </AppLayout>
  );
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  //   const userData = await fetchUserData(c);
  //   if (isErrorResponse(userData)) {
  //     return userData.resp;
  //   }
  //   return userData.toSSPropsResult;
  return {props: {}};
});
