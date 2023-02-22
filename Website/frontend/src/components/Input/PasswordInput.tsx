import {LockIcon} from "@/icons/LockIcon";

import {BaseInput, CustomInputProps} from "./BaseInput";

export function PasswordInput(props: CustomInputProps) {
  return <BaseInput label="Password" icon={<LockIcon />} {...props} />;
}
