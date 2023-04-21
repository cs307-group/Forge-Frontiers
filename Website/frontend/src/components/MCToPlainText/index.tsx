export function MCToPlainText({text}: {text: string}) {
  const str = text.replace(/[§\&]./g, "");
  return <span data-debug-orig={text}>{str}</span>;
}
