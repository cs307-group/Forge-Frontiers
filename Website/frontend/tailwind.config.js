/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: "class",
  content: [
    "./app/**/*.{js,ts,jsx,tsx}",
    "./pages/**/*.{js,ts,jsx,tsx}",
    "./components/**/*.{js,ts,jsx,tsx}",

    // Or if using `src` directory:
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    backgroundImage: {
      "ff-gradient": "linear-gradient(90deg, #F5A731 0%, #F4EB9B 100%)",
      "ff-gradient-shifted": "linear-gradient(90deg, #F5A731 30%, #F4EB9B 70%)",
    },
    extend: {
      colors: {
        "ff-theme": "#EDA232",
      },
    },
  },
  plugins: [],
};
