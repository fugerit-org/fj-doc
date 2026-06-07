// Design tokens — colori, tipografia, spacing per MUI v6
export const tokens = {
  colors: {
    // Verde Fugerit (default / opzione 1)
    fugerit: {
      primary: '#00C17C',
      primaryDark: '#009960',
      primaryLight: '#33CC96',
      secondary: '#6C63FF',
      background: '#0D1117',
      paper: '#161B22',
      paperElevated: '#1C2128',
      border: '#30363D',
      text: '#E6EDF3',
      textSecondary: '#8B949E',
    },
    // Blu/Viola tech (opzione 2)
    techBlue: {
      primary: '#6C63FF',
      primaryDark: '#4B3FE4',
      primaryLight: '#8F88FF',
      secondary: '#00C17C',
      background: '#0A0E1A',
      paper: '#111827',
      paperElevated: '#1A2235',
      border: '#1F2D48',
      text: '#E2E8F0',
      textSecondary: '#94A3B8',
    },
    // Neutro professionale (opzione 3)
    professional: {
      primary: '#38BDF8',
      primaryDark: '#0EA5E9',
      primaryLight: '#7DD3FC',
      secondary: '#F472B6',
      background: '#0F172A',
      paper: '#1E293B',
      paperElevated: '#273549',
      border: '#334155',
      text: '#F1F5F9',
      textSecondary: '#94A3B8',
    },
    // Verde Matrix - custom (opzione 4, corrisponde al tema precedente)
    matrix: {
      primary: '#00FF00',
      primaryDark: '#00CC00',
      primaryLight: '#33FF33',
      secondary: '#00FFAA',
      background: '#111111',
      paper: '#212121',
      paperElevated: '#2A2A2A',
      border: '#333333',
      text: '#E0E0E0',
      textSecondary: '#9E9E9E',
    },
    // Light (opzione 5)
    light: {
      primary: '#00C17C',
      primaryDark: '#009960',
      primaryLight: '#33CC96',
      secondary: '#6C63FF',
      background: '#F5F7FA',
      paper: '#FFFFFF',
      paperElevated: '#F0F2F5',
      border: '#E0E4EA',
      text: '#1A1F2E',
      textSecondary: '#6B7280',
    },
  },
  typography: {
    fontFamily: '"Inter", "Roboto", "Helvetica Neue", Arial, sans-serif',
    fontWeightLight: 300,
    fontWeightRegular: 400,
    fontWeightMedium: 500,
    fontWeightBold: 700,
    fontSize: 14,
  },
  shape: {
    borderRadius: 10,
  },
  spacing: 8,
};

export default tokens;
