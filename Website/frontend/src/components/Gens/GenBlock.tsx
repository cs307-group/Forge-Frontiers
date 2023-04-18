// import {useRouter} from "next/router";
// import React, { useState, useEffect } from "react";
// import {Toaster, toast} from "react-hot-toast";
// import Moment from "react-moment";

// import {useStatus} from "@/hooks/use-status";
// import ProgressBar from 'react-bootstrap/ProgressBar';

// // import {Button} from "../Button";
// import {Spacer} from "../Spacer";
// import 'bootstrap/dist/css/bootstrap.min.css';

// interface genInfo {
// 	type: string;
// 	level: number;
// 	rate: number;
// 	curr_cap; number;
// 	last_collected: number;
// 	max: number;
// }

// export function GenBlock(props:genInfo) {
// 	const [count, setCount] = React.useState(props.curr_cap);

// 	React.useEffect(() => {
// 		const interval = setInterval(() => {
// 				setCount((count) => count < props.max ? count + 1 : count)
// 		}
// 		, props.rate);
// 		return () => clearInterval(interval);
// 	}, []);

//   return (

// 		<div className="bg-[#171717] rounded-md p-1 h-30">
// 			<Spacer y={40} />
// 			<p>Generator Type: {props.type} level {props.level}</p>
// 			<ProgressBar variant="warning" animated now={count/props.max*100}/>
// 			<p>Current: {`${count}/${props.max}`}</p>

//     </div>
//   );
// }

import {useState} from "react";

import {GeneratorConfigStatic, GeneratorState} from "@/handlers/types";
import {useInterval} from "@/hooks/use-interval";

import {Client} from "../Client";
import {Spacer} from "../Spacer";

type GenProps = GeneratorState & {config: GeneratorConfigStatic["silver-gen"]};
function __GenBlock({level, config, last_collection_time}: GenProps) {
  const [count, setCount] = useState(0);
  useInterval(
    () => {
      setCount(() => {
        return Math.min(
          (+new Date() - last_collection_time) /
            config.levels[level].generation_rate,
          config.levels[level].max_size
        );
      });
    },
    count >= config.levels[level].max_size ? null : 100
  );
  return (
    <div className="h-30 rounded-md border-2 p-2  dark:border-transparent dark:bg-[#171717]">
      <Spacer y={10} />
      <p>
        Generator Type: {config.resource} level {level}
      </p>
      <Spacer y={10} />
      <Progress curr={count} max={config.levels[level].max_size} />
      <Spacer y={10} />
      {/* <ProgressBar
        variant="warning"
        animated
        now={(count / config.levels[level].max_size) * 100}
      /> */}
      <p>Current: {`${count.toFixed(2)}/${config.levels[level].max_size}`}</p>
    </div>
  );
}

function Progress({curr, max}: {curr: number; max: number}) {
  return (
    <div
      style={{"--x-scale": curr / max} as any}
      className="h-4 w-full origin-left scale-x-[var(--x-scale)] transform-gpu rounded-xl bg-[#0070f3]"
    ></div>
  );
}
export function GenBlock(gen: GenProps) {
  return (
    <Client>
      <__GenBlock {...gen} />
    </Client>
  );
}
