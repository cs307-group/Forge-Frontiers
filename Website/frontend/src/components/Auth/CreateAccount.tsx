import {useRouter} from "next/router";
import {useState} from "react";
import {Toaster, toast} from "react-hot-toast";

import {createAccount, isError, login} from "@/handlers/runtime";
import {useStatus} from "@/hooks/use-status";

import {Button} from "../Button";
import {EmailInput} from "../Input/EmailInput";
import {NameInput} from "../Input/NameInput";
import {PasswordInput} from "../Input/PasswordInput";
import {Spacer} from "../Spacer";

export function CreateAccount() {
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const {push} = useRouter();
  const [status, setStatus] = useStatus();
  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (status.type === "loading") return;
    setStatus({type: "loading", message: "Loading..."});
    const obj = {name, password, email};
    const res = await createAccount(obj);
    setStatus({type: "idle"});
    if (isError(res)) {
      toast.error(res.error);
      return;
    }
    toast.success("Account Successfully Created!");
    await login(obj);
    push("/profile?new");
  }

  return (
    <div className="mt-12 w-[80%]">
      <Toaster />
      <form onSubmit={handleSubmit}>
        <NameInput
          placeholder="Name"
          required
          value={name}
          maxLength={50}
          onChange={(e) => setName(e.currentTarget.value)}
        />
        <Spacer y={50} />
        <EmailInput
          placeholder="you@example.com"
          required
          type="email"
          value={email}
          onChange={(e) => setEmail(e.currentTarget.value)}
        />
        <Spacer y={50} />
        <PasswordInput
          type="password"
          placeholder="hunter2"
          value={password}
          required
          minLength={3}
          onChange={(e) => setPassword(e.currentTarget.value)}
        />
        <Spacer y={50} />
        <div className="mx-auto w-[80%]">
          <Button className="w-full mt-4 p-2">Sign Up</Button>
        </div>
        <Spacer y={50} />
        <div>{status.message}</div>
      </form>
    </div>
  );
}
