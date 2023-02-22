import {Button} from "../Button";
import {EmailInput} from "../Input/EmailInput";
import {NameInput} from "../Input/NameInput";
import {PasswordInput} from "../Input/PasswordInput";
import {Spacer} from "../Spacer";

export function CreateAccount() {
  function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
  }
  return (
    <div className="mt-12 w-[80%]">
      <form onSubmit={handleSubmit}>
        <NameInput placeholder="Name" />
        <Spacer y={50} />
        <EmailInput placeholder="you@example.com" />
        <Spacer y={50} />
        <PasswordInput placeholder="hunter2" />
        <Spacer y={50} />
        <div className="mx-auto w-[80%]">
          <Button className="w-full mt-4 p-2">Sign Up</Button>
        </div>
        <Spacer y={50} />
      </form>
    </div>
  );
}
