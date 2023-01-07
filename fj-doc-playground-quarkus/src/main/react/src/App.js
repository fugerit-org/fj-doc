import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Helmet } from 'react-helmet';
import { Fragment } from 'react';
import Playground from './Playground';

const TITLE = 'Venus (fj-doc) playground';

function App() {

	return (
		<Fragment>
			<Helmet>
				<title>{TITLE}</title>
			</Helmet>
			<Playground />
		</Fragment>
	);
}

export default App;
