import {makeAutoObservable} from 'mobx'

class AuthStore {
  rootStore

  constructor(rootStore) {
    this.rootStore = rootStore
    makeAutoObservable(this)
  }
}

export default AuthStore
