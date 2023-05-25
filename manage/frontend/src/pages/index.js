import {useEffect} from 'react'
import {useRouter} from 'next/router'
import dynamic from 'next/dynamic'
import {observer} from 'mobx-react-lite'

const Loader = dynamic(() => import('../components/elements/loader'))

const Base = observer(() => {
  const router = useRouter()

  useEffect(() => {
    router.push('/dashboard')
  })

  return <Loader/>
})

export default Base
