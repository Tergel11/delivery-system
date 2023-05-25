import {action, makeAutoObservable, observable, runInAction} from 'mobx'
import {list as listApplicationRoles} from '../../services/auth/applicationRole'
import {list, select} from '../../services/auth/businessRole'

class BusinessRoleStore {
  rootStore
  @observable loading = false
  @observable applicationRoles = []
  @observable select = []
  @observable data = {
    list: [],
    pagination: [],
  };

  constructor(rootStore) {
    this.rootStore = rootStore
    makeAutoObservable(this)
  }

  @action
  fetchApplicationRoles(token) {
    this.loading = true
    listApplicationRoles(token)
      .then(response => {
        runInAction(() => {
          this.applicationRoles = response.data
          this.loading = false
        })
      })
  }

  @action
  fetchList(token, payload) {
    this.loading = true
    list(token, payload)
      .then(response => {
        runInAction(() => {
          this.data = response.data
          this.loading = false
        })
      })
      .catch(() => {
        runInAction(() => {
          this.data = {
            list: [],
            pagination: [],
          }
          this.loading = false
        })
      })
  }

  @action
  fetchSelect(token, payload) {
    this.loading = true
    select(token, payload)
      .then(response => {
        if (response.result === true && response.data) {
          runInAction(() => {
            this.select = response.data
          })
        }
        runInAction(() => {
          this.loading = false
        })
      })
  }
}

export default BusinessRoleStore
