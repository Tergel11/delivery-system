import AuthStore from './auth/auth'
import BusinessRoleStore from './auth/businessRole'
import AccountStore from './auth/account'
import ReferenceDataStore from './reference/data'
import ReferenceTypeStore from './reference/type'
import SysLanguageStore from './classification/syslanguage'
import NameSpaceStore from './syslocale/namespace'
import LocaleStore from './syslocale/locale'

class RootStore {
	accountStore
	authStore
	businessRoleStore
	referenceDataStore
	referenceTypeStore
	sysLanguageStore
	nameSpaceStore
	localeStore

	constructor() {
		 this.accountStore = new AccountStore()
		 this.authStore = new AuthStore(this)
		 this.businessRoleStore = new BusinessRoleStore(this)
		 this.referenceDataStore = new ReferenceDataStore(this)
		 this.referenceTypeStore = new ReferenceTypeStore(this)
		 this.sysLanguageStore = new SysLanguageStore(this)
		 this.nameSpaceStore = new NameSpaceStore(this)
		 this.localeStore = new LocaleStore(this)
	}

	getStores() {
		 return {
			 accountStore: this.accountStore,
			 authStore: this.authStore,
			 businessRoleStore: this.businessRoleStore,
			 referenceDataStore: this.referenceDataStore,
			 referenceTypeStore: this.referenceTypeStore,
			 sysLanguageStore: this.sysLanguageStore,
			 nameSpaceStore: this.nameSpaceStore,
			 localeStore: this.localeStore
		 }
	}
}

export default RootStore
