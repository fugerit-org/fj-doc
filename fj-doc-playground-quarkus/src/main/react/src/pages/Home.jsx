import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Helmet } from 'react-helmet-async';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import Card from '@mui/material/Card';
import CardActionArea from '@mui/material/CardActionArea';
import CardContent from '@mui/material/CardContent';
import TextField from '@mui/material/TextField';
import IconButton from '@mui/material/IconButton';
import Tooltip from '@mui/material/Tooltip';
import Chip from '@mui/material/Chip';
import Divider from '@mui/material/Divider';
import { CopyToClipboard } from 'react-copy-to-clipboard';

import EditIcon from '@mui/icons-material/Edit';
import SwapHorizIcon from '@mui/icons-material/SwapHoriz';
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import SettingsSuggestIcon from '@mui/icons-material/SettingsSuggest';
import RocketLaunchIcon from '@mui/icons-material/RocketLaunch';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';
import OpenInNewIcon from '@mui/icons-material/OpenInNew';

import { getVersion, getInfo } from '../api/metaApi';

const BASE = '/fj-doc-playground/home';

const dockerCmd =
  'docker run -it -p 8080:8080 --name fj-doc-playground-quarkus fugeritorg/fj-doc-playground-quarkus:snapshot';

const features = [
  {
    icon: <EditIcon sx={{ fontSize: 32 }} />,
    title: 'Doc Editor & Generator',
    description:
      'Edit FTL, XML, JSON or YAML documents and generate output in HTML, PDF, XLSX, Markdown and more.',
    path: `${BASE}/doc_fun/doc_xml_editor`,
    color: 'primary',
  },
  {
    icon: <SwapHorizIcon sx={{ fontSize: 32 }} />,
    title: 'Source Conversion',
    description:
      'Convert Venus Doc source documents between XML, JSON and YAML formats with pretty-printing.',
    path: `${BASE}/doc_fun/doc_conversion`,
    color: 'secondary',
  },
  {
    icon: <CheckCircleOutlineIcon sx={{ fontSize: 32 }} />,
    title: 'Doc Type Validator',
    description: 'Upload and validate documents in all supported formats.',
    path: `${BASE}/doc_fun/doc_type_validator`,
    color: 'success',
  },
  {
    icon: <SettingsSuggestIcon sx={{ fontSize: 32 }} />,
    title: 'Config Convert',
    description: 'Convert old fj-doc-tool configuration to the new FreeMarker format.',
    path: `${BASE}/doc_fun/doc_config_convert`,
    color: 'warning',
  },
  {
    icon: <RocketLaunchIcon sx={{ fontSize: 32 }} />,
    title: 'Project Init',
    description:
      'Bootstrap a new Venus (fj-doc) project with Maven/Gradle, Quarkus/Spring Boot and extensions.',
    path: `${BASE}/doc_fun/doc_project_init`,
    color: 'info',
  },
];

const Home = ({ setHelpContent, setVersionInfo }) => {
  useEffect(() => {
    setHelpContent('home');
    // Load version info for navbar
    getVersion().then((r) => { if (r.success) setVersionInfo?.(r.result); });
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const [metaInfo, setMetaInfo] = React.useState(null);
  useEffect(() => {
    getInfo().then((r) => { if (r.success) setMetaInfo(r.result); });
  }, []);

  return (
    <>
      <Helmet>
        <title>Home — Venus (fj-doc) Playground</title>
      </Helmet>

      {/* Hero section */}
      <Box sx={{ mb: 5, textAlign: 'center' }}>
        <Typography variant="h3" component="h1" fontWeight={700} gutterBottom>
          Venus{' '}
          <Box component="span" color="primary.main">
            (fj-doc)
          </Box>{' '}
          Playground
        </Typography>
        <Typography variant="h6" color="text.secondary" sx={{ maxWidth: 640, mx: 'auto' }}>
          Generate documents in PDF, HTML, XLSX, Markdown and more — starting from XML, JSON, YAML or
          FreeMarker templates.
        </Typography>
      </Box>

      {/* Feature cards */}
      <Grid container spacing={3} sx={{ mb: 5 }}>
        {features.map((f) => (
          <Grid size={{ xs: 12, sm: 6, md: 4 }} key={f.title}>
            <Card
              sx={{
                height: '100%',
                transition: 'transform 0.2s, box-shadow 0.2s',
                '&:hover': { transform: 'translateY(-4px)', boxShadow: 6 },
              }}
            >
              <CardActionArea component={Link} to={f.path} sx={{ height: '100%', p: 1 }}>
                <CardContent>
                  <Box color={`${f.color}.main`} sx={{ mb: 1.5 }}>
                    {f.icon}
                  </Box>
                  <Typography variant="h6" fontWeight={600} gutterBottom>
                    {f.title}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {f.description}
                  </Typography>
                </CardContent>
              </CardActionArea>
            </Card>
          </Grid>
        ))}
      </Grid>

      <Divider sx={{ mb: 4 }} />

      {/* Quick start section */}
      <Box sx={{ mb: 4 }}>
        <Typography variant="h5" fontWeight={600} gutterBottom>
          Run locally
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
          Use the public Docker image or{' '}
          <Link
            href="https://github.com/fugerit-org/fj-doc/tree/main/fj-doc-playground-quarkus"
            target="_blank"
            rel="noreferrer"
          >
            build from source
          </Link>
          .
        </Typography>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <TextField
            id="docker-cmd"
            value={dockerCmd}
            size="small"
            slotProps={{ input: { readOnly: true } }}
            sx={{ flex: 1, fontFamily: 'monospace' }}
          />
          <CopyToClipboard text={dockerCmd}>
            <Tooltip title="Copy command">
              <IconButton size="small">
                <ContentCopyIcon fontSize="small" />
              </IconButton>
            </Tooltip>
          </CopyToClipboard>
        </Box>
        <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
          Then open{' '}
          <Link href="http://localhost:8080/fj-doc-playground/home/" target="_blank" rel="noreferrer">
            http://localhost:8080/fj-doc-playground/home/
          </Link>
          .{' '}
          See{' '}
          <Link
            href="https://hub.docker.com/repository/docker/fugeritorg/fj-doc-playground-quarkus/general"
            target="_blank"
            rel="noreferrer"
          >
            Docker Hub
          </Link>{' '}
          for all available tags.
        </Typography>
      </Box>

      {/* Docs links */}
      <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap', mb: 4 }}>
        <Chip
          icon={<PictureAsPdfIcon />}
          label="Playground Guide (PDF)"
          component="a"
          href="/fj-doc-playground/home/help/fj-doc-playground-quarkus.pdf"
          clickable
          variant="outlined"
        />
        <Chip
          icon={<OpenInNewIcon />}
          label="Venus Docs"
          component="a"
          href="https://venusdocs.fugerit.org/guide/"
          target="_blank"
          rel="noreferrer"
          clickable
          variant="outlined"
        />
        <Chip
          icon={<OpenInNewIcon />}
          label="Doc Format Summary"
          component="a"
          href="https://venusdocs.fugerit.org/guide/#doc-format-entry-point"
          target="_blank"
          rel="noreferrer"
          clickable
          variant="outlined"
        />
      </Box>

      {/* Meta info */}
      {metaInfo && (
        <Box sx={{ mt: 2 }}>
          <Typography variant="caption" color="text.secondary">
            {Object.entries(metaInfo)
              .map(([k, v]) => `${k}=${v}`)
              .join(' · ')}
          </Typography>
        </Box>
      )}
    </>
  );
};

export default Home;
