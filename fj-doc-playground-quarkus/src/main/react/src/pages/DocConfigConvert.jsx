import { useState, useEffect } from 'react';
import { Helmet } from 'react-helmet-async';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import CircularProgress from '@mui/material/CircularProgress';
import Link from '@mui/material/Link';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';

import CodeEditor from '../components/CodeEditor';
import DocOutputViewer from '../components/DocOutputViewer';
import StatusAlert from '../components/StatusAlert';
import { getCatalogRes } from '../api/catalogApi';
import { convertConfig } from '../api/convertApi';

const DocConfigConvert = ({ setHelpContent }) => {
  useEffect(() => { setHelpContent('doc-config-convert'); }, []); // eslint-disable-line

  const theme = useTheme();
  const isWide = useMediaQuery(theme.breakpoints.up('md'));
  const editorHeightMain  = isWide ? 'calc(100vh - 440px)' : '280px';
  const editorHeightProps = isWide ? '160px' : '140px';
  const outputHeight      = isWide ? 'calc(100vh - 220px)' : '450px';

  const [docContent,        setDocContent]        = useState('');
  const [freemarkerJsonData, setFreemarkerJsonData] = useState('');
  const [docOutput,         setDocOutput]         = useState(null);
  const [outputMessage,     setOutputMessage]     = useState(null);
  const [error,             setError]             = useState(null);
  const [loading,           setLoading]           = useState(false);

  useEffect(() => {
    getCatalogRes('convert-config-stub.properties').then((r) => {
      if (r.success) setFreemarkerJsonData(r.result ?? '');
    });
    getCatalogRes('doc-process-stub.xml').then((r) => {
      if (r.success) setDocContent(r.result ?? '');
    });
  }, []);

  const handleConvert = async () => {
    if (!docContent) { setError('No input to convert.'); return; }
    setError(null);
    setLoading(true);
    const response = await convertConfig({ docContent, freemarkerJsonData });
    setLoading(false);
    if (response?.success) {
      setDocOutput(response.result.docOutputBase64 ?? null);
      setOutputMessage(response.result.message ?? null);
    } else {
      setError(response?.result?.message || `Error ${response?.status}`);
    }
  };

  return (
    <>
      <Helmet><title>Config Convert — Venus Playground</title></Helmet>

      <Typography variant="h5" fontWeight={600} gutterBottom>
        Doc Configuration Convert
      </Typography>
      <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
        Convert old fj-doc-tool configuration to the new FreeMarker format. See{' '}
        <Link href="https://github.com/fugerit-org/fj-doc/blob/main/fj-doc-tool/README.md" target="_blank" rel="noreferrer">
          fj-doc-tool README
        </Link>.
      </Typography>

      <StatusAlert message={error} onClose={() => setError(null)} />

      {/* ── TOOLBAR ─────────────────────────────────────────────────── */}
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          gap: 2,
          mb: 2,
          p: 1.5,
          borderRadius: 2,
          bgcolor: 'background.paper',
          border: '1px solid',
          borderColor: 'divider',
        }}
      >
        <Button
          variant="contained"
          onClick={handleConvert}
          disabled={loading}
          startIcon={loading ? <CircularProgress size={16} color="inherit" /> : null}
          sx={{ whiteSpace: 'nowrap' }}
        >
          {loading ? 'Converting…' : 'Convert configuration'}
        </Button>
        <Typography variant="caption" color="text.secondary">
          Edit the XML doc-process and the properties below, then click Convert.
        </Typography>
      </Box>

      {/* ── MAIN CONTENT ───────────────────────────────────────────── */}
      <Box
        sx={{
          display: 'flex',
          flexDirection: { xs: 'column', md: 'row' },
          gap: 1.5,
          width: '100%',
        }}
      >
        {/* LEFT: XML editor + properties editor */}
        <Box
          sx={{
            flex: '1 1 0',
            minWidth: 0,
            display: 'flex',
            flexDirection: 'column',
            gap: 1,
          }}
        >
          <Typography
            variant="caption"
            color="text.secondary"
            sx={{ fontWeight: 600, textTransform: 'uppercase', letterSpacing: 0.5 }}
          >
            Input XML (doc-process)
          </Typography>
          <CodeEditor
            mode="xml"
            value={docContent}
            onChange={setDocContent}
            name="DOC_ACE_EDITOR"
            height={editorHeightMain}
            width="100%"
          />

          <Typography
            variant="caption"
            color="text.secondary"
            sx={{ fontWeight: 600, textTransform: 'uppercase', letterSpacing: 0.5, mt: 1 }}
          >
            Conversion parameters (properties)
          </Typography>
          <CodeEditor
            mode="properties"
            value={freemarkerJsonData}
            onChange={setFreemarkerJsonData}
            name="DOC_JSON_EDITOR"
            height={editorHeightProps}
            width="100%"
          />
        </Box>

        {/* Divider — vertical on wide, horizontal on narrow */}
        <Box
          sx={{
            flexShrink: 0,
            bgcolor: 'divider',
            width:  { xs: '100%', md: '1px' },
            height: { xs: '1px', md: 'auto' },
          }}
        />

        {/* RIGHT: output */}
        <Box
          sx={{
            flex: '1 1 0',
            minWidth: 0,
            display: 'flex',
            flexDirection: 'column',
            gap: 1,
          }}
        >
          <Typography
            variant="caption"
            color="text.secondary"
            sx={{ fontWeight: 600, textTransform: 'uppercase', letterSpacing: 0.5 }}
          >
            Output
          </Typography>
          <Box sx={{ flex: 1 }}>
            <DocOutputViewer
              format={docOutput ? 'XML' : null}
              data={docOutput}
              message={outputMessage}
              height={outputHeight}
            />
          </Box>
        </Box>
      </Box>
    </>
  );
};

export default DocConfigConvert;
