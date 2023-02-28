import {useState} from "react";

import {Button} from "../Button";
import {EmailInput} from "../Input/EmailInput";
import {NameInput} from "../Input/NameInput";
import {PasswordInput} from "../Input/PasswordInput";
import {Spacer} from "../Spacer";

type Status = "idle" | "loading" | "error" | "success";

export function CreateAccount() {
  const [status, setStatus] = useState<{type: Status; message?: string}>({
    type: "idle",
  });

  const [name, setName] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (status.type === "loading") return;
    setStatus({type: "loading", message: "Loading..."});
    const obj = {name, password, email};
    const res = await fetch("/api/create-account", {
      body: JSON.stringify(obj),
      method: "POST",
      headers: {"content-type": "application/json"},
    });
    setStatus({type: "idle"});
  }

  return (
    <div className="mt-12 w-[80%]">
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
