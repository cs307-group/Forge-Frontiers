import {useId} from "react";

import * as $Switch from "@radix-ui/react-switch";

export function Switch({
  defaultValue,
  name,
}: {
  defaultValue: boolean;
  name: string;
}) {
  const sId = useId();
  return (
    <$Switch.Root
      defaultChecked={defaultValue}
      name={name}
      id={sId}
      className="g-switch-root relative h-[25px] w-[42px] rounded-full bg-[color:rgba(0,0,0,0.44)]
     shadow-[0_2px_10px_rgba(0,_0,_0,_0.14)] focus:shadow-[0_0_0_2px_black]"
    >
      <$Switch.Thumb
        className="g-switch-thumb block h-[21px] w-[21px] translate-x-0.5 rounded-full bg-[white] 
      shadow-[0_2px_10px_rgba(1,_0,_0,_0.14)] transition-transform duration-100 will-change-transform"
      ></$Switch.Thumb>
    </$Switch.Root>
  );
}
