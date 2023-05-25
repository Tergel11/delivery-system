import {useState, useEffect} from 'react'
import {observer} from 'mobx-react-lite'
import {
  Form,
  Input,
  message,
  Modal,
  Select
} from 'antd'

import {create, update} from '../../../common/services/auth/businessRole'
import {useStore} from '../../../context/mobxStore'
import {useSession} from 'next-auth/react'

const BusinessRoleCreateUpdateModal = observer(({visible, updateData, closeModal}) => {
  const {data: session = {}} = useSession()
  const [form] = Form.useForm()
  const businessRoleStore = useStore('businessRoleStore')
  const [loading, setLoading] = useState(false)
  // const [formValues, setFormValues] = useState({}) // needed for form.getFieldValue to work

  useEffect(() => {
    form.resetFields()
  }, [updateData])

  const isCreate = updateData && !updateData.key

  const submitHandle = () => {
    form.validateFields()
      .then((values) => {
        setLoading(true)
        const promise = isCreate ? create : update
        promise(session.token, values)
          .then(response => {
            setLoading(false)
            if (response.result) {
              message.success(`Амжилттай ${isCreate ? 'бүртгэлээ' : 'хадгаллаа'}`)
              backHandle(true)
            } else
              message.error(`${isCreate ? 'Бүртгэхэд' : 'Хадгалахад'} алдаа гарлаа: ${response.message}`)
          })
          .catch(e => {
            setLoading(false)
            message.error(`${isCreate ? 'Бүртгэхэд' : 'Хадгалахад'} алдаа гарлаа: ${e.message}`)
          })
      })
  }

  const backHandle = (refresh) => {
    form.resetFields()
    closeModal(refresh)
  }

  return (
    <Modal
      title={`Цахим ажлын байр ${create ? 'бүртгэх' : 'засварлах'}`}
      visible={visible}
      onOk={submitHandle}
      onCancel={backHandle}
      okText={create ? 'Бүртгэх' : 'Хадгалах'}
      cancelText='Буцах'
      confirmLoading={loading}
    >
      <Form
        form={form}
        initialValues={updateData}
        onFinish={submitHandle}
        layout='vertical'
        // onValuesChange={(values) => setFormValues(values)}
      >
        <Form.Item
          label='Хэрэглэгчийн эрх'
          name='role'
          rules={
            [{required: true, message: 'Хэрэглэгчийн эрх бичнэ үү'}]
          }>
          <Input placeholder='Цахим ажлын байр' readOnly={!create}/>
        </Form.Item>
        <Form.Item
          label='Нэр'
          name='name'
          rules={
            [{required: true, message: 'Нэр бичнэ үү'}]
          }>
          <Input placeholder='Нэр'/>
        </Form.Item>
        <Form.Item
          label='Хандах эрх'
          name='applicationRoles'
        >
          <Select
            mode='multiple'
            placeholder='Хандах эрхүүд сонгоно уу'
            style={{width: '100%'}}
          >
            {
              businessRoleStore.applicationRoles?.map(appRole =>
                <Select.Option key={appRole} value={appRole}>{appRole}</Select.Option>)
            }
          </Select>
        </Form.Item>
      </Form>
    </Modal>
  )
})

export default BusinessRoleCreateUpdateModal
