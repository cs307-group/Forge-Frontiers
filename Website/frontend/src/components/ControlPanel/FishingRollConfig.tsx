import {FishingRoll} from "@/handlers/types";

import {Button} from "../Button";
import {BaseInput} from "../Input/BaseInput";

export function FishingRollConfig({data}: {data: FishingRoll[]}) {
  return (
    <div>
      <form action="/api/update-fishing-roll" method="post">
        <div className="mx-auto flex max-w-[800px] flex-col gap-2">
          {data
            .sort((a, b) => b.chance - a.chance)
            .map((roll) => (
              <div key={roll._id}>
                <BaseInput
                  name={roll.rarity}
                  label={"Roll chance for " + roll.rarity}
                  defaultValue={roll.chance}
                  type="number"
                />
              </div>
            ))}
          <div className="mt-4 flex items-center justify-center">
            <Button className="p-2">Save</Button>
          </div>
        </div>
      </form>
    </div>
  );
}
