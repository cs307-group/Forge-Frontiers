export function Spacer({x = 0, y = 0}: {x?: number; y?: number}) {
  return (
    <div
      aria-hidden
      style={{marginTop: y, marginBottom: y, marginLeft: x, marginRight: x}}
    />
  );
}
