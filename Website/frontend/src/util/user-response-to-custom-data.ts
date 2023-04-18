import {
  EdgeFunctionResponse,
  ErrorResponse,
  isErrorResponse,
} from "@/handlers/fetch-util";
import {UserDataSecure} from "@/handlers/types";

export function userResponseToCustomData(
  user: ErrorResponse | EdgeFunctionResponse<UserDataSecure>
) {
  if (isErrorResponse(user)) {
    return {user: null};
  }
  return {user: user.resp, cookie: user.extractCookie() || null};
}
