import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Helmet } from 'react-helmet';
import Playground from './Playground';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import {MenuItem, Select} from "@mui/material";
import React, { useState } from 'react';

const TITLE = 'Venus (fj-doc) playground';

const darkTheme = createTheme({
    palette: {
        mode: 'dark',
    },
});

const lightTheme = createTheme({
    palette: {
        mode: 'light',
    },
});

const themes = [
    { key: 'dark', theme: darkTheme },
    { key: 'light', theme: lightTheme }
]


function App() {

    const [state, setState] = useState({ currentState: darkTheme });

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
