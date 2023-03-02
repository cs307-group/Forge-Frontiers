import {LockIcon} from "@/icons/LockIcon";

import {BaseInput, CustomInputProps} from "./BaseInput";

export function PasswordInput(props: CustomInputProps) {
  return (
    <BaseInput
      type="password"
      label="Password"
      icon={<LockIcon />}
      {...props}
    />
  );
}
