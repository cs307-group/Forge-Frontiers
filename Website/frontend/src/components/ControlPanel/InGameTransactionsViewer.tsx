import {ShopData} from "@/handlers/types";

export function InGameTransactionsViewer({shop}: {shop: ShopData[]}) {
  return (
    <table className="w-full mx-auto max-w-[1000px] table-auto border-separate rounded-lg border border-gray-200 bg-white shadow-lg">
      <thead>
        <tr className="bg-gray-100 text-xs uppercase leading-normal text-gray-600">
          <th className="px-4 py-3 text-left">Date</th>
          <th className="px-4 py-3 text-left">Item Price</th>
          <th className="px-4 py-3 text-left">Seller ID</th>
          <th className="px-4 py-3 text-left">Buyer ID</th>
        </tr>
      </thead>
      <tbody className="text-sm text-gray-600">
        {shop.map((sale) => (
          <tr
            key={sale.id_}
            className="border-b border-gray-200 hover:bg-gray-50"
          >
            <td className="px-4 py-3">
              {sale.date_sold == -1 ? "Not Sold Yet" : sale.date_sold}
            </td>
            <td className="px-4 py-3">
              {sale.item_name} x1 - {sale.price}
            </td>
            <td className="px-4 py-3">{sale.lister_player_id}</td>
            <td className="px-4 py-3">{sale.buyer_id || ""}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

// {arrayIter(shop)
//   // .filter((sale) => sale.date_sold != -1)
//   .map((sale) => (
