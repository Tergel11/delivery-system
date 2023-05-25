import React from 'react'
import RootStore from './root'

export const rootStore = new RootStore()
export const RootStoreContext = React.createContext(rootStore)
// export const StoresProvider = StoresContext.Provider
