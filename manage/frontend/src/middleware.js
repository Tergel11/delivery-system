import {withAuth} from 'next-auth/middleware'
// import {NextResponse} from 'next/server'

// More on how NextAuth.js middleware works: https://next-auth.js.org/configuration/nextjs#middleware
export default withAuth({
  callbacks: {
    authorized({_req, token}) {
      // console.log(req, 'req')
      // console.log(token?.user, 'token user')
      // `/admin` requires admin role
      // if (req.nextUrl.pathname === '/admin') {
      //   return token?.userRole === 'admin'
      // }
      // only requires the user to be logged in
      return !!token
    },
  },
})

export const config = {
  matcher:
    [
      '/dashboard',
      '/settings/:path*'
    ]
}
