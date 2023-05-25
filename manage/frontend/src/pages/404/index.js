import {Button} from 'antd'
import {useRouter} from 'next/router'
import {RightOutlined} from '@ant-design/icons'

import styles from './404.module.scss'

const Custom404 = () => {
  const router = useRouter()
  return (
    <div className={styles.wrapper}>
      <h1 className={styles.text}>Хуудас олдсонгүй</h1>
      <Button type='link' onClick={() => router.push('/')}>Нүүр хуудас руу буцах <RightOutlined /></Button>
    </div>
  )
}

export default Custom404
