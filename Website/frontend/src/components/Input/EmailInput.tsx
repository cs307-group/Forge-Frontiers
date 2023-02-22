import {ProfileIcon} from "@/icons/ProfileIcon";

import {BaseInput, CustomInputProps} from "./BaseInput";

export function EmailInput(props: CustomInputProps) {
  return <BaseInput label="Your email" icon={<ProfileIcon />} {...props} />;
}
