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

const themes = [
    { key: 'dark', theme: darkTheme },
    { key: 'light', theme: lightTheme }
]

export default themes;