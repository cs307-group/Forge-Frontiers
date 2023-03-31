import {useRouter} from "next/router";
import {useState} from "react";
import {Toaster, toast} from "react-hot-toast";

import {useStatus} from "@/hooks/use-status";

// import {Button} from "../Button";
import {Spacer} from "../Spacer";
import genDataJSON from './generators.json'

interface genInfo {
	type: string;
	level: number;
	rate: number;
	last_collected: number;
}

export function GenBlock(props:genInfo) {
	console.log(props);
	console.log(genDataJSON.conversion);
  return (
    <div>
    </div>
  );
}