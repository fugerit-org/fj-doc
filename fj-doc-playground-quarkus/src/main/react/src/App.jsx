import './App.css';
import React, { useState } from 'react';
import { Helmet } from 'react-helmet-async';
import { Routes, Route } from 'react-router-dom';
import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import LinearProgress from '@mui/material/LinearProgress';

import themeList from './theme';
import AppNavbar from './components/AppNavbar';

// Lazy-load pages to reduce initial bundle
const Home = React.lazy(() => import('./pages/Home'));
const DocXmlEditor = React.lazy(() => import('./pages/DocXmlEditor'));
const DocConversion = React.lazy(() => import('./pages/DocConversion'));
const DocProjectInit = React.lazy(() => import('./pages/DocProjectInit'));
const DocValTestForm = React.lazy(() => import('./pages/DocValTestForm'));
const DocConfigConvert = React.lazy(() => import('./pages/DocConfigConvert'));

const BASE = '/fj-doc-playground/home';

function App() {
  const [currentTheme, setCurrentTheme] = useState(themeList[0].theme); // fugerit green default
  const [helpContent, setHelpContent] = useState('home');
  const [versionInfo, setVersionInfo] = useState(null);

  return (
    <ThemeProvider theme={currentTheme}>
      <CssBaseline />
      <Helmet>
        <title>Venus (fj-doc) Playground</title>
        <meta
          name="description"
          content="Generate documents in PDF, HTML, XLSX, Markdown and more from XML, JSON, YAML or FreeMarker templates."
        />
      </Helmet>

      <AppNavbar
        themes={themeList}
        currentTheme={currentTheme}
        onThemeChange={setCurrentTheme}
        helpContent={helpContent}
        versionInfo={versionInfo}
      />

      <Container maxWidth={false} sx={{ py: 3, px: { xs: 2, md: 3 }, flex: 1 }}>
        <React.Suspense
          fallback={
            <Box sx={{ width: '100%', mt: 2 }}>
              <LinearProgress />
            </Box>
          }
        >
          <Routes>
            <Route
              path={`${BASE}/doc_fun/doc_xml_editor`}
              element={<DocXmlEditor setHelpContent={setHelpContent} />}
            />
            <Route
              path={`${BASE}/doc_fun/doc_conversion`}
              element={<DocConversion setHelpContent={setHelpContent} />}
            />
            <Route
              path={`${BASE}/doc_fun/doc_conversion_x2x`}
              element={<DocConversion setHelpContent={setHelpContent} from="XML" to="XML" />}
            />
            <Route
              path={`${BASE}/doc_fun/doc_conversion_j2j`}
              element={<DocConversion setHelpContent={setHelpContent} from="JSON" to="JSON" />}
            />
            <Route
              path={`${BASE}/doc_fun/doc_conversion_y2y`}
              element={<DocConversion setHelpContent={setHelpContent} from="YAML" to="YAML" />}
            />
            <Route
              path={`${BASE}/doc_fun/doc_type_validator`}
              element={<DocValTestForm setHelpContent={setHelpContent} />}
            />
            <Route
              path={`${BASE}/doc_fun/doc_config_convert`}
              element={<DocConfigConvert setHelpContent={setHelpContent} />}
            />
            <Route
              path={`${BASE}/doc_fun/doc_project_init`}
              element={<DocProjectInit setHelpContent={setHelpContent} />}
            />
            <Route path="*" element={<Home setHelpContent={setHelpContent} setVersionInfo={setVersionInfo} />} />
          </Routes>
        </React.Suspense>
      </Container>
    </ThemeProvider>
  );
}

export default App;
