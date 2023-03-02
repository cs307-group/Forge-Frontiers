import {useRouter} from "next/router";
import {useState} from "react";
import {Toaster, toast} from "react-hot-toast";

import {isError, linkAccount} from "@/handlers/runtime";

import {Button} from "../Button";
import {BaseInput} from "../Input/BaseInput";
import {Spacer} from "../Spacer";

export function LinkAccount() {
  const [code, setCode] = useState("");
  const {push} = useRouter();
  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    const resp = await linkAccount({code});
    if (isError(resp)) {
      return toast.error(resp.error);
    }
    push("/profile");
  }
  return (
    <div className="mt-12 w-[80%]">
      <Toaster />
      <form onSubmit={handleSubmit}>
        <BaseInput
          onInput={(e) => setCode(e.currentTarget.value)}
          label="Code"
          placeholder="Code generated from '/ls'"
        />
        <Spacer y={50} />
        <div className="mx-auto w-[80%]">
          <Button className="w-full mt-4 p-2">Link Account</Button>
        </div>
        <Spacer y={50} />
      </form>
    </div>
  );
}
