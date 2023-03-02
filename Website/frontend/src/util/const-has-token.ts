export function hasToken(cookies: Record<string, any>) {
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
