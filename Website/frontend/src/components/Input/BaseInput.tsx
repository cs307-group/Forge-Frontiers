import {clsx} from "clsx";
import React, {useId} from "react";

export type CustomInputProps = Omit<
  React.DetailedHTMLProps<
    React.InputHTMLAttributes<HTMLInputElement>,
    HTMLInputElement
  >,
  "id" | "className" | "icon" | "label"
>;

export interface BaseInputProps extends CustomInputProps {
  label: string;
  hideLabel?: string;
  icon?: React.ReactNode;
}

export function BaseInput({label, hideLabel, icon, ...rest}: BaseInputProps) {
  const id = useId();
  return (
    <div className="relative flex flex-col gap-3">
      <label htmlFor={id} className={hideLabel ? "sr-only" : "text-xl"}>
        {label}
      </label>
      <span aria-hidden className="absolute top-12 ml-2">
        {icon}
      </span>
      <input
        id={id}
        className={clsx(
          "h-10 rounded-sm border-2 border-white bg-[#262C2C] outline-none",
          icon ? "pl-8" : "pl-4"
        )}
        {...rest}
      />
    </div>
  );
}
