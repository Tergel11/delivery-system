import NextAuth from 'next-auth'
import CredentialsProvider from 'next-auth/providers/credentials'

import {login} from '../../../common/services/auth/auth'

// For more information on each option (and a full list of options) go to
// https://next-auth.js.org/configuration/options
export default NextAuth({
  // https://next-auth.js.org/configuration/providers
  providers: [
    CredentialsProvider({
      // The name to display on the sign in form (e.g. 'Sign in with...')
      name: 'Credentials',
      // `credentials` is used to generate a form on the sign in page.
      // You can specify which fields should be submitted, by adding keys to the `credentials` object.
      // e.g. domain, username, password, 2FA token, etc.
      // You can pass any HTML attribute to the <input> tag through the object.
      // credentials: {
      //   username: {label: 'Нэвтрэх нэр', type: 'text'},
      //   password: {label: 'Нууц үг', type: 'password'}
      // },
      async authorize(credentials, _req) {
        // console.log(credentials, 'credentials')
        return login(credentials?.username, credentials?.password)
          .then(response => {
            // console.log(response, 'login response')
            if (response.result === true && response.data) {
              return Promise.resolve(response.data)
            } else {
              // console.log(response.message, 'response message')
              return Promise.reject(new Error(response.message))
            }
          })
      }
    }),
  ],
  // Database optional. MySQL, Maria DB, Postgres and MongoDB are supported.
  // https://next-auth.js.org/configuration/databases
  //
  // Notes:
  // * You must install an appropriate node_module for your database
  // * The Email provider requires a database (OAuth providers do not)
  // database: process.env.DATABASE_URL,

  // The secret should be set to a reasonably long random string.
  // It is used to sign cookies and to sign and encrypt JSON Web Tokens, unless
  // a separate secret is defined explicitly for encrypting the JWT.
  secret: process.env.NEXTAUTH_SECRET,

  session: {
    // Use JSON Web Tokens for session instead of database sessions.
    // This option can be used with or without a database for users/accounts.
    // Note: `strategy` should be set to 'jwt' if no database is used.
    strategy: 'jwt',
    name: 'portal'
    // Seconds - How long until an idle session expires and is no longer valid.
    // maxAge: 30 * 24 * 60 * 60, // 30 days

    // Seconds - Throttle how frequently to write to database to extend a session.
    // Use it to limit write operations. Set to 0 to always update the database.
    // Note: This option is ignored if using JSON Web Tokens
    // updateAge: 24 * 60 * 60, // 24 hours
  },

  // JSON Web tokens are only used for sessions if the `strategy: 'jwt'` session
  // option is set - or by default if no database is specified.
  // https://next-auth.js.org/configuration/options#jwt
  jwt: {
    // A secret to use for key generation (you should set this explicitly)
    secret: process.env.NEXTAUTH_SECRET,
    // Set to true to use encryption (default: false)
    // encryption: true,
    // You can define your own encode/decode functions for signing and encryption
    // if you want to override the default behaviour.
    // encode: async ({ secret, token, maxAge }) => {},
    // decode: async ({ secret, token, maxAge }) => {},
  },

  // You can define custom pages to override the built-in ones. These will be regular Next.js pages
  // so ensure that they are placed outside of the '/api' folder, e.g. signIn: '/auth/mycustom-signin'
  // The routes shown here are the default URLs that will be used when a custom
  // pages is not specified for that route.
  // https://next-auth.js.org/configuration/pages
  pages: {
    signIn: '/auth/signin',
    signOut: '/auth/signout',
    error: '/auth/signin', // Error code passed in query string as ?error=
    verifyRequest: '/auth/verify', // Used for check email page
    newUser: undefined // If set, new users will be directed here on first sign in
  },

  // Callbacks are asynchronous functions you can use to control what happens
  // when an action is performed.
  // https://next-auth.js.org/configuration/callbacks
  callbacks: {
    // async signIn({ user, account, profile, email, credentials }) { return true },
    // async redirect({ url, baseUrl }) { return baseUrl },
    // async jwt({ token, user, account, profile, isNewUser }) { return token }
    jwt: ({token, user}) => {
      // console.log(token, 'token2')
      // console.log(user, 'userData')
      if (user) {
        token.token = user.token
        token.user = user
      }

      return token
    },
    async session({session, token, _user}) {
      // console.log(token, 'token')
      if (token) {
        // console.log(token?.user, 'token user')
        session.user = {
          name: token?.user?.fullName,
          email: token?.user?.email,
          image: token?.user?.profileImageUrl,
        }
        // session.expires = token?.user.expires
        session.token = token?.token
        session.businessRole = token?.user?.businessRole?.role
        session.applicationRoles = token?.user?.businessRole?.applicationRoles
      }
      // console.log(session, 'session')
      return session
    },
  },

  // Events are useful for logging
  // https://next-auth.js.org/configuration/events
  events: {},

  // Enable debug messages in the console if you are having problems
  debug: false,
})
