import {useEffect, useState} from "react";

export function Client({
  children,
  fallback = null,
}: {
  children?: any;
  fallback?: React.ReactNode;
}) {
  const [ready, setReady] = useState(false);
  useEffect(() => {
    setReady(true);
  }, []);
  return <>{ready ? children : fallback}</>;
}
