import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Helmet } from 'react-helmet';
import Playground from './Playground';
import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import {MenuItem, Select} from "@mui/material";
import React, { useState } from 'react';
import themes from './themes'

const TITLE = 'Venus (fj-doc) playground';

function App() {

    const [state, setState] = useState({ currentState: themes.at( 2 ).theme });

    return (
        <ThemeProvider theme={state.currentState}>
            <CssBaseline />
            <Helmet>
                <title>{TITLE}</title>
            </Helmet>
            <Playground />
            <p>Select theme :
            <Select id="themeCatalog"
                    onChange={e => {
                        setState( { currentState: e.target.value } )
                    }}
                    value={state.currentState}
                    defaultValue={state.currentState}>
                {themes.map( (current) =>  <MenuItem key={current.key} selected={current.theme===state.currentState} value={current.theme}>{current.key}</MenuItem>)}
            </Select></p>
        </ThemeProvider>
    );
}

export default App;
