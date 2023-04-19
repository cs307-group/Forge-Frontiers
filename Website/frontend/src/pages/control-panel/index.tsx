import {fetchShopStatus} from "@/handlers/admin";
import {requireAdminPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";

export {default as default} from "@/components/ControlPanel/default";

export const getServerSideProps = requireAdminPageView(
  async (c, user, cookie) => {
    const shop = await fetchShopStatus(c);
    if (isErrorResponse(shop)) {
      return shop.resp
    }
    return shop.addCustomData({cookie, user}).toSSPropsResult;
  }
);
