// Import ace core FIRST so the global is set before mode files run
import AceEditor from 'react-ace';

// Side-effect imports: must come after react-ace (which sets up global `ace`)
import 'ace-builds/src-noconflict/mode-xml';
import 'ace-builds/src-noconflict/mode-json';
import 'ace-builds/src-noconflict/mode-yaml';
import 'ace-builds/src-noconflict/mode-kotlin';
import 'ace-builds/src-noconflict/mode-ftl';
import 'ace-builds/src-noconflict/mode-markdown';
import 'ace-builds/src-noconflict/mode-text';
import 'ace-builds/src-noconflict/mode-properties';
import 'ace-builds/src-noconflict/theme-one_dark';
import 'ace-builds/src-noconflict/theme-xcode';
import 'ace-builds/src-noconflict/ext-language_tools';

/**
 * CodeEditor — shared wrapper around react-ace with sensible defaults.
 *
 * Props (all optional with defaults):
 *   mode        {string}    — ace mode, default 'xml'
 *   theme       {string}    — ace theme, default 'one_dark'
 *   value       {string}    — editor content
 *   onChange    {function}  — called on content change
 *   readOnly    {boolean}   — default false
 *   height      {string}    — CSS height, default '320px'
 *   width       {string}    — CSS width, default '100%'
 *   name        {string}    — unique editor DOM id (required by ace)
 */
const CodeEditor = ({
  mode = 'xml',
  theme = 'one_dark',
  value = '',
  onChange,
  readOnly = false,
  height = '320px',
  width = '100%',
  name = 'code-editor',
  ...rest
}) => (
  <AceEditor
    mode={mode}
    theme={theme}
    name={name}
    value={value}
    onChange={onChange}
    readOnly={readOnly}
    height={height}
    width={width}
    editorProps={{ $blockScrolling: true }}
    enableBasicAutocompletion={!readOnly}
    enableLiveAutocompletion={!readOnly}
    enableSnippets={!readOnly}
    setOptions={{ useWorker: false }}
    {...rest}
  />
);

export default CodeEditor;
