import { useState, useCallback, useEffect } from 'react';
import { Helmet } from 'react-helmet-async';
import Box from '@mui/material/Box';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import CircularProgress from '@mui/material/CircularProgress';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';

import CodeEditor from '../components/CodeEditor';
import DocOutputViewer from '../components/DocOutputViewer';
import FreemarkerDataEditor from '../components/FreemarkerDataEditor';
import StatusAlert from '../components/StatusAlert';
import { useCatalog } from '../hooks/useCatalog';
import { generateDocument, validateDocument } from '../api/generateApi';

const INPUT_FORMATS = [
  { value: 'FTLX', label: 'FTLX (FreeMarker)' },
  { value: 'XML',  label: 'XML' },
  { value: 'JSON', label: 'JSON' },
  { value: 'YAML', label: 'YAML' },
  { value: 'KTS',  label: 'KTS (Kotlin Script)' },
];

const OUTPUT_FORMATS = [
  { value: 'HTML',    label: 'HTML' },
  { value: 'PDF',     label: 'PDF (fop)' },
  { value: 'PDFA',    label: 'PDF/A (fop)' },
  { value: 'PDFUA',   label: 'PDF/UA-1 (fop)' },
  { value: 'XLSX',    label: 'XLSX' },
  { value: 'MD',      label: 'Markdown' },
  { value: 'XML',     label: 'Venus XML' },
  { value: 'JSON',    label: 'Venus JSON' },
  { value: 'YAML',    label: 'Venus YAML' },
  { value: 'FO',      label: 'XSL-FO' },
  { value: 'CSV',     label: 'CSV' },
  { value: 'OPENPDF', label: 'PDF (openpdf)' },
  { value: 'OPENRTF', label: 'RTF (openrtf)' },
  { value: 'PDFBOX',  label: 'PDF (pdfbox)' },
  { value: 'ADOC',    label: 'AsciiDoc' },
];

const editorModeForInput = (format) => {
  if (format === 'KTS') return 'kotlin';
  if (format === 'FTLX') return 'ftl';
  return format?.toLowerCase() ?? 'xml';
};

/* Height of the split panels: viewport minus navbar (64px) minus container
   padding (48px) minus title+toolbar+gaps (approx 120px) */
const PANEL_HEIGHT = 'calc(100vh - 230px)';
const PANEL_HEIGHT_NARROW = '450px';

const DocXmlEditor = ({ setHelpContent }) => {
  useEffect(() => { setHelpContent('doc-generator'); }, []); // eslint-disable-line

  const theme = useTheme();
  const isWide = useMediaQuery(theme.breakpoints.up('md'));

  const [inputFormat,        setInputFormat]        = useState('FTLX');
  const [outputFormat,       setOutputFormat]       = useState('HTML');

  const editorHeight = isWide ? PANEL_HEIGHT : PANEL_HEIGHT_NARROW;
  const hasExtraEditor = inputFormat === 'FTLX' || inputFormat === 'KTS';
  const outputHeight = isWide
    ? (hasExtraEditor ? 'calc(100vh - 20px)' : PANEL_HEIGHT)
    : '500px'; // taller output area on mobile/tablets for better readability

  const [docContent,         setDocContent]         = useState('');
  const [freemarkerJsonData, setFreemarkerJsonData] = useState('{"docTitle":"My FreeMarker Template Sample Doc Title"}');
  const [docOutput,          setDocOutput]          = useState(null);
  const [docFormat,          setDocFormat]          = useState(null);
  const [generationTime,     setGenerationTime]     = useState(null);
  const [outputMessage,      setOutputMessage]      = useState(null);
  const [error,              setError]              = useState(null);
  const [loading,            setLoading]            = useState(false);

  const handleEditorContent   = useCallback((c) => setDocContent(c), []);
  const handleJsonDataContent = useCallback((c) => setFreemarkerJsonData(c), []);

  const { entries, selectedId, handleSelect: handleCatalogSelect } =
    useCatalog(inputFormat, handleEditorContent, handleJsonDataContent);

  const runGenerate = async (apiCall) => {
    setError(null);
    setLoading(true);
    const payload = { inputFormat, outputFormat, docContent, freemarkerJsonData };
    const response = await apiCall(payload);
    setLoading(false);
    if (response?.success) {
      setDocOutput(response.result.docOutputBase64 ?? null);
      setDocFormat(outputFormat);
      setGenerationTime(response.result.generationTime ?? null);
      setOutputMessage(response.result.message ?? null);
    } else {
      setError(response?.result?.message || `Error ${response?.status}`);
    }
  };

  const handleGenerate = () => runGenerate(generateDocument);
  const handleValidate = () => runGenerate(validateDocument);
  const validationEnabled = inputFormat !== 'FTLX' && inputFormat !== 'KTS';

  return (
    <>
      <Helmet><title>Doc Editor — Venus Playground</title></Helmet>

      {/* Title */}
      <Typography variant="h5" fontWeight={600} sx={{ mb: 1 }}>
        Document Editor &amp; Generator
      </Typography>

      <StatusAlert message={error} onClose={() => setError(null)} />

      {/* ── CONTROL BAR: full width, single row ─────────────────────── */}
      <Box
        sx={{
          display: 'flex',
          flexWrap: 'wrap',
          alignItems: 'flex-end',
          gap: 1.5,
          p: 1.5,
          mb: 1.5,
          borderRadius: 2,
          bgcolor: 'background.paper',
          border: '1px solid',
          borderColor: 'divider',
        }}
      >
        {entries.length > 0 && (
          <FormControl size="small" sx={{ minWidth: 220, flex: '1 1 220px' }}>
            <InputLabel id="catalog-label">Sample catalog</InputLabel>
            <Select
              labelId="catalog-label"
              id="doc-catalog-select"
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

        <FormControl size="small" sx={{ minWidth: 160, flex: '0 0 160px' }}>
          <InputLabel id="input-format-label">Source type</InputLabel>
          <Select
            labelId="input-format-label"
            id="input-type-select"
            value={inputFormat}
            label="Source type"
            onChange={(e) => setInputFormat(e.target.value)}
          >
            {INPUT_FORMATS.map((f) => (
              <MenuItem key={f.value} value={f.value}>{f.label}</MenuItem>
            ))}
          </Select>
        </FormControl>

        <FormControl size="small" sx={{ minWidth: 160, flex: '0 0 160px' }}>
          <InputLabel id="output-format-label">Output format</InputLabel>
          <Select
            labelId="output-format-label"
            id="output-type-select"
            value={outputFormat}
            label="Output format"
            onChange={(e) => setOutputFormat(e.target.value)}
          >
            {OUTPUT_FORMATS.map((f) => (
              <MenuItem key={f.value} value={f.value}>{f.label}</MenuItem>
            ))}
          </Select>
        </FormControl>

        {/* Spacer pushes buttons to the right */}
        <Box sx={{ flex: '1 0 0' }} />

        <Stack direction="row" spacing={1} flexShrink={0}>
          <Button
            variant="contained"
            onClick={handleGenerate}
            disabled={loading}
            startIcon={loading ? <CircularProgress size={16} color="inherit" /> : null}
          >
            {loading ? 'Generating…' : 'Generate'}
          </Button>
          <Button
            variant="outlined"
            disabled={!validationEnabled || loading}
            onClick={handleValidate}
          >
            Validate
          </Button>
        </Stack>
      </Box>

      {/* ── SPLIT PANEL: 50% | 50% on wide, stacked on narrow ──────── */}
      <Box
        sx={{
          display: 'flex',
          flexDirection: { xs: 'column', md: 'row' },
          gap: 1,
          width: '100%',
        }}
      >
        {/* LEFT — source editor */}
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
            Source — {inputFormat}
          </Typography>
          <CodeEditor
            mode={editorModeForInput(inputFormat)}
            value={docContent}
            onChange={setDocContent}
            name="DOC_ACE_EDITOR"
            height={editorHeight}
            width="100%"
          />
          <FreemarkerDataEditor
            inputFormat={inputFormat}
            value={freemarkerJsonData}
            onChange={setFreemarkerJsonData}
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

        {/* RIGHT — output */}
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
            Output{docFormat ? ` — ${docFormat}` : ''}
          </Typography>
          <Box sx={{ minHeight: outputHeight }}>
            <DocOutputViewer
              format={docFormat}
              data={docOutput}
              generationTime={generationTime}
              message={outputMessage}
              height={outputHeight}
            />
          </Box>
        </Box>
      </Box>
    </>
  );
};

export default DocXmlEditor;
