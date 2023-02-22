import {Button} from "../Button";
import {EmailInput} from "../Input/EmailInput";
import {PasswordInput} from "../Input/PasswordInput";
import {Spacer} from "../Spacer";

export function LoginToAccount() {
  function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
  }
  return (
    <div className="mt-12 w-[80%]">
      <form onSubmit={handleSubmit}>
        <EmailInput placeholder="you@example.com" />
        <Spacer y={50} />
        <PasswordInput placeholder="hunter2" />
        <Spacer y={50} />
        <div className="mx-auto w-[80%]">
          <Button className="w-full mt-4 p-2">Login</Button>
        </div>
        <Spacer y={50} />
      </form>
    </div>
  );
}
