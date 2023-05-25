// import {useRouter} from 'next/router'

import DefaultLayout from './default'
import LoginLayout from './login'

const layouts = {
  defaultLayout: DefaultLayout,
  loginLayout: LoginLayout,
}

const renderlayouts = (ls, children) => {
  const Layout = layouts[ls.mainLayout] || layouts.defaultLayout
  return (
    <Layout>
      {ls.render ? ls.render(children) : children}
    </Layout>
  )
}

const LayoutWrapper = (props) => {
  // const router = useRouter()
  // const Layout = router.asPath.split('/')[1] === 'user' ? layouts.userLayout : layouts.publicLayout
  // const Layout = layouts[Component.layout] || ((children) => <>{children}</>);

  const Layout = layouts[props.layout] || layouts.defaultLayout
  // const SubLayout = props.layouts ? renderlayouts(props.layouts, props.children) : <></>
  if (props.layouts)
    return renderlayouts(props.layouts, props.children)

  if (Layout != null)
    return <Layout {...props}>{props.children}</Layout>

  return <DefaultLayout {...props}>{props.children}</DefaultLayout>
}

export default LayoutWrapper
