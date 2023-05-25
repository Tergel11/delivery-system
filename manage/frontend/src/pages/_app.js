import {SessionProvider} from 'next-auth/react'
import dynamic from 'next/dynamic'
import {ConfigProvider} from 'antd'

import {MobxProvider} from '../context/mobxStore'
import Auth from '../middlewares/auth'

import 'antd/dist/reset.css'
import CustomVars from '../styles/customVars.json'

const LayoutWrapper = dynamic(() => import('../layouts'))

import '../styles/vars.scss'
import '../styles/global.scss'

function StarterApp(
  {
    Component,
    pageProps: {session, ...pageProps}
  }) {
  return <MobxProvider>
    <SessionProvider session={session}>
      <ConfigProvider
        theme={CustomVars}
      >
        <LayoutWrapper layouts={Component?.layouts} layout={Component?.layout || 'defaultLayout'} {...pageProps}>
          {Component.appRoles ?
            <Auth appRoles={Component.appRoles}>
              <Component {...pageProps} />
            </Auth>
            :
            <Component {...pageProps} />
          }
        </LayoutWrapper>
      </ConfigProvider>
    </SessionProvider>
  </MobxProvider>
}

export default StarterApp
