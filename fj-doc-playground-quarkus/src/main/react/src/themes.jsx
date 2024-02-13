import {createTheme} from "@mui/material/styles";

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

const customTheme = createTheme( {
    palette: {
        mode: 'dark',
        primary: {
            main: '#0f0',
        },
        background: {
            default: '#111111',
            paper: '#212121',
        },
    }
})

const themes = [
    { key: 'custom', theme: customTheme },
    { key: 'dark', theme: darkTheme },
    { key: 'light', theme: lightTheme }
]

export default themes;