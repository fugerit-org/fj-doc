import React from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Chip from '@mui/material/Chip';
import Link from '@mui/material/Link';
import CodeEditor from './CodeEditor';
import DOMPurify from 'isomorphic-dompurify';

const _MarkdownEditor = React.lazy(() =>
  import('@uiw/react-markdown-editor').then((m) => ({ default: m.default ?? m }))
);

/** Decode base64 (UTF-8 safe) */
function safeAtob(code) {
  try {
    return decodeURIComponent(
      atob(code)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
  } catch {
    return atob(code);
  }
}

const BINARY_FORMATS = {
  PDF: 'application/pdf',
  PDFA: 'application/pdf',
  PDFUA: 'application/pdf',
  OPENPDF: 'application/pdf',
  PDFBOX: 'application/pdf',
};

const DOWNLOAD_FORMATS = {
  XLSX: { mime: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', ext: 'xlsx' },
  OPENRTF: { mime: 'application/rtf', ext: 'rtf' },
};

const TEXT_EDITOR_FORMATS = {
  XML: 'xml',
  FO: 'xml',
  JSON: 'json',
  YAML: 'yaml',
  CSV: 'text',
  ADOC: 'text',
};

/**
 * DocOutputViewer — renders document output based on format.
 *
 * Props:
 *   format          {string}  — output format key (HTML, PDF, XLSX, MD, XML, ...)
 *   data            {string}  — base64-encoded output data
 *   generationTime  {string}  — optional generation time string
 *   message         {string}  — optional text message (for validate / errors)
 */
const DocOutputViewer = ({ format, data, generationTime, message, height = '400px' }) => {
  if (!format && !message) {
    return (
      <Box
        sx={{
          height: height,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          border: '1px dashed',
          borderColor: 'divider',
          borderRadius: 2,
          color: 'text.secondary',
        }}
      >
        <Typography variant="body2">Output will appear here</Typography>
      </Box>
    );
  }

  let content = null;

  if (format && data) {
    if (format === 'HTML') {
      const decoded = atob(data);
      content = (
        <Box
          sx={{
            border: '1px solid',
            borderColor: 'divider',
            borderRadius: 2,
            p: 2,
            height: height,
            bgcolor: 'background.paper',
            overflow: 'auto',
          }}
          contentEditable
          suppressContentEditableWarning
          // nosemgrep
          dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(decoded) }}
        />
      );
    } else if (BINARY_FORMATS[format]) {
      const src = `data:${BINARY_FORMATS[format]};base64,${data}`;
      content = (
        <Box sx={{ height: height, width: '100%' }}>
          <embed
            width="100%"
            height="100%"
            src={src}
            style={{ borderRadius: 8, border: 'none', display: 'block' }}
          />
        </Box>
      );
    } else if (DOWNLOAD_FORMATS[format]) {
      const { mime, ext } = DOWNLOAD_FORMATS[format];
      const href = `data:${mime};base64,${data}`;
      content = (
        <Box sx={{ p: 2 }}>
          <Link href={href} download={`generated_document.${ext}`} variant="button">
            ⬇ Download generated_document.{ext}
          </Link>
        </Box>
      );
    } else if (format === 'MD') {
      const decoded = safeAtob(data);
      content = (
        <React.Suspense fallback={<Typography>Loading markdown editor…</Typography>}>
          <_MarkdownEditor key={Date.now()} value={decoded} />
        </React.Suspense>
      );
    } else if (TEXT_EDITOR_FORMATS[format]) {
      const decoded = safeAtob(data);
      content = (
        <CodeEditor
          mode={TEXT_EDITOR_FORMATS[format]}
          value={decoded}
          readOnly
          name={`DOC_${format}_OUTPUT`}
          height={height}
        />
      );
    }
  }

  if (message && !content) {
    content = (
      <CodeEditor
        mode="text"
        value={message}
        readOnly
        name="DOC_MESSAGE_OUTPUT"
        height={height}
      />
    );
  }

  return (
    <Box>
      {content}
      {generationTime && (
        <Box sx={{ mt: 1, display: 'flex', alignItems: 'center', gap: 1 }}>
          <Chip label={`⏱ ${generationTime}`} size="small" variant="outlined" />
          {format && <Chip label={format} size="small" color="primary" variant="outlined" />}
        </Box>
      )}
    </Box>
  );
};

export default DocOutputViewer;
