import { useState, useEffect } from 'react';
import Popover from '@mui/material/Popover';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import Tooltip from '@mui/material/Tooltip';
import HelpOutlineIcon from '@mui/icons-material/HelpOutline';
import DOMPurify from 'isomorphic-dompurify';

/**
 * PageHelp — contextual help button that shows a popover with HTML content.
 *
 * Props:
 *   helpContent {string} — page key used to fetch /help/<key>.html
 */
const PageHelp = ({ helpContent }) => {
  const [anchorEl, setAnchorEl] = useState(null);
  const [helpText, setHelpText] = useState('');

  useEffect(() => {
    if (!helpContent) return;
    fetch(`/fj-doc-playground/home/help/${helpContent}.html`)
      .then((r) => r.text())
      .then(setHelpText)
      .catch(() => setHelpText('<p>Help content not available.</p>'));
  }, [helpContent]);

  const open = Boolean(anchorEl);

  return (
    <>
      <Tooltip title="Help">
        <IconButton
          aria-describedby={open ? 'help-popover' : undefined}
          onClick={(e) => setAnchorEl(e.currentTarget)}
          size="small"
        >
          <HelpOutlineIcon />
        </IconButton>
      </Tooltip>
      <Popover
        id="help-popover"
        open={open}
        anchorEl={anchorEl}
        onClose={() => setAnchorEl(null)}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
        transformOrigin={{ vertical: 'top', horizontal: 'right' }}
        PaperProps={{ sx: { maxWidth: 480, maxHeight: 400, overflow: 'auto', p: 2 } }}
      >
        {/* eslint-disable-next-line react/no-danger */}
        <Typography component="div" dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(helpText) }} />
      </Popover>
    </>
  );
};

export default PageHelp;
