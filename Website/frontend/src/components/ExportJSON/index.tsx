import {Button} from "../Button";

export function ExportJSON({
  data,
  name = "export.json",
}: {
  data: object;
  name?: string;
}) {
  return (
    <form
      action="/api/export-json"
      method="post"
      onSubmit={(e) => {
        // do this on the frontend
        e.preventDefault();
        const blob = new Blob([JSON.stringify(data)], {
          type: "application/json",
        });
        const u = URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = u;
        a.download = name;
        a.click();
        setTimeout(() => {
          URL.revokeObjectURL(u);
        }, 3000);
      }}
    >
      <noscript>
        <input type="hidden" name="data" value={JSON.stringify(data)} />
        <input type="hidden" name="file-name" value={name} />
      </noscript>
      <Button className="p-2">Export</Button>
    </form>
  );
}
