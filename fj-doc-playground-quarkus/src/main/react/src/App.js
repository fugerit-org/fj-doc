import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { ListGroup } from 'react-bootstrap';

import {
	BrowserRouter as Router,
	Routes,
	Route,
	Link
} from "react-router-dom";

import DevValTestForm from './playground/DocValTestForm';
import DocXmlEditor from './playground/DocXmlEditor';



function App() {

	const homepage = '/playground/fjdoc';

	let home = <h1>This is a simple playground for Venus (fj-doc)</h1>

	return (
		<Router>
			<div className="App">

				<ListGroup>
					<ListGroup.Item><Link to={homepage}>Home</Link></ListGroup.Item>
					<ListGroup.Item><Link to={homepage + "/doc_type_validator"}>Doc Type Validator</Link></ListGroup.Item>
					<ListGroup.Item><Link to={homepage + "/doc_xml_editor"}>Doc Xml Editor</Link></ListGroup.Item>
				</ListGroup>

				<Routes>
					<Route path={homepage + "/doc_type_validator"} element={<DevValTestForm />} />
					<Route path={homepage + "/doc_xml_editor"} element={<DocXmlEditor />} />
					<Route path="*" element={home}/>
				</Routes>

			</div>
		</Router>
	);
}

export default App;
