import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import CodeEditor from './CodeEditor';

/**
 * FreemarkerDataEditor — JSON data editor for FreeMarker/KTS templates.
 *
 * Props:
 *   inputFormat   {string}   — 'FTLX' or 'KTS'
 *   value         {string}   — JSON string
 *   onChange      {function} — called with new value
 */
const FreemarkerDataEditor = ({ inputFormat, value, onChange }) => {
  if (inputFormat !== 'FTLX' && inputFormat !== 'KTS') return null;

  const hint =
    inputFormat === 'KTS'
      ? 'Json properties will be available in KTS as a map named \'data\'. Use attStr(data, "key"), attList, attMap or attListMap.'
      : 'Json properties will be available in FTL as variables. e.g. ${docTitle}';

  return (
    <Box sx={{ mt: 2 }}>
      <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
        {hint}
      </Typography>
      <CodeEditor
        mode="json"
        value={value}
        onChange={onChange}
        name="DOC_JSON_EDITOR"
        height="180px"
      />
    </Box>
  );
};

export default FreemarkerDataEditor;
