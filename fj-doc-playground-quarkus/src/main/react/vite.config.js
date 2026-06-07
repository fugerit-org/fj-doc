import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  base: "/fj-doc-playground/home",
  optimizeDeps: {
    include: [
      'react-ace',
      'ace-builds',
      'ace-builds/src-noconflict/mode-xml',
      'ace-builds/src-noconflict/mode-json',
      'ace-builds/src-noconflict/mode-yaml',
      'ace-builds/src-noconflict/mode-kotlin',
      'ace-builds/src-noconflict/mode-ftl',
      'ace-builds/src-noconflict/mode-markdown',
      'ace-builds/src-noconflict/mode-text',
      'ace-builds/src-noconflict/mode-properties',
      'ace-builds/src-noconflict/theme-one_dark',
      'ace-builds/src-noconflict/theme-xcode',
      'ace-builds/src-noconflict/ext-language_tools',
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
