import React from 'react'
import {rootStore, RootStoreContext} from '../common/stores/stores'

export const useRootStore = () => React.useContext(RootStoreContext)

export const useStore = (store) => React.useContext(RootStoreContext)[store]

export function MobxProvider({children}) {
  // only create root store once (store is a singleton)
  return <RootStoreContext.Provider value={rootStore}>{children}</RootStoreContext.Provider>
}
