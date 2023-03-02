import {useRouter} from "next/router";
import {useState} from "react";
import {Toaster, toast} from "react-hot-toast";

import {isError, login} from "@/handlers";
import {useStatus} from "@/hooks/use-status";

import {Button} from "../Button";
import {EmailInput} from "../Input/EmailInput";
import {PasswordInput} from "../Input/PasswordInput";
import {Spacer} from "../Spacer";

export function LoginToAccount() {
  const [status, setStatus] = useStatus();
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const {push} = useRouter();
  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (status.type === "loading") return;
    setStatus({type: "loading", message: "Loading..."});
    const obj = {password, email};
    const res = await login(obj);

    setStatus({type: "idle"});
    if (isError(res)) {
      toast.error(res.error);
      return;
    }
    toast.success("Welcome back!");
    push("/profile");
  }

  return (
    <div className="mt-12 w-[80%]">
      <Toaster />
      <form onSubmit={handleSubmit}>
        <EmailInput
          placeholder="you@example.com"
          value={email}
          onChange={(e) => {
            setEmail(e.currentTarget.value);
          }}
        />
        <Spacer y={50} />
        <PasswordInput
          placeholder="hunter2"
          value={password}
          onChange={(e) => {
            setPassword(e.currentTarget.value);
          }}
        />
        <Spacer y={50} />
        <div className="mx-auto w-[80%]">
          <Button className="w-full mt-4 p-2">Login</Button>
        </div>
        <Spacer y={50} />
      </form>
    </div>
  );
}
