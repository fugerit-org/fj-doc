import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import IconButton from '@mui/material/IconButton';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import Tooltip from '@mui/material/Tooltip';
import Chip from '@mui/material/Chip';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import ListItemIcon from '@mui/material/ListItemIcon';
import Divider from '@mui/material/Divider';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';

// Icons
import PaletteIcon from '@mui/icons-material/Palette';
import MenuIcon from '@mui/icons-material/Menu';
import HomeIcon from '@mui/icons-material/Home';
import EditIcon from '@mui/icons-material/Edit';
import SwapHorizIcon from '@mui/icons-material/SwapHoriz';
import BuildIcon from '@mui/icons-material/Build';
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import SettingsSuggestIcon from '@mui/icons-material/SettingsSuggest';
import RocketLaunchIcon from '@mui/icons-material/RocketLaunch';

const BASE = '/fj-doc-playground/home';

const navItems = [
  { label: 'Home', path: BASE, icon: <HomeIcon fontSize="small" /> },
  { label: 'Doc Editor', path: `${BASE}/doc_fun/doc_xml_editor`, icon: <EditIcon fontSize="small" /> },
  { label: 'Conversion', path: `${BASE}/doc_fun/doc_conversion`, icon: <SwapHorizIcon fontSize="small" /> },
  {
    label: 'Tools',
    icon: <BuildIcon fontSize="small" />,
    children: [
      { label: 'Project Init', path: `${BASE}/doc_fun/doc_project_init`, icon: <RocketLaunchIcon fontSize="small" /> },
      { label: 'Type Validator', path: `${BASE}/doc_fun/doc_type_validator`, icon: <CheckCircleOutlineIcon fontSize="small" /> },
      { label: 'Config Convert', path: `${BASE}/doc_fun/doc_config_convert`, icon: <SettingsSuggestIcon fontSize="small" /> },
    ],
  },
];

const PageHelp = React.lazy(() => import('./PageHelp'));

/**
 * AppNavbar — sticky top navigation bar.
 *
 * Props:
 *   themes          {Array}    — list of { key, label, theme }
 *   currentTheme    {object}   — active MUI theme object
 *   onThemeChange   {function} — called with new theme object
 *   helpContent     {string}   — page help key
 *   versionInfo     {object}   — { version, revision }
 */
const AppNavbar = ({ themes, currentTheme, onThemeChange, helpContent, versionInfo }) => {
  const location = useLocation();
  const muiTheme = useTheme();
  const isMobile = useMediaQuery(muiTheme.breakpoints.down('md'));

  const [themeAnchor, setThemeAnchor] = useState(null);
  const [toolsAnchor, setToolsAnchor] = useState(null);
  const [drawerOpen, setDrawerOpen] = useState(false);

  // Determine active tab index from current route
  const getActiveTab = () => {
    const path = location.pathname;
    if (path.includes('doc_xml_editor')) return 1;
    if (path.includes('doc_conversion')) return 2;
    if (
      path.includes('doc_project_init') ||
      path.includes('doc_type_validator') ||
      path.includes('doc_config_convert')
    )
      return 3;
    return 0;
  };

  const handleToolsOpen = (e) => setToolsAnchor(e.currentTarget);
  const handleToolsClose = () => setToolsAnchor(null);

  return (
    <AppBar position="sticky" elevation={0} color="default">
      <Toolbar sx={{ gap: 1, minHeight: { xs: 56, sm: 64 } }}>
        {/* Logo + title */}
        <Box
          component={Link}
          to={BASE}
          sx={{ display: 'flex', alignItems: 'center', gap: 1, textDecoration: 'none', color: 'inherit', mr: 2 }}
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 500 500"
            style={{ width: 32, height: 32, borderRadius: '8px', flexShrink: 0 }}
          >
            <defs>
              <linearGradient id="logoBgGrad" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" stopColor="#00a896" />
                <stop offset="100%" stopColor="#028090" />
              </linearGradient>
              <filter id="logoGlow" x="-20%" y="-20%" width="140%" height="140%">
                <feGaussianBlur stdDeviation="8" result="blur" />
                <feComposite in="SourceGraphic" in2="blur" operator="over" />
              </filter>
            </defs>
            <rect width="500" height="500" rx="90" fill="url(#logoBgGrad)" />
            <g fill="#e0fbfc" filter="url(#logoGlow)">
              <path transform="translate(-45, 0)" d="M 210,90 C 170,90 165,130 165,160 L 165,210 C 165,235 145,245 125,250 C 145,255 165,265 165,290 L 165,340 C 165,370 170,410 210,410 L 210,375 C 185,375 195,350 195,330 L 195,285 C 195,255 165,255 155,250 C 165,245 195,245 195,215 L 195,170 C 195,150 185,125 210,125 Z" />
              <path transform="translate(45, 0)" d="M 290,90 C 330,90 335,130 335,160 L 335,210 C 335,235 355,245 375,250 C 355,255 335,265 335,290 L 335,340 C 335,370 330,410 290,410 L 290,375 C 315,375 305,350 305,330 L 305,285 C 305,255 335,255 345,250 C 335,245 305,245 305,215 L 305,170 C 305,150 315,125 290,125 Z" />
              <path d="M 195,140 L 235,340 L 265,340 L 305,140 L 270,140 L 250,290 L 230,140 Z" />
            </g>
          </svg>
          <Typography
            variant="h6"
            sx={{ fontWeight: 700, fontSize: { xs: '1rem', sm: '1.1rem' }, display: { xs: 'none', sm: 'block' } }}
          >
            Venus Playground
          </Typography>
        </Box>

        {/* Desktop navigation tabs */}
        {!isMobile && (
          <Tabs
            value={getActiveTab()}
            sx={{ flex: 1, '& .MuiTabs-indicator': { height: 3, borderRadius: '3px 3px 0 0' } }}
          >
            <Tab
              label="Home"
              component={Link}
              to={BASE}
              icon={<HomeIcon />}
              iconPosition="start"
              sx={{ minHeight: 64 }}
            />
            <Tab
              label="Doc Editor"
              component={Link}
              to={`${BASE}/doc_fun/doc_xml_editor`}
              icon={<EditIcon />}
              iconPosition="start"
              sx={{ minHeight: 64 }}
            />
            <Tab
              label="Conversion"
              component={Link}
              to={`${BASE}/doc_fun/doc_conversion`}
              icon={<SwapHorizIcon />}
              iconPosition="start"
              sx={{ minHeight: 64 }}
            />
            <Tab
              label="Tools ▾"
              onClick={handleToolsOpen}
              icon={<BuildIcon />}
              iconPosition="start"
              sx={{ minHeight: 64 }}
            />
          </Tabs>
        )}

        {/* Tools dropdown */}
        <Menu anchorEl={toolsAnchor} open={Boolean(toolsAnchor)} onClose={handleToolsClose}>
          {navItems[3].children.map((child) => (
            <MenuItem
              key={child.path}
              component={Link}
              to={child.path}
              onClick={handleToolsClose}
              sx={{ gap: 1 }}
            >
              {child.icon}
              {child.label}
            </MenuItem>
          ))}
        </Menu>

        <Box sx={{ flex: 1 }} />

        {/* Version chip */}
        {versionInfo?.version && (
          <Chip
            label={`v${versionInfo.version}`}
            size="small"
            variant="outlined"
            sx={{ display: { xs: 'none', md: 'flex' }, fontSize: '0.7rem' }}
          />
        )}

        {/* Help button */}
        {helpContent && (
          <React.Suspense fallback={null}>
            <PageHelp helpContent={helpContent} />
          </React.Suspense>
        )}

        {/* Theme selector */}
        <Tooltip title="Change theme">
          <IconButton onClick={(e) => setThemeAnchor(e.currentTarget)} size="small">
            <PaletteIcon />
          </IconButton>
        </Tooltip>
        <Menu
          anchorEl={themeAnchor}
          open={Boolean(themeAnchor)}
          onClose={() => setThemeAnchor(null)}
        >
          {themes.map((t) => (
            <MenuItem
              key={t.key}
              selected={t.theme === currentTheme}
              onClick={() => {
                onThemeChange(t.theme);
                setThemeAnchor(null);
              }}
            >
              {t.label}
            </MenuItem>
          ))}
        </Menu>

        {/* Mobile hamburger */}
        {isMobile && (
          <IconButton onClick={() => setDrawerOpen(true)} edge="end">
            <MenuIcon />
          </IconButton>
        )}
      </Toolbar>

      {/* Mobile Drawer */}
      <Drawer anchor="right" open={drawerOpen} onClose={() => setDrawerOpen(false)}>
        <Box sx={{ width: 260, pt: 2 }}>
          <Typography variant="h6" sx={{ px: 2, pb: 1, fontWeight: 700 }}>
            Venus Playground
          </Typography>
          <Divider />
          <List>
            <ListItem disablePadding>
              <ListItemButton component={Link} to={BASE} onClick={() => setDrawerOpen(false)}>
                <ListItemIcon><HomeIcon /></ListItemIcon>
                <ListItemText primary="Home" />
              </ListItemButton>
            </ListItem>
            <ListItem disablePadding>
              <ListItemButton
                component={Link}
                to={`${BASE}/doc_fun/doc_xml_editor`}
                onClick={() => setDrawerOpen(false)}
              >
                <ListItemIcon><EditIcon /></ListItemIcon>
                <ListItemText primary="Doc Editor" />
              </ListItemButton>
            </ListItem>
            <ListItem disablePadding>
              <ListItemButton
                component={Link}
                to={`${BASE}/doc_fun/doc_conversion`}
                onClick={() => setDrawerOpen(false)}
              >
                <ListItemIcon><SwapHorizIcon /></ListItemIcon>
                <ListItemText primary="Conversion" />
              </ListItemButton>
            </ListItem>
            <Divider sx={{ my: 1 }} />
            <ListItem>
              <ListItemText primary="Tools" primaryTypographyProps={{ variant: 'caption', color: 'text.secondary' }} />
            </ListItem>
            {navItems[3].children.map((child) => (
              <ListItem disablePadding key={child.path}>
                <ListItemButton
                  component={Link}
                  to={child.path}
                  onClick={() => setDrawerOpen(false)}
                >
                  <ListItemIcon>{child.icon}</ListItemIcon>
                  <ListItemText primary={child.label} />
                </ListItemButton>
              </ListItem>
            ))}
          </List>
        </Box>
      </Drawer>
    </AppBar>
  );
};

export default AppNavbar;
