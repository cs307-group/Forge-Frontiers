export function hasToken(cookies: any): cookies is string {
  try {
    return Boolean(
      cookies &&
        "tokens" in cookies &&
        cookies.tokens &&
        JSON.parse(cookies.tokens)
    );
  } catch (_) {
    return false;
  }
}
