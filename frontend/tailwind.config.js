/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class',
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'ayg': {
          'primary': '#5F8FA3',       // Azul grisáceo corporativo
          'primary-dark': '#4A7289',  // Hover/active states
          'primary-light': '#7FA5B8', // Backgrounds suaves
        },
      },
    },
  },
  plugins: [],
}
