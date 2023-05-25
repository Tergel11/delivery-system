import {useState, useEffect} from 'react'
import {useRouter} from 'next/router'
import dynamic from 'next/dynamic'
import {DefaultSeo} from 'next-seo'
import {signOut, useSession} from 'next-auth/react'
import {observer} from 'mobx-react-lite'
import dayjs from 'dayjs'
import {
  FloatButton,
  Layout
} from 'antd'
import {
  ArrowUpOutlined
} from '@ant-design/icons'

const SideMenu = dynamic(() => import('../../components/elements/sideMenu'))
const Header = dynamic(() => import('../../components/elements/header'))

import styles from './defaultLayout.module.scss'

const DefaultLayout = observer(({children}) => {
  const router = useRouter()
  const {data: session} = useSession()
  const [collapsed, setCollapsed] = useState(true)

  useEffect(() => {
    const logout = () => {
      signOut({redirect: false, callbackUrl: '/auth/signin'})
        .then(signOutResponse => {
          // console.log(signOutResponse, 'signOutResponse')
          router.push(signOutResponse.url)
        })
        .catch(e => {
          console.log(e)
          router.push('/auth/signin')
        })
    }

    if (session && Object.keys(session).length > 0 && session.expires && !dayjs().isBefore(dayjs(session.expires))) {
      logout()
    }
  }, [router, session])

  return (
    <>
      <DefaultSeo title='Astvision Starter' />
      <Layout hasSider className={styles.wrapper}>
        <SideMenu collapsed={collapsed} setCollapsed={setCollapsed} />
        <Layout style={{
          marginLeft: collapsed ? 80 : 280,
        }}>
          <Header collapsed={collapsed} setCollapsed={setCollapsed} />
          <Layout.Content style={{backgroundColor: 'transparent'}}>
            <div className={styles.content}>
              {children}
            </div>
            <FloatButton.BackTop>
              <div className={styles.anchor}>
                <ArrowUpOutlined/>
              </div>
            </FloatButton.BackTop>
          </Layout.Content>
        </Layout>
      </Layout>
    </>
  )
})

export default DefaultLayout
