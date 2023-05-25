import {observable, makeAutoObservable, action} from 'mobx'
import {list, get, select, create, update, deleteOne} from '../../services/reference/data'

class ReferenceDataStore {
  @observable data = {
    list: [],
    pagination: [],
  }
  @observable searchFormValues = {}

  @observable current = {}
  @observable selectData = []
  @observable loading = false

  constructor() {
    makeAutoObservable(this)
  }

  @action
  fetchList(payload, token) {
    this.loading = true
    list(payload, token).then(response => {
      if (response.result && response.data) {
        this.data = response.data
      }
      this.loading = false
    })
  }

  @action
  fetch(payload, token) {
    this.loading = true
    const promise = get(Object.assign({deleted: false}, payload), token)
    promise.then(response => {
      if (response.result && response.data) {
        this.current = response.data
      }
      this.loading = false
    })
    return promise
  }

  // @action
  // fetchSelect(payload) {
  //   return select(Object.assign({deleted: false}, payload))
  // }

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
    this.loading = true
    const promise = create(payload, token)
    promise.then(response => {
      if (response.result && response.data) {
        this.current = response.data
      }
      this.loading = false
    })
    return promise
  }

  @action
  update(payload, token) {
    this.loading = true
    const promise = update(payload, token)
    promise.then(response => {
      if (response.result && response.data) {
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
  setSearchFormValues(searchFormValues) {
    this.searchFormValues = searchFormValues
  }
}

export default ReferenceDataStore
