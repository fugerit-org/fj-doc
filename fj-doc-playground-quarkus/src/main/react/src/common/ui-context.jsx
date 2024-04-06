import React, { createContext } from "react"

const UIContext = createContext({
    user: {},
    expiresIn: 0,
    location: {},
    navigate: () => {},
    isLoading: false,
    messages: {},
    setMessages: () => {},
    setLoading: () => {},
    handleSpinner: () => {},
    clearMessages: () => {},
    clearSingleMessage: (severity, i) => {},
    setUser: () => {},
    setExpiresIn: () => {},
    setUiState: () => {},
    appService: {}
})

export default UIContext
