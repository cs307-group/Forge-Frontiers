import {Raleway} from "@next/font/google";

const raleway = Raleway({preload: false})

export default function Login() {
  return (
    <>
      <div className="global_background full_page">
        <div className="login_super_content vertical_center">
          <p style={raleway.style} className="login_title">Hello World</p>
        </div>
      </div>
    </>
  );
}
