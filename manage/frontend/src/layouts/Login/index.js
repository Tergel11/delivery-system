import Link from 'next/link'
import {DefaultSeo} from 'next-seo'

import styles from './loginLayout.module.scss'

const LoginLayout = ({children}) => {
  return (
    <>
      <DefaultSeo title='Astvision Starter' />
      <div className={styles.container} style={{backgroundImage: 'url(/images/common/auth-background.svg)'}}>
        <div className={styles.content}>
          <div className={styles.top}>
            <div className={styles.header}>
              <Link href='/'>
                <img alt='logo' className={styles.logo} src='/images/logo/logo.svg' />
              </Link>
            </div>
          </div>
          {children}
        </div>
      </div>
    </>
  )
}

export default LoginLayout
