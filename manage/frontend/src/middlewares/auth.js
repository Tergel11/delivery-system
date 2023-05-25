import {useSession} from 'next-auth/react'
import {useRouter} from 'next/router'
import {useEffect} from 'react'

import {checkAuthRole} from '../common/util/auth'

const Auth = ({children, appRoles}) => {
  const {data: session} = useSession()
  const router = useRouter()

  useEffect(() => {
    // console.log(session.user, 'session user')
    // console.log(session.applicationRoles, 'session app roles')
    if (!session)
      router.push('/auth/signin')

    if (!checkAuthRole(appRoles, session?.applicationRoles)) {
      router.push('/403')
    }
  }, [router])

  return children
}

export default Auth
