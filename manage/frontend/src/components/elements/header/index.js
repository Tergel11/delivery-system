import {useSession} from 'next-auth/react'
import Link from 'next/link'
import {observer} from 'mobx-react-lite'
import {
  Avatar,
  Button,
  Dropdown,
  Layout,
} from 'antd'
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UserOutlined,
} from '@ant-design/icons'

import styles from './header.module.scss'

const items = [
  {
    label: <Link href={'/profile'}>Миний мэдээлэл</Link>,
    key: 'profile',
  },
  {
    type: 'divider',
  },
  {
    label: <Link href={'/auth/signout'}>Системээс гарах</Link>,
    key: 'signout',
    danger: true,
  },
]

const Header = observer(({collapsed, setCollapsed}) => {
  const {data: session = {}} = useSession()

  return (
    <Layout.Header theme='light' className={styles.wrapper}>
      <Button 
        type='primary'
        ghost
        icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />} 
        onClick={() => setCollapsed(!collapsed)}
      />
      <div className={styles.right}>
        <Dropdown 
          menu={{items}}
          placement='bottomRight'
          trigger={['click']}
        >
          <div className={styles.user}>
            <div className={styles.left}>
              <Avatar size={30} icon={<UserOutlined />} />
            </div>
            <div className={styles.middle}>
              <h3>{session?.user?.name || 'Хэрэглэгч'}</h3>
              <p>{session?.user?.email || '-'}</p>
            </div>
          </div>
        </Dropdown>
      </div>
    </Layout.Header>
  )
})

export default Header
