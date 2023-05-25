import React, {useState} from 'react'
import {observer} from 'mobx-react-lite'
import {Form, Input, Tabs} from 'antd'
import LanguageSelect from './LanguageSelect'

const MultiLangField = observer(({
  token,
  value,
  name,
  type,
  form,
  required,
  onChange,
  formItemName,
  ...restProps
}) => {

  const [currentCode, setCurrentCode] = useState('mn')
  const CustomItem = type === 'Input' && Input || type === 'TextArea' && Input.TextArea

  const onSelectChange = (key) => {
    if (!value[key]) {
      Object.assign(value, {
        [key]: ''
      })
    }
    setCurrentCode(key)
  }

  const handleEdit = (key, action) => {
    switch (action) {
      case 'remove':
        handleRemove(key)
        break
      default:
        console.log('no action found')
    }
  }

  const handleRemove = (key) => {
    delete value[key]
    setCurrentCode('mn')
  }

  const renderExtraAction = () => (
    <div style={{width: '120px'}}>
      <LanguageSelect
        allowClear={false}
        defaultValue={currentCode}
        onChange={onSelectChange}
        token={token}/>
    </div>
  )

  const items = value && Object.entries(value).map(([key, val]) => {
    return {
      label: key,
      key: key,
      closable: key !== 'mn',
      children: (

        <Form.Item
          key={key}
          name={[formItemName, key]}
          rules={[{required: !!required, message: `${key} ${name} оруулна уу!`}]}
        >
          <CustomItem
            placeholder={`${key} ${name.toLowerCase()}`}
          />
        </Form.Item>
      )
    }
  })

  return (
    <Tabs
      hideAdd
      tabBarExtraContent={renderExtraAction()}
      activeKey={currentCode}
      type={'editable-card'}
      onEdit={(key, action) => handleEdit(key, action)}
      onChange={(key) => setCurrentCode(key)}
      forcerender
      items={items}
    />
  )
})

export default MultiLangField