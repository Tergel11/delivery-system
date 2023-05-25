import {useEffect, useState} from 'react'
import {useRouter} from 'next/router'
import {useSession} from 'next-auth/react'
import Link from 'next/link'
import {observer} from 'mobx-react-lite'
import {
  Layout,
  Menu
} from 'antd'
import {
  DashboardOutlined, OrderedListOutlined,
  SettingOutlined,
  UsergroupAddOutlined,
  UserOutlined
} from '@ant-design/icons'
import classNames from 'classnames'

// import {useStore} from '../../context/mobxStore'
import {checkAuthRole} from '../../../common/util/auth'

import styles from './sideMenu.module.scss'

const rootMenuKeys = [
  'dashboard',
  'report',
  'settings',
]

const menuItemTemplate = [
  {
    key: 'dashboard',
    label: <Link href={'/dashboard'}>Хянах самбар</Link>,
    // title: 'Хянах самбар',
    icon: <DashboardOutlined/>,
    role: 'ROLE_DASHBOARD_VIEW',
  },
  {
    key: 'report',
    label: 'Тайлан',
    // title: 'Тайлан',
    icon: <DashboardOutlined/>,
    role: ['ROLE_REPORT_USER', 'ROLE_REPORT_PAYMENT'],
    children: [
      {
        key: 'report-user',
        label: <Link href={'/report/user'}>Хэрэглэгчийн тайлан</Link>,
        title: 'Хэрэглэгчийн тайлан',
        icon: <UsergroupAddOutlined/>,
        role: 'ROLE_REPORT_USER',
      },
      {
        key: 'report-payment',
        label: <Link href={'/report/payment'}>Төлбөрийн тайлан</Link>,
        title: 'Төлбөрийн тайлан',
        icon: <UserOutlined/>,
        role: 'ROLE_REPORT_PAYMENT',
      }
    ],
  },
  {
    key: 'settings',
    label: 'Тохиргоо',
    // title: 'Тохиргоо',
    icon: <SettingOutlined/>,
    role: ['ROLE_BUSINESS_ROLE_VIEW', 'ROLE_USER_VIEW'],
    children: [
      {
        key: 'settings-business-role',
        label: <Link href={'/settings/business-role'}>Хэрэглэгчийн эрх</Link>,
        title: 'Хэрэглэгчийн эрх',
        icon: <UsergroupAddOutlined/>,
        role: 'ROLE_BUSINESS_ROLE_VIEW',
      },
      {
        key: 'settings-user',
        label: <Link href={'/settings/user'}>Системийн хэрэглэгч</Link>,
        title: 'Системийн хэрэглэгч',
        icon: <UserOutlined/>,
        role: 'ROLE_USER_VIEW',
      }
    ],
  },
  {
    key: 'reference',
    label: 'Лавлах сан',
    // title: 'Тохиргоо',
    icon: <OrderedListOutlined/>,
    role: ['ROLE_REFERENCE_TYPE_VIEW', 'ROLE_REFERENCE_DATA_VIEW'],
    children: [
      {
        key: 'reference-type',
        label: <Link href={'/reference/type'}>Төрөл</Link>,
        title: 'Төрөл',
        icon: <OrderedListOutlined/>,
        role: 'ROLE_REFERENCE_TYPE_VIEW'
      },
      {
        key: 'reference-data',
        label: <Link href={'/reference/data'}>Дата</Link>,
        title: 'Дата',
        icon: <OrderedListOutlined/>,
        role: 'ROLE_REFERENCE_DATA_VIEW',
      }
    ],
  },
  {
    key: 'classification',
    label: 'Ангилалын сан',
    // title: 'Тохиргоо',
    icon: <OrderedListOutlined/>,
    role: ['ROLE_MANAGE_DEFAULT'],
    children: [
      {
        key: 'classification-sys-language',
        label: <Link href={'/classification/sys-language'}>Системийн хэл</Link>,
        title: 'Системийн хэл',
        icon: <OrderedListOutlined/>,
        role: 'ROLE_LOCALE_VIEW'
      }
    ],
  },
  {
    key: 'sys-locale',
    label: 'Хэлний удирдлага',
    // title: 'Тохиргоо',
    icon: <OrderedListOutlined/>,
    role: ['ROLE_LOCALE_VIEW'],
    children: [
      {
        key: 'name-space',
        label: <Link href={'/sys-locale/name-space'}>Байрлал</Link>,
        title: 'Байрлал',
        icon: <OrderedListOutlined/>,
        role: 'ROLE_LOCALE_VIEW'
      },
      {
        key: 'locale',
        label: <Link href={'/sys-locale/locale'}>Орчуулга</Link>,
        title: 'Орчуулга',
        icon: <OrderedListOutlined/>,
        role: 'ROLE_LOCALE_VIEW',
      }
    ],
  },
]

const SideMenu = observer(({collapsed, setCollapsed}) => {
  // const rootStore = useRootStore()
  // const authStore = useStore('authStore')
  const router = useRouter()
  const {data: session = {}} = useSession()
  const [menuItems, setMenuItems] = useState([])
  const [openKeys, setOpenKeys] = useState([])

  useEffect(() => {
    // console.log(router.pathname.split('/').filter(item => item !== ''), 'path name')
    setOpenKeys(router.pathname.split('/').filter(item => item !== ''))
  }, [router.pathname])

  useEffect(() => {
    // console.log(router.pathname.split('/').filter(item => item !== ''), 'path name')
    if (session?.applicationRoles)
      setMenuItems(getCheckedMenuItems(menuItemTemplate, session.applicationRoles))
  }, [session])

  const onOpenChange = keys => {
    const latestOpenKey = keys.find(key => openKeys.indexOf(key) === -1)
    // console.log(latestOpenKey, 'latestOpenKey')
    if (rootMenuKeys.indexOf(latestOpenKey) === -1) {
      setOpenKeys(keys)
    } else {
      setOpenKeys(latestOpenKey ? [latestOpenKey] : [])
    }
  }

  const getCheckedMenuItems = (menuItems, applicationRoles) => {
    const checkedMenuItems = []

    menuItems.map(menuItem => {
      let checkedChildren = null

      if (menuItem.children) {
        checkedChildren = []
        menuItem.children.map(subMenuItem => {
          const hasSubAccess = checkAuthRole(subMenuItem.role, applicationRoles)
          checkedChildren.push(Object.assign(subMenuItem, {
            disabled: !hasSubAccess,
            label: hasSubAccess ? subMenuItem.label : subMenuItem.title,
          }))
        })
      }

      const hasAccess = checkAuthRole(menuItem.role, applicationRoles)

      checkedMenuItems.push(Object.assign(menuItem, {
        // style: !hasAccess && {opacity: '0.25'},
        disabled: !hasAccess,
        // label: hasAccess ? menuItem.label : menuItem.title,
        children: checkedChildren
      }))
    })

    // console.log(checkedMenuItems, 'checkedMenuItems')
    return checkedMenuItems
  }

  // const onMenuClick = (e) => {
  //   console.log(e)
  // }

  // console.log(openKeys, 'openKeys')
  return (
    <Layout.Sider 
      // trigger={null} 
      // theme='light' 
      width={collapsed ? 80 : 280} 
      collapsible 
      collapsed={collapsed} 
      onCollapse={(value) => setCollapsed(value)}
      className={styles.sideMenu}
    >
      <div className={styles.wrapper}>
        <div className={classNames(styles.logoWrapper, collapsed && styles.small)}>
          <Link href='/dashboard'>
            {!collapsed ? (
              <img
                src='/images/logo/logo-light.svg'
                alt='logo-main'
              />
            )
              :
              (
                <img
                  src='/images/logo/logo-light-sm.svg'
                  alt='logo-main'
                  style={{height: 40}}
                />
              )}
          </Link>
        </div>
        <Menu
          openKeys={openKeys}
          // selectedKeys={openKeys}
          onOpenChange={onOpenChange}
          mode='inline'
          theme='light'
          items={menuItems}
          // onClick={onMenuClick}
        />
        {/* <div className={styles.bottomWrapper}>
        <Button
          block
          size='large'
          type='primary'
          icon={<LogoutOutlined/>}
          onClick={() => router.push('/auth/signout')}
        >
          {!collapsed && 'Системээс гарах'}
        </Button>
        {!collapsed && <div className={styles.copyright}>© {dayjs().format('YYYY')} Accept System</div>}
      </div> */}
      </div>
    </Layout.Sider>
  )
})

export default SideMenu
