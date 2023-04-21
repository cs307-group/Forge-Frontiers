export interface TTypes {
  children?: any;
}
export function Table({children}: TTypes) {
  return (
    <table className="mx-auto w-full max-w-[1000px] table-auto border-separate rounded-lg border border-gray-200 bg-white shadow-lg">
      {children}
    </table>
  );
}

export function THead({children}: TTypes) {
  return (
    <thead>
      <tr className="bg-gray-100 text-xs uppercase leading-normal text-gray-600">
        {children}
      </tr>
    </thead>
  );
}

export function TH({children}: TTypes) {
  return <th className="px-4 py-3 text-left">{children}</th>;
}

export function TBody({children}: TTypes) {
  return <tbody className="text-sm text-gray-600">{children}</tbody>;
}

export function TR({children}: TTypes) {
  return (
    <tr className="border-b border-gray-200 hover:bg-gray-50">{children}</tr>
  );
}

export function TD({children}: TTypes) {
  return <td className="px-4 py-3">{children}</td>;
}
