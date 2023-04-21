import {useState} from "react";

import {Button} from "@/components/Button";
import {BaseInput} from "@/components/Input/BaseInput";
import {AppLayout, CONTROL_PANEL} from "@/components/Layout/AppLayout";
import {Spacer} from "@/components/Spacer";
import {QBlock} from "@/components/faq-questions/QBlock";
import faqData from "@/components/faq-questions/questions.json";
import {UserDataSecure} from "@/handlers/types";
import {fetchUserData} from "@/handlers/user-data";
import {userResponseToCustomData} from "@/util/user-response-to-custom-data";

export default function Faq({user}: {user?: UserDataSecure}) {
  const [q, setQ] = useState("");

  const filteredData = Object.fromEntries(
    Object.entries(faqData).filter(
      ([key, _]) => typeof key === "string" && key.includes(q)
    )
  );

  const qBlocks = Object.entries(filteredData).map(([question, answer]) => (
    <div key={question}>
      <Spacer y={20} />
      <QBlock question={question} answer={answer} />
    </div>
  ));

  return (
    <AppLayout
      active="faq"
      title="FAQ"
      extraNavItems={user?.is_admin ? CONTROL_PANEL : {}}
    >
      <Spacer y={30} />
      <div className="mx-auto w-[95%] max-w-[400px]">
        <BaseInput
          label="Question"
          name="q"
          value={q}
          onChange={(e) => {
            setQ(e.currentTarget.value);
          }}
        />
        <div className="flex items-center justify-end">
          <Button className="my-2 p-2">Search</Button>
        </div>
      </div>

      {qBlocks}
    </AppLayout>
  );
}

export const getServerSideProps = async (c: any) => {
  if (!c.req.cookies.token) return {props: {}};
  const user = await fetchUserData(c);
  return {props: {...userResponseToCustomData(user)}};
};
