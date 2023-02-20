import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Helmet } from 'react-helmet';
import Playground from './Playground';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';

const TITLE = 'Venus (fj-doc) playground';

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
  },
});

function App() {

	return (
		<ThemeProvider theme={darkTheme}>
      		<CssBaseline />
			<Helmet>
				<title>{TITLE}</title>
			</Helmet>
			<Playground />
		</ThemeProvider>
	);
}

export default App;
