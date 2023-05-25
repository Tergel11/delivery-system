import Document, {Html, Head, Main, NextScript} from 'next/document'

class StarterDocument extends Document {
  static async getInitialProps(ctx) {
    const initialProps = await Document.getInitialProps(ctx)
    return {...initialProps}
  }

  render() {
    return (
      <Html>
        <Head>
          <link rel='shortcut icon' href='/images/logo/favicon.ico' />
          <link
            rel='preload'
            href='/fonts/Manrope/Manrope-Bold.ttf'
            as='font'
            crossOrigin=''
          />
          <link
            rel='preload'
            href='/fonts/Manrope/Manrope-Medium.ttf'
            as='font'
            crossOrigin=''
          />
          <link
            rel='preload'
            href='/fonts/Manrope/Manrope-Regular.ttf'
            as='font'
            crossOrigin=''
          />
        </Head>
        <body>
          <Main />
          <NextScript />
        </body>
      </Html>
    )
  }
}

export default StarterDocument
