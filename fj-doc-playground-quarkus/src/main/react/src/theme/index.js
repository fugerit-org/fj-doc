import { createTheme } from '@mui/material/styles';
import { tokens } from './tokens';

const makeTheme = (colors, mode = 'dark') =>
  createTheme({
    palette: {
      mode,
      primary: {
        main: colors.primary,
        dark: colors.primaryDark,
        light: colors.primaryLight,
      },
      secondary: {
        main: colors.secondary,
      },
      background: {
        default: colors.background,
        paper: colors.paper,
      },
      divider: colors.border,
      text: {
        primary: colors.text,
        secondary: colors.textSecondary,
      },
    },
    typography: {
      fontFamily: tokens.typography.fontFamily,
      fontWeightLight: tokens.typography.fontWeightLight,
      fontWeightRegular: tokens.typography.fontWeightRegular,
      fontWeightMedium: tokens.typography.fontWeightMedium,
      fontWeightBold: tokens.typography.fontWeightBold,
      h1: { fontSize: '2rem', fontWeight: 700 },
      h2: { fontSize: '1.5rem', fontWeight: 600 },
      h3: { fontSize: '1.25rem', fontWeight: 600 },
      h5: { fontWeight: 600 },
      h6: { fontWeight: 600 },
    },
    shape: {
      borderRadius: tokens.shape.borderRadius,
    },
    components: {
      MuiButton: {
        styleOverrides: {
          root: {
            textTransform: 'none',
            fontWeight: 600,
            borderRadius: '8px',
          },
        },
      },
      MuiCard: {
        styleOverrides: {
          root: {
            backgroundImage: 'none',
            border: `1px solid ${colors.border}`,
          },
        },
      },
      MuiPaper: {
        styleOverrides: {
          root: {
            backgroundImage: 'none',
          },
        },
      },
      MuiAppBar: {
        styleOverrides: {
          root: {
            backgroundImage: 'none',
            backgroundColor: colors.paper,
            borderBottom: `1px solid ${colors.border}`,
          },
        },
      },
      MuiTab: {
        styleOverrides: {
          root: {
            textTransform: 'none',
            fontWeight: 500,
            fontSize: '0.9rem',
          },
        },
      },
      MuiSelect: {
        styleOverrides: {
          root: {
            borderRadius: '8px',
          },
        },
      },
      MuiTextField: {
        styleOverrides: {
          root: {
            '& .MuiOutlinedInput-root': {
              borderRadius: '8px',
            },
          },
        },
      },
      MuiChip: {
        styleOverrides: {
          root: {
            fontWeight: 500,
          },
        },
      },
    },
  });

const themeList = [
  {
    key: 'fugerit',
    label: '🟢 Fugerit Green (default)',
    theme: makeTheme(tokens.colors.fugerit, 'dark'),
  },
  {
    key: 'tech-blue',
    label: '🔵 Tech Blue',
    theme: makeTheme(tokens.colors.techBlue, 'dark'),
  },
  {
    key: 'professional',
    label: '🔷 Professional',
    theme: makeTheme(tokens.colors.professional, 'dark'),
  },
  {
    key: 'matrix',
    label: '💚 Matrix (legacy)',
    theme: makeTheme(tokens.colors.matrix, 'dark'),
  },
  {
    key: 'light',
    label: '☀️ Light',
    theme: makeTheme(tokens.colors.light, 'light'),
  },
];

export default themeList;
