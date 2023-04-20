import {useState} from "react";

// import { Box, Text, Flex, IconButton, useColorModeValue } from '@chakra-ui/react';
// import { ChevronUpIcon, ChevronDownIcon } from '@chakra-ui/icons';

// type QProps = {
//   question: string;
// 	answer: string;
// };

// export function QBlock({
// 	question,
// 	answer,
// }: QProps){
// 	return (
// 		<div
// 			key={question}
// 			className="flex h-20 flex-col rounded-md border-2 p-4 dark:bg-[#171717]"
// 		>
//       {question}
//       <
// 			{answer}
// 		</div>
// 	)
// };

type QProps = {
  question: string;
  answer: string;
};

export function QBlock({question, answer}: QProps) {
  const [isOpen, setIsOpen] = useState(true);

  function toggle() {
    setIsOpen(!isOpen);
  }

  return (
    <div
      key={question}
      className={
        "q-block h-30 flex flex-col rounded-md border-2 p-4 dark:bg-[#171717]"
      }
      onClick={toggle}
    >
      <h2 className="q-block__title">{question}</h2>
      <p hidden={isOpen}>{answer}</p>
    </div>
  );
}
