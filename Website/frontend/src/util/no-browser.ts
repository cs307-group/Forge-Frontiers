/**
 * import this module and use the function
 * to make sure any code in the surrounding scope doesn't make it to the browser
 * this is for server only code such as fetching directly with the python backend
 * we do not want the frontend to do that so we make sure this code throws on a browser
 */

const IS_BROWSER =
  typeof document !== "undefined" && typeof HTMLSpanElement !== "undefined";
document.createElement("span") instanceof HTMLSpanElement;

export function noBrowser() {
  if (IS_BROWSER) {
    throw new Error(
      "This module cannot run in the browser context, don't import"
    );
  }
}
