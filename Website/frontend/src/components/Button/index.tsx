import clsx from "clsx";
import React from "react";

export type ButtonProps = React.DetailedHTMLProps<
  React.ButtonHTMLAttributes<HTMLButtonElement>,
  HTMLButtonElement
>;

export function Button({children, className, ...rest}: ButtonProps) {
  return (
    <button
      className={clsx(
        "bg-ff-gradient text-black text-xl rounded-3xl transition px-8 hover:scale-95",
        className
      )}
      {...rest}
    >
      {children}
    </button>
  );
}
