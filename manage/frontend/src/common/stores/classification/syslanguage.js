import {observable, action, makeAutoObservable} from 'mobx'
import {list, select, create, update, deleteOne, get, getForCode} from '../../services/classification/syslanguage'

class SysLanguageStore {
  @observable data = {
    list: [],
    pagination: [],
  }
  @observable searchFormValues = {}

  @observable loading = false
  @observable selectLoading = false
  @observable current = {}
  @observable selectData = []
  @observable currentKey = []

  constructor() {
    makeAutoObservable(this)
  }

  @action
  fetchList(payload, token) {
    this.loading = true
    list(payload, token).then(response => {
      if (response.result === true && response.data) {
        response.data.status = response.result
        this.data = response.data
      }
      this.loading = false
    })
  }

  @action
  fetch(payload, token) {
    this.loading = true
    const promise = get(payload, token)
    promise.then(response => {
      if (response.result === true && response.data) {
        this.current = response.data
      }
      this.loading = false
    })
    return promise
  }

  @action
  fetchForCode(payload, token) {
    const promise = getForCode(payload, token)
    promise.then(response => {
      if (response.result === true && response.data) {
        this.current = response.data
      }
      this.loading = false
    })
    return promise
  }

  @action
  fetchSelect(payload, token) {
    this.selectLoading = true
    const promise = select(payload, token)
    promise.then(response => {
      if (response.result === true && response.data) {
        this.selectData = response.data
      }
      this.selectLoading = false
    })
    return promise
  }

  @action
  create(payload, token) {
    const promise = create(payload, token)
    promise.then(response => {
      if (response.result === true && response.data) {
        this.current = response.data
      }
      this.loading = false
    })
    return promise
  }

  @action
  update(payload, token) {
    const promise = update(payload, token)
    promise.then(response => {
      if (response.result === true && response.data) {
        this.current = response.data
      }
      this.loading = false
    })
    return promise
  }

  @action
  deleteOne(payload, token) {
    return deleteOne(payload, token)
  }

  @action
  setSearchFormValues(current) {
    this.searchFormValues = current
  }

  @action
  clearCurrent() {
    this.current = {}
  }
}

export default SysLanguageStore
