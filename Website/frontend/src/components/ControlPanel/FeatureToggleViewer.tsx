import {useState} from "react";

import {Spacer} from "../Spacer";
import {Switch} from "../Switch";
import jsonData from "./status.json";

export function FeatureToggleViewer({data}: {data: string}) {
  const jsonString = JSON.stringify(data);
  const [sws, setSwitches] = useState(() => JSON.parse(jsonString));

  const handleSwitchChange = (name: string, val: boolean) => {
    setSwitches((prevSwitches: any) => ({...prevSwitches, [name]: val}));
  };

  const handleSaveClick = () => {
    console.log(sws);
  };
  const switches = Object.entries(sws).map(([feature, status]) => (
    <div key={feature}>
      <Spacer y={30} />
      <div className="flex items-center justify-between">
        <div>
          {feature} is Currently {status ? "Enabled" : "Disabled"}
        </div>
        <Switch
          name={feature}
          checked={status as any}
          onCheckedChange={(e) => handleSwitchChange(feature, e)}
        />
      </div>
    </div>
  ));

  return (
    <div className="mx-auto max-w-[90%] overflow-auto">
      <Spacer y={30} />
      {switches}
      <Spacer y={50} />
      <button onClick={handleSaveClick}>Save</button>
    </div>
  );
}
