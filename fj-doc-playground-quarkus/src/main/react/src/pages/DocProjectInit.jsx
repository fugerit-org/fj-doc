import { useState, useEffect } from 'react';
import { Helmet } from 'react-helmet-async';
import Grid from '@mui/material/Grid';
import TextField from '@mui/material/TextField';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import CircularProgress from '@mui/material/CircularProgress';
import Switch from '@mui/material/Switch';
import FormControlLabel from '@mui/material/FormControlLabel';
import Alert from '@mui/material/Alert';
import Stepper from '@mui/material/Stepper';
import Step from '@mui/material/Step';
import StepLabel from '@mui/material/StepLabel';
import Link from '@mui/material/Link';
import Chip from '@mui/material/Chip';
import OutlinedInput from '@mui/material/OutlinedInput';
import Checkbox from '@mui/material/Checkbox';
import ListItemText from '@mui/material/ListItemText';

import StatusAlert from '../components/StatusAlert';
import { getExtensionsList, initProject } from '../api/projectApi';
import { getAvailableVersions } from '../api/metaApi';

const JAVA_VERSIONS = [8, 11, 17, 21];

const FLAVOURS = [
  { value: 'vanilla', label: 'Vanilla (Simple library project)' },
  { value: 'quarkus-3', label: 'Quarkus 3 — Maven, YAML config (Recommended)' },
  { value: 'quarkus-3-gradle', label: 'Quarkus 3 — Gradle Groovy' },
  { value: 'quarkus-3-gradle-kts', label: 'Quarkus 3 — Gradle Kotlin' },
  { value: 'quarkus-3-properties', label: 'Quarkus 3 — Maven, Properties config' },
  { value: 'quarkus-2', label: 'Quarkus 2 (legacy)' },
  { value: 'micronaut-4', label: 'Micronaut 4' },
  { value: 'springboot-3', label: 'SpringBoot 3' },
  { value: 'openliberty', label: 'OpenLiberty' },
  { value: 'direct', label: 'Direct (Doc Maven Plugin, direct goal)' },
];

const CI_OPTIONS = [
  { value: '', label: 'None' },
  { value: 'github', label: 'GitHub Actions' },
];

const STEPS = ['Maven Coordinates', 'Runtime & Version', 'Extensions', 'Review & Generate'];

const DocProjectInit = ({ setHelpContent }) => {
  useEffect(() => { setHelpContent('doc-project-init'); }, []); // eslint-disable-line

  const [activeStep, setActiveStep] = useState(0);

  // Form state
  const [groupId, setGroupId] = useState('org.fugerit.java.demo');
  const [artifactId, setArtifactId] = useState('fugerit-doc-demo');
  const [projectVersion, setProjectVersion] = useState('1.0.0-SNAPSHOT');
  const [javaVersion, setJavaVersion] = useState(21);
  const [venusVersion, setVenusVersion] = useState('');
  const [availableVersions, setAvailableVersions] = useState([]);
  const [flavour, setFlavour] = useState('vanilla');
  const [flavourVersion, setFlavourVersion] = useState('');
  const [withCI, setWithCI] = useState('');
  const [addVerifyPlugin, setAddVerifyPlugin] = useState(true);
  const [addDirectPlugin, setAddDirectPlugin] = useState(false);
  const [addJacoco, setAddJacoco] = useState(false);
  const [addFormatting, setAddFormatting] = useState(false);
  const [extensionList, setExtensionList] = useState([]);
  const [selectedExtensions, setSelectedExtensions] = useState(['fj-doc-base', 'fj-doc-freemarker']);
  const [loadingExts, setLoadingExts] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [serverMessage, setServerMessage] = useState('');
  const [error, setError] = useState(null);

  useEffect(() => {
    // Load extensions and available versions in parallel
    Promise.all([getExtensionsList(), getAvailableVersions()]).then(([extsResp, versResp]) => {
      if (extsResp.success) setExtensionList(extsResp.result ?? []);
      if (versResp.success && versResp.result?.length > 0) {
        setAvailableVersions(versResp.result);
        setVenusVersion(versResp.result[0]);
      }
      setLoadingExts(false);
    });
  }, []);

  const handleSubmit = async () => {
    setIsSubmitting(true);
    setError(null);
    setServerMessage('');
    const payload = {
      groupId, artifactId, projectVersion,
      javaVersion, venusVersion, flavour, flavourVersion,
      addVerifyPlugin, addDirectPlugin, addJacoco, addFormatting, withCI,
      extensionList: selectedExtensions,
    };
    const response = await initProject(payload);
    setIsSubmitting(false);
    if (response?.success) {
      const result = response.result;
      if (result.message) setServerMessage(result.message);
      if (result.content) {
        const link = document.createElement('a');
        link.href = `data:application/zip;base64,${result.content}`;
        link.download = `${artifactId}.zip`;
        link.click();
      }
    } else {
      setError(response?.result?.message || `Error ${response?.status}`);
    }
  };

  const stepContent = [
    // Step 0: Maven coordinates
    <Grid container spacing={2} key="step0">
      <Grid size={12}>
        <TextField label="Group ID" fullWidth value={groupId} onChange={(e) => setGroupId(e.target.value)}
          helperText="e.g. org.fugerit.java.demo" />
      </Grid>
      <Grid size={12}>
        <TextField label="Artifact ID" fullWidth value={artifactId} onChange={(e) => setArtifactId(e.target.value)}
          helperText="e.g. my-doc-project" />
      </Grid>
      <Grid size={12}>
        <TextField label="Project Version" fullWidth value={projectVersion}
          onChange={(e) => setProjectVersion(e.target.value)} helperText="e.g. 1.0.0-SNAPSHOT" />
      </Grid>
    </Grid>,

    // Step 1: Runtime & versions
    <Grid container spacing={2} key="step1">
      <Grid size={{ xs: 12, sm: 6 }}>
        <FormControl fullWidth>
          <InputLabel id="java-ver-label">Java Version</InputLabel>
          <Select labelId="java-ver-label" id="java-version-select" value={javaVersion}
            label="Java Version" onChange={(e) => setJavaVersion(e.target.value)}>
            {JAVA_VERSIONS.map((v) => <MenuItem key={v} value={v}>{v}</MenuItem>)}
          </Select>
        </FormControl>
      </Grid>
      <Grid size={{ xs: 12, sm: 6 }}>
        <FormControl fullWidth>
          <InputLabel id="venus-ver-label">Venus Version</InputLabel>
          <Select labelId="venus-ver-label" id="venus-version-select" value={venusVersion}
            label="Venus Version" onChange={(e) => setVenusVersion(e.target.value)}>
            {availableVersions.map((v) => <MenuItem key={v} value={v}>{v}</MenuItem>)}
          </Select>
        </FormControl>
      </Grid>
      <Grid size={{ xs: 12, sm: 6 }}>
        <FormControl fullWidth>
          <InputLabel id="flavour-label">Flavour</InputLabel>
          <Select labelId="flavour-label" id="venus-flavour-select" value={flavour}
            label="Flavour" onChange={(e) => setFlavour(e.target.value)}>
            {FLAVOURS.map((f) => <MenuItem key={f.value} value={f.value}>{f.label}</MenuItem>)}
          </Select>
        </FormControl>
      </Grid>
      <Grid size={{ xs: 12, sm: 6 }}>
        <TextField label="Flavour version (leave blank for default)" fullWidth value={flavourVersion}
          onChange={(e) => setFlavourVersion(e.target.value)} />
      </Grid>
      <Grid size={{ xs: 12, sm: 6 }}>
        <FormControl fullWidth>
          <InputLabel id="ci-label">CI</InputLabel>
          <Select labelId="ci-label" id="venus-ci-select" value={withCI}
            label="CI" onChange={(e) => setWithCI(e.target.value)}>
            {CI_OPTIONS.map((c) => <MenuItem key={c.value} value={c.value}>{c.label}</MenuItem>)}
          </Select>
        </FormControl>
      </Grid>
      <Grid size={{ xs: 12, sm: 6 }}>
        <Box sx={{ display: 'flex', flexDirection: 'column' }}>
          <FormControlLabel control={<Switch checked={addVerifyPlugin} onChange={(e) => setAddVerifyPlugin(e.target.checked)} />}
            label="Add verify plugin" />
          <FormControlLabel control={<Switch checked={addDirectPlugin} onChange={(e) => setAddDirectPlugin(e.target.checked)} />}
            label="Add direct plugin" />
          <FormControlLabel control={<Switch checked={addJacoco} onChange={(e) => setAddJacoco(e.target.checked)} />}
            label="Add JaCoCo" />
          <FormControlLabel control={<Switch checked={addFormatting} onChange={(e) => setAddFormatting(e.target.checked)} />}
            label="Add formatting" />
        </Box>
      </Grid>
    </Grid>,

    // Step 2: Extensions
    <Box key="step2">
      {loadingExts ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', p: 4 }}>
          <CircularProgress />
        </Box>
      ) : (
        <>
          <FormControl fullWidth>
            <InputLabel id="ext-label">Extensions (*)</InputLabel>
            <Select
              labelId="ext-label"
              id="extension-multi-select"
              multiple
              value={selectedExtensions}
              onChange={(e) => setSelectedExtensions(e.target.value)}
              input={<OutlinedInput label="Extensions (*)" />}
              renderValue={(selected) => (
                <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                  {selected.map((v) => <Chip key={v} label={v} size="small" />)}
                </Box>
              )}
            >
              {extensionList.map((ext) => (
                <MenuItem key={ext.key} value={ext.key}>
                  <Checkbox checked={selectedExtensions.includes(ext.key)} />
                  <ListItemText primary={ext.label} />
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <Typography variant="caption" color="text.secondary" sx={{ mt: 1, display: 'block' }}>
            (*) See the{' '}
            <Link href="https://venusdocs.fugerit.org/guide/#doc-handler-module-handlers" target="_blank" rel="noreferrer">
              guide
            </Link>{' '}
            for supported formats per extension.
          </Typography>
        </>
      )}
    </Box>,

    // Step 3: Review
    <Grid container spacing={1} key="step3">
      {[
        ['Group ID', groupId], ['Artifact ID', artifactId], ['Version', projectVersion],
        ['Java', javaVersion], ['Venus', venusVersion], ['Flavour', flavour],
        ['CI', withCI || 'None'], ['Verify plugin', String(addVerifyPlugin)],
        ['Direct plugin', String(addDirectPlugin)], ['JaCoCo', String(addJacoco)],
        ['Formatting', String(addFormatting)],
      ].map(([k, v]) => (
        <Grid size={{ xs: 6, sm: 4 }} key={k}>
          <Typography variant="caption" color="text.secondary">{k}</Typography>
          <Typography variant="body2" fontWeight={500}>{v}</Typography>
        </Grid>
      ))}
      <Grid size={12}>
        <Typography variant="caption" color="text.secondary">Extensions</Typography>
        <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5, mt: 0.5 }}>
          {selectedExtensions.map((e) => <Chip key={e} label={e} size="small" variant="outlined" />)}
        </Box>
      </Grid>
    </Grid>,
  ];

  return (
    <>
      <Helmet><title>Project Init — Venus Playground</title></Helmet>

      <Typography variant="h5" fontWeight={600} gutterBottom>
        Fugerit Venus Doc Project Initialization
      </Typography>

      <StatusAlert message={error} onClose={() => setError(null)} />

      <Stepper activeStep={activeStep} sx={{ mb: 4 }}>
        {STEPS.map((label) => (
          <Step key={label}><StepLabel>{label}</StepLabel></Step>
        ))}
      </Stepper>

      <Box sx={{ minHeight: 300, mb: 3 }}>
        {stepContent[activeStep]}
      </Box>

      {serverMessage && (
        <Alert severity="success" sx={{ mb: 2 }}>{serverMessage}</Alert>
      )}

      <Box sx={{ display: 'flex', gap: 2 }}>
        {activeStep > 0 && (
          <Button variant="outlined" onClick={() => setActiveStep((s) => s - 1)}>
            Back
          </Button>
        )}
        {activeStep < STEPS.length - 1 ? (
          <Button variant="contained" onClick={() => setActiveStep((s) => s + 1)}>
            Next
          </Button>
        ) : (
          <Button
            variant="contained"
            color="primary"
            onClick={handleSubmit}
            disabled={isSubmitting}
            startIcon={isSubmitting ? <CircularProgress size={16} color="inherit" /> : null}
          >
            {isSubmitting ? 'Creating project…' : 'Create Project'}
          </Button>
        )}
      </Box>
    </>
  );
};

export default DocProjectInit;
