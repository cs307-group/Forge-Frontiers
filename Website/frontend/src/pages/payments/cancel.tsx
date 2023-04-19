import {AppLayout} from "@/components/Layout/AppLayout";
import {requireAuthenticatedPageView} from "@/handlers/auth";

export default function Success({}: {}) {
  return (
    <AppLayout active="faq" title="FAQ">
      Your payment failed. Any charges made will be reversed soon.
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
