import {observer} from 'mobx-react-lite'

import {useStore} from '../../../context/mobxStore'

const UserList = observer(() => {
  const _authStore = useStore('authStore')

  return (
    <>
      User
    </>
  )
})

export default UserList
