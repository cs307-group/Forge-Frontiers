import {useState} from "react";

import {Button} from "../Button";
import {Spacer} from "../Spacer";
import {Switch} from "../Switch";

export function FeatureToggleViewer({data}: {data: string}) {
  const [sws, setSwitches] = useState(data);

  const handleSwitchChange = (name: string, val: boolean) => {
    setSwitches((prevSwitches: any) => ({...prevSwitches, [name]: val}));
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
      <form action="/api/update-features" method="post">
        <input type="hidden" value={JSON.stringify(sws)} name="swsConfig" />
        <Spacer y={30} />
        {switches}
        <Spacer y={50} />
        <Button className="p-2">Save</Button>
      </form>
    </div>
  );
}
