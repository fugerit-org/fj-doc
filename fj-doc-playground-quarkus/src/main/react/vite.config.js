import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  base:"/fj-doc-playground/home",
  optimizeDeps: {
    include: [
      'react-ace',
      '@uiw/react-markdown-editor',
    ],
  },
  server: {
    open: true,
    port: 3000,
    proxy: {
      "/fj-doc-playground/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        secure: false,
      }
    }
  },
})
