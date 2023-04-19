import {AnimatePresence, motion} from "framer-motion";
import Link from "next/link";
import {useState} from "react";
import jsonData from "./status.json"

import {Spacer} from "../Spacer";
import {Switch} from "../Switch";

type FeaturesData = {
  features: string;
};

//export function FeatureToggleViewer({jsonString}: {jsonString: FeaturesData}) {
export function FeatureToggleViewer() {

  const jsonString = JSON.stringify(jsonData);
  const [sws, setSwitches] = useState(() => JSON.parse(jsonString));

  // const handleSwitchChange = (name: string) => {
  //   setSwitches((prevSwitches: any) => ({ ...prevSwitches, [name]: event.target.checked }));
  // };
  const handleSwitchChange = (name: string, event:  React.FormEvent<HTMLButtonElement>) => {
    const val = event.currentTarget.dataset.state == "checked" ? false : true
    setSwitches((prevSwitches: any) => ({ ...prevSwitches, [name]: val}));
  };
  
  const handleSaveClick = () => {
    console.log(sws);
  };
  const switches = Object.entries(sws).map(([feature, status]) => (
    <div key={feature}>
      <Spacer y={20} />
      <div className="flex items-center justify-between">
      <div>{feature} is Currently {status ? "Enabled"  : "Disabled"}</div>
        <Switch
          name={feature}
          defaultValue={status ? true  : false}
          handler = {handleSwitchChange}
        />
      </div>
    </div>
  ));

  return (
    <div className="mx-auto max-w-[90%] overflow-auto">
      <Spacer y={30} />
      {switches}
      <button onClick={handleSaveClick}>Save</button>
    </div>
  );
}
