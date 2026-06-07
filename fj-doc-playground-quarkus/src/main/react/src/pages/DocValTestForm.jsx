import { useState, useEffect } from 'react';
import { Helmet } from 'react-helmet-async';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Chip from '@mui/material/Chip';
import Box from '@mui/material/Box';
import Alert from '@mui/material/Alert';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import FilePresentIcon from '@mui/icons-material/FilePresent';

import StatusAlert from '../components/StatusAlert';
import { getSupportedExtensions, checkFile } from '../api/valApi';

const DocValTestForm = ({ setHelpContent }) => {
  useEffect(() => { setHelpContent('doc-type-validator'); }, []); // eslint-disable-line

  const [fileToValidate, setFileToValidate] = useState(null);
  const [dragActive, setDragActive] = useState(false);
  const [supportedExtensions, setSupportedExtensions] = useState([]);
  const [validationResult, setValidationResult] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    getSupportedExtensions().then((r) => {
      if (r.success) setSupportedExtensions(r.result ?? []);
    });
  }, []);

  const handleDrag = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true);
    } else if (e.type === 'dragleave') {
      setDragActive(false);
    }
  };

  const handleDrop = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(false);
    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      setFileToValidate(e.dataTransfer.files[0]);
    }
  };

  const handleSend = async (e) => {
    if (e) e.preventDefault();
    if (!fileToValidate) {
      setError('Please select a file to validate.');
      return;
    }
    setError(null);
    setValidationResult(null);
    setLoading(true);
    const formData = new FormData();
    formData.append('file', fileToValidate);
    const response = await checkFile(formData);
    setLoading(false);
    if (response?.success) {
      setValidationResult(response.result?.message ?? 'Validation complete');
    } else {
      setError(`Validation failed (status ${response?.status})`);
    }
  };

  return (
    <>
      <Helmet><title>Doc Type Validator — Venus Playground</title></Helmet>

      <Typography variant="h5" fontWeight={600} gutterBottom>
        Doc Type Validator
      </Typography>

      <StatusAlert message={error} onClose={() => setError(null)} />

      {validationResult && (
        <Alert severity="success" sx={{ mb: 2.5, borderRadius: 2 }}>
          <strong>Validation result:</strong> {validationResult}
        </Alert>
      )}

      {/* ── MAIN CONTENT ── */}
      <Box
        sx={{
          display: 'flex',
          flexDirection: { xs: 'column', md: 'row' },
          gap: 4,
          width: '100%',
          mt: 1,
        }}
      >
        {/* LEFT: Upload & Validate Form */}
        <Box
          sx={{
            flex: '1 1 0',
            minWidth: 0,
            display: 'flex',
            flexDirection: 'column',
            gap: 2,
          }}
        >
          <Typography variant="body2" color="text.secondary">
            Upload a document file to validate its structure and format against supported definitions.
          </Typography>

          <Box
            component="form"
            onSubmit={handleSend}
            sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}
          >
            {/* Hidden native input */}
            <input
              id="file-input-uploader"
              type="file"
              onChange={(e) => setFileToValidate(e.target.files[0] ?? null)}
              style={{ display: 'none' }}
            />

            {/* Custom interactive dropzone label */}
            <label htmlFor="file-input-uploader" style={{ width: '100%' }}>
              <Box
                onDragEnter={handleDrag}
                onDragOver={handleDrag}
                onDragLeave={handleDrag}
                onDrop={handleDrop}
                sx={{
                  border: '2px dashed',
                  borderColor: dragActive
                    ? 'primary.main'
                    : fileToValidate
                    ? 'success.main'
                    : 'divider',
                  borderRadius: 2.5,
                  p: 4,
                  textAlign: 'center',
                  cursor: 'pointer',
                  bgcolor: dragActive ? 'action.hover' : 'background.paper',
                  transition: 'all 0.2s ease-in-out',
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: 'center',
                  gap: 1.5,
                  '&:hover': {
                    borderColor: 'primary.main',
                    bgcolor: 'action.hover',
                  },
                }}
              >
                {fileToValidate ? (
                  <FilePresentIcon sx={{ fontSize: 48, color: 'success.main' }} />
                ) : (
                  <CloudUploadIcon sx={{ fontSize: 48, color: 'text.secondary' }} />
                )}

                <Box>
                  <Typography variant="subtitle1" fontWeight={600} color="text.primary">
                    {fileToValidate ? fileToValidate.name : 'Click to upload a file'}
                  </Typography>
                  <Typography variant="caption" color="text.secondary" display="block" sx={{ mt: 0.5 }}>
                    {fileToValidate
                      ? `${(fileToValidate.size / 1024).toFixed(1)} KB`
                      : 'or drag and drop your file here'}
                  </Typography>
                </Box>
              </Box>
            </label>

            <Button
              variant="contained"
              type="submit"
              disabled={loading || !fileToValidate}
              sx={{ py: 1.2, fontWeight: 600 }}
            >
              {loading ? 'Validating…' : 'Validate file'}
            </Button>
          </Box>
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

        {/* RIGHT: Supported Extensions Catalog */}
        <Box
          sx={{
            flex: '1 1 0',
            minWidth: 0,
            display: 'flex',
            flexDirection: 'column',
            gap: 2,
          }}
        >
          {supportedExtensions.length > 0 && (
            <>
              <Typography variant="subtitle2" color="text.secondary" sx={{ fontWeight: 600 }}>
                Supported extensions
              </Typography>
              <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
                {supportedExtensions.map((ext) => (
                  <Chip
                    key={ext}
                    label={ext.toUpperCase()}
                    size="small"
                    variant="outlined"
                    sx={{ borderRadius: 1.5, fontWeight: 500 }}
                  />
                ))}
              </Box>
            </>
          )}
        </Box>
      </Box>
    </>
  );
};

export default DocValTestForm;
