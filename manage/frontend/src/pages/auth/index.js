import {useEffect} from 'react'
import {useRouter} from 'next/router'
// import {Col, Row} from 'antd'

import styles from './auth.module.scss'

const Auth = ({children}) => {
  const router = useRouter()
  // const [activeKey, setActiveKey] = useState<string>()

  useEffect(() => {
    const pathNames = router.pathname.split('/')
    if (pathNames.length > 2) {
      // setActiveKey(router.pathname.split('/')[2])
    } else {
      router.push('/auth/signin')
    }
  }, [router])

  return (
    <div className={styles.main}>
      <div className={styles.card}>
        {children}
      </div>
    </div>
  )
}

export default Auth
