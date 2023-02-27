import {Button} from "../Button";
import {BaseInput} from "../Input/BaseInput";
import {PasswordInput} from "../Input/PasswordInput";
import {Spacer} from "../Spacer";

export function LinkAccount() {
  function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
  }
  return (
    <div className="mt-12 w-[80%]">
      <form onSubmit={handleSubmit}>
        <BaseInput label="Code" placeholder="Code generated from '/ls'" />
        <Spacer y={50} />
        <div className="mx-auto w-[80%]">
          <Button className="w-full mt-4 p-2">Link Account</Button>
        </div>
        <Spacer y={50} />
      </form>
    </div>
  );
}
