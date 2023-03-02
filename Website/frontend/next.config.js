/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    remotePatterns: [
      {
        hostname: "visage.surgeplay.com",
      },
    ],
  },
  reactStrictMode: true,
};

module.exports = nextConfig;
