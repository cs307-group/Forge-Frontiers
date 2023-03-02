import {useState} from "react";
type Status = "idle" | "loading" | "error" | "success";

export function useStatus() {
  return useState<{type: Status; message?: string}>({
    type: "idle",
  });
}
