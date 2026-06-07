import Alert from '@mui/material/Alert';
import AlertTitle from '@mui/material/AlertTitle';
import Collapse from '@mui/material/Collapse';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';

/**
 * StatusAlert — MUI Alert with dismiss capability.
 *
 * Props:
 *   message   {string|null}  — message text; null = hidden
 *   severity  {'error'|'warning'|'info'|'success'} — default 'error'
 *   title     {string}       — optional title
 *   onClose   {function}     — called when closed
 */
const StatusAlert = ({ message, severity = 'error', title, onClose }) => (
  <Collapse in={Boolean(message)}>
    <Alert
      severity={severity}
      sx={{ mb: 2, mt: 1 }}
      action={
        onClose && (
          <IconButton
            aria-label="close"
            color="inherit"
            size="small"
            onClick={onClose}
          >
            <CloseIcon fontSize="inherit" />
          </IconButton>
        )
      }
    >
      {title && <AlertTitle>{title}</AlertTitle>}
      {message}
    </Alert>
  </Collapse>
);

export default StatusAlert;
