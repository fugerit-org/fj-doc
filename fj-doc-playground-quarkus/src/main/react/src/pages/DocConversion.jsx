import { useState, useCallback, useEffect } from 'react';
import { Helmet } from 'react-helmet-async';
import Box from '@mui/material/Box';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Stack from '@mui/material/Stack';
import CircularProgress from '@mui/material/CircularProgress';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';

import CodeEditor from '../components/CodeEditor';
import StatusAlert from '../components/StatusAlert';
import { useCatalog } from '../hooks/useCatalog';
import { convertDoc } from '../api/convertApi';

const FORMATS = ['XML', 'JSON', 'YAML'];

const DocConversion = ({ setHelpContent, from, to }) => {
  useEffect(() => { setHelpContent('doc-conversion'); }, []); // eslint-disable-line

  const theme = useTheme();
  const isWide = useMediaQuery(theme.breakpoints.up('md'));
  const editorHeight = isWide ? 'calc(100vh - 230px)' : '450px';

  const [inputFormat,  setInputFormat]  = useState(from ?? 'XML');
  const [outputFormat, setOutputFormat] = useState(to ?? 'JSON');
  const [docContent,   setDocContent]   = useState('');
  const [docOutput,    setDocOutput]    = useState('');
  const [error,        setError]        = useState(null);
  const [loading,      setLoading]      = useState(false);

  const handleEditorContent = useCallback((c) => setDocContent(c), []);

  const { entries, selectedId, handleSelect: handleCatalogSelect } =
    useCatalog(inputFormat, handleEditorContent);

  const runConvert = async (prettyPrint) => {
    setError(null);
    setLoading(true);
    const response = await convertDoc({ inputFormat, outputFormat, docContent, prettyPrint });
    setLoading(false);
    if (response?.success) {
      setDocOutput(response.result.docOutput ?? '');
    } else {
      setError(response?.result?.message || `Error ${response?.status}`);
    }
  };

  return (
    <>
      <Helmet><title>Doc Conversion — Venus Playground</title></Helmet>

      <Typography variant="h5" fontWeight={600} gutterBottom>
        Source Document Conversion
      </Typography>

      <StatusAlert message={error} onClose={() => setError(null)} />

      {/* ── TOOLBAR ─────────────────────────────────────────────────── */}
      <Box
        sx={{
          display: 'flex',
          flexWrap: 'wrap',
          gap: 1.5,
          alignItems: 'flex-end',
          mb: 2,
          p: 1.5,
          borderRadius: 2,
          bgcolor: 'background.paper',
          border: '1px solid',
          borderColor: 'divider',
        }}
      >
        {/* Catalog */}
        {entries.length > 0 && (
          <FormControl size="small" sx={{ minWidth: 240, flex: '1 1 240px' }}>
            <InputLabel id="catalog-label">Sample catalog</InputLabel>
            <Select
              labelId="catalog-label"
              value={selectedId ?? ''}
              label="Sample catalog"
              onChange={handleCatalogSelect}
            >
              {entries.map((e) => (
                <MenuItem key={e.key} value={e.key}>{e.label}</MenuItem>
              ))}
            </Select>
          </FormControl>
        )}

        {/* Convert from */}
        <FormControl size="small" sx={{ minWidth: 140, flex: '0 0 140px' }}>
          <InputLabel id="input-fmt-label">Convert from</InputLabel>
          <Select
            labelId="input-fmt-label"
            id="input-type-select"
            value={inputFormat}
            label="Convert from"
            onChange={(e) => setInputFormat(e.target.value)}
          >
            {FORMATS.map((f) => <MenuItem key={f} value={f}>{f}</MenuItem>)}
          </Select>
        </FormControl>

        {/* Convert to */}
        <FormControl size="small" sx={{ minWidth: 140, flex: '0 0 140px' }}>
          <InputLabel id="output-fmt-label">Convert to</InputLabel>
          <Select
            labelId="output-fmt-label"
            id="output-type-select"
            value={outputFormat}
            label="Convert to"
            onChange={(e) => setOutputFormat(e.target.value)}
          >
            {FORMATS.map((f) => <MenuItem key={f} value={f}>{f}</MenuItem>)}
          </Select>
        </FormControl>

        <Box sx={{ flex: '1 1 0' }} />

        <Stack direction="row" spacing={1} flexShrink={0}>
          <Button
            variant="contained"
            onClick={() => runConvert(true)}
            disabled={loading}
            startIcon={loading ? <CircularProgress size={16} color="inherit" /> : null}
            sx={{ whiteSpace: 'nowrap' }}
          >
            Convert &amp; pretty print
          </Button>
          <Button
            variant="outlined"
            onClick={() => runConvert(false)}
            disabled={loading}
            sx={{ whiteSpace: 'nowrap' }}
          >
            Only convert
          </Button>
        </Stack>
      </Box>

      {/* ── MAIN CONTENT: Input (left) | Output (right) ─────────────── */}
      <Box
        sx={{
          display: 'flex',
          flexDirection: { xs: 'column', md: 'row' },
          gap: 1.5,
          width: '100%',
        }}
      >
        {/* LEFT: input editor */}
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
            Input — {inputFormat}
          </Typography>
          <CodeEditor
            mode={inputFormat.toLowerCase()}
            value={docContent}
            onChange={setDocContent}
            name="DOC_INPUT"
            height={editorHeight}
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

        {/* RIGHT: output editor (read-only) */}
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
            Output — {outputFormat}
          </Typography>
          <CodeEditor
            mode={outputFormat.toLowerCase()}
            value={docOutput}
            readOnly
            name="DOC_OUTPUT"
            height={editorHeight}
            width="100%"
          />
        </Box>
      </Box>
    </>
  );
};

export default DocConversion;
