import React, {useEffect} from 'react'
import {observer} from 'mobx-react-lite'
import {Select} from 'antd'
import {useStore} from '../../../context/mobxStore'

const NameSpaceSelect = observer(({
  placeHolder,
  token,
  onChange,
  allowClear,
  value,
  ...restProps
}) => {

  const nameSpaceStore = useStore('nameSpaceStore')

  useEffect(() => {
    if (nameSpaceStore?.selectData?.length === 0 && !nameSpaceStore?.selectLoading)
      nameSpaceStore.fetchSelect({active: true}, token)
  }, [])

  return (
    <Select
      onChange={onChange}
      value={value}
      style={{width: '100%'}}
      allowClear={allowClear ? allowClear : false}
      placeholder={placeHolder || 'Сонгох'}
    >
      {
        nameSpaceStore.selectData?.map((item, index) => (
          <Select.Option value={item?.id} >{item?.name}</Select.Option>
        ))
      }
    </Select>
  )
})

export default NameSpaceSelect