import {useEffect} from 'react'
import {signOut} from 'next-auth/react'
import {useRouter} from 'next/router'
import {Spin} from 'antd'

const SignOut = () => {
  const router = useRouter()
  useEffect(() => {
    signOut({redirect: false, callbackUrl: '/auth/signin'})
      .then(signOutResponse => {
        // console.log(signOutResponse, 'signOutResponse')
        router.push(signOutResponse.url)
      })
      .catch(e => {
        console.log(e)
        router.push('/auth/signin')
      })
  }, [router])
  return <Spin/>
}

SignOut.layout = 'loginLayout'

export default SignOut
