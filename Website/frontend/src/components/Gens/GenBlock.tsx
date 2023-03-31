import {useRouter} from "next/router";
import React, { useState, useEffect } from "react";
import {Toaster, toast} from "react-hot-toast";
import Moment from "react-moment";

import {useStatus} from "@/hooks/use-status";
import ProgressBar from 'react-bootstrap/ProgressBar';

// import {Button} from "../Button";
import {Spacer} from "../Spacer";
import 'bootstrap/dist/css/bootstrap.min.css';


interface genInfo {
	type: string;
	level: number;
	rate: number;
	curr_cap; number;
	last_collected: number;
	max: number;
}

export function GenBlock(props:genInfo) {
	const [count, setCount] = React.useState(props.curr_cap);
	
	React.useEffect(() => {
		const interval = setInterval(() => {
				setCount((count) => count < props.max ? count + 1 : count)
		}
		, props.rate);
		return () => clearInterval(interval);
	}, []);

  return (
		
		<div className="bg-[#171717] rounded-md p-1 h-30">
			<Spacer y={40} />
			<p>Generator Type: {props.type} level {props.level}</p>
			<ProgressBar variant="warning" animated now={count/props.max*100}/>
			<p>Current: {`${count}/${props.max}`}</p>
			
    </div>
  );
}