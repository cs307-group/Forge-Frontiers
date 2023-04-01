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
        "rounded-3xl bg-ff-gradient px-8 text-xl text-black transition hover:scale-95",
        className
      )}
      {...rest}
    >
      {children}
    </button>
  );
}
