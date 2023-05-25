import React, {useState} from 'react'
import {Button, Form, Input, Modal, Popconfirm, Switch, Table, message, Tooltip} from 'antd'
import {observer} from 'mobx-react-lite'

import {useStore} from '../../../context/mobxStore'
import {DeleteTwoTone, InfoCircleOutlined} from '@ant-design/icons'
import SelectNameSpace from './SelectNameSpace'

const FormItem = Form.Item

const NameSpaceCreate = observer(({
  modalOpen,
  handleUpdate,
  onClose,
  editData
}) => {

  const [form] = Form.useForm()
  const nameSpaceStore = useStore('nameSpaceStore')
  const loading = nameSpaceStore?.loading

  const backHandle = () => {
    form.resetFields()
    onClose()
  }

  const submitHandle = () => {
    form.validateFields()
      .then(fieldsValue => {
        update(fieldsValue)
      })
      .catch(errorInfo => {
        console.error('Maroon ! --> ~ file: createModal.js ~ line 54 ~ submitHandle ~ errorInfo', errorInfo)
      })
  }

  const update = (payload) => {
    const newPayload = Object.assign({...editData, ...payload})
    if (newPayload?.id) {
      handleUpdate(newPayload, form)
    }
  }

  const validateField = (_rule, control, callback) => {
    const regex = /^[a-z]+$/
    if (control) {
      const result = regex.test(control)
      if (result) {
        callback()
      } else
        callback('Талбарын нэр буруу байна!')
    } else
      callback()
  }

  return (
    <Modal
      title={`Орчуулгын харяалах байрлал засах`}
      width={500}
      onOk={() => submitHandle()}
      onCancel={backHandle}
      okText="Хадгалах"
      cancelText="Болих"
      confirmLoading={loading}
      open={modalOpen}
      forceRender
    >
      <br/>
      <Form
        layout={'vertical'}
        form={form}
        initialValues={editData}
      >
        <FormItem
          label="Нэр"
          name="name"
          rules={[{required: true, message: 'Нэр бичнэ үү'}]}
          // className="mb10"
        >
          <Input placeholder="Нэр"/>
        </FormItem>
        <FormItem
          label="Утга"
          name="value"
          rules={[
            {required: true, message: 'Утга'},
            {validator: (rule, control, callback) => validateField(rule, control, callback)}
          ]}
          extra={'Зөвхөн латин жижиг үсэг оруулна'}
          // className="mb10"
        >
          <Input
            placeholder="Утга"
          />
        </FormItem>
        <FormItem
          // label="Идэвхтэй эсэх"
          name="active"
          valuePropName={'checked'}
          // className="mb10"
        >
          <Switch checkedChildren="Идэвхтэй" unCheckedChildren="Идэвхгүй"/>
        </FormItem>
      </Form>
    </Modal>
  )
})

export default NameSpaceCreate