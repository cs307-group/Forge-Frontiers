import {ProfileIcon} from "@/icons/ProfileIcon";

import {BaseInput, CustomInputProps} from "./BaseInput";

export function NameInput(props: CustomInputProps) {
  return <BaseInput label="Your name" icon={<ProfileIcon />} {...props} />;
}
