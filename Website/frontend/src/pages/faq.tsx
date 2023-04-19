import {FormEvent, useState} from "react";

import {Button} from "@/components/Button";
import {AppLayout, CONTROL_PANEL} from "@/components/Layout/AppLayout";
import {useCookieSync} from "@/hooks/use-cookie-sync";
import {QBlock} from "../components/faq-questions/QBlock"
import {Spacer} from "../components/Spacer";
import faqData from "../components/faq-questions/questions.json"
import {BaseInput} from "@/components/Input/BaseInput";

import {requireAuthenticatedPageView} from "@/handlers/auth";
import {isErrorResponse} from "@/handlers/fetch-util";
import {UserData, UserDataSecure} from "@/handlers/types";
import {fetchUserData, searchUserData} from "@/handlers/user-data";
import {userResponseToCustomData} from "@/util/user-response-to-custom-data";

export default function Profile({
  data,
  cookie,
  user,
}: {
  data: UserData[];
  cookie: object;
  user?: UserDataSecure;
})
 {
  useCookieSync(cookie);
  const [q, setQ] = useState("");

	const filteredData = Object.fromEntries(
		Object.entries(faqData).filter(([key, _]) => typeof key === 'string' && key.includes(q))
	);

	const qBlocks = Object.entries(filteredData).map(([question, answer]) => {
    return (
			<div>
				<Spacer y = {20}/>
				<QBlock
					key={question}
					question={question}
					answer={answer}
				/>
			</div>
    );
	});

	return <AppLayout 
		active="faq" 
		title="FAQ"
		extraNavItems={user?.is_admin ? CONTROL_PANEL : {}}
		>	
		<Spacer y={30}/>
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
}

export const getServerSideProps = requireAuthenticatedPageView(async (c) => {
  const [searchResults, userData] = await Promise.all([
    searchUserData(c),
    fetchUserData(c),
  ]);

  if (isErrorResponse(searchResults)) {
    // hacky but our api kinda sucks
    return {
      props: {
        ...searchResults.resp,
        ...userResponseToCustomData(userData),
      },
    };
  }
  searchResults.addCustomData(userResponseToCustomData(userData));

  return searchResults.toSSPropsResult;
});
