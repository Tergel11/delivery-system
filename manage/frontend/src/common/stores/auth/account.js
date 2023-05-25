import {action, makeAutoObservable, observable, runInAction} from 'mobx'
import {getProfile} from '../../services/auth/account'

class AccountStore {
  rootStore
  @observable loading = false
  @observable profile

  constructor(rootStore) {
    this.rootStore = rootStore
    makeAutoObservable(this)
  }

  @action
  fetchProfile(token) {
    this.loading = true
    const _promise = getProfile(token)
      .then(response => {
        runInAction(() => {
          this.profile = response.data
          this.loading = false
        })
      })
  }
}

export default AccountStore
