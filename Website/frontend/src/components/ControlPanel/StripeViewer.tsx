import Stripe from "stripe";

const columns = [
  {key: "amount_total", title: "Amount"},
  {key: "currency", title: "Currency"},
  {key: "payment_method_types", title: "Payment Method Types"},
  {key: "date", title: "Date"},
];
const formatter = new Intl.DateTimeFormat(["en"], {
  dateStyle: "short",
  timeStyle: "medium",
});

export function StripeViewer({data}: {data: Stripe.Checkout.Session[]}) {
  return (
    <div className="overflow-x-auto">
      <table className="w-full table-auto border-collapse">
        <thead>
          <tr className="border-b-2 border-gray-200 text-sm font-bold uppercase">
            {columns.map((column) => (
              <th key={column.key} className="px-4 py-2 text-left">
                {column.title}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="text-sm font-medium text-white">
          {data.map((item) => (
            <tr key={item.id} className="border-b border-gray-200">
              <td className="px-4 py-2">{item.amount_total! / 100}</td>
              <td className="px-4 py-2">{item.currency}</td>
              <td className="px-4 py-2">
                {item.payment_method_types.join(", ")}
              </td>
              <td className="px-4 py-2">
                {formatter.format(item.created * 1000)}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
