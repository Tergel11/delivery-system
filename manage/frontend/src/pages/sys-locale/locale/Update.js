import {Col, Form, Input, Modal, Row, Switch, Tooltip} from 'antd'
import {observer} from 'mobx-react-lite'

import {useStore} from '../../../context/mobxStore'
import SelectNameSpace from '../name-space/SelectNameSpace'
import LanguageSelect from '../../classification/sys-language/LanguageSelect'
import {InfoCircleOutlined} from '@ant-design/icons'
import {useSession} from 'next-auth/react'

const FormItem = Form.Item

const LocaleUpdate = observer(({
  modalOpen,
  handleUpdate,
  onClose,
  editData,
}) => {

  const [form] = Form.useForm()
  const nameSpaceStore = useStore('nameSpaceStore')
  const loading = nameSpaceStore?.loading
  const {data: session={}} = useSession()

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
      title={'Орчуулгын мэдээлэл засах'}
      width={600}
      onOk={() => submitHandle()}
      onCancel={backHandle}
      okText='Хадгалах'
      cancelText='Болих'
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
        <Row gutter={25}>
          <Col span={11}>
            <FormItem
              label='Байршил'
              name='nsId'
              initialValue={editData?.nsId}
              rules={[{required: true, message: 'Байршил сонгоно уу'}]}
              // className='mb10'
            >
              <SelectNameSpace placeHolder={'Сонгох'} token={session.token}/>
            </FormItem>
            <FormItem
              label='Хэл'
              name='lng'
              initialValue={editData?.lng}
              rules={[{required: true, message: 'Хэл сонгоно уу'}]}
              // className='mb10'
            >
              <LanguageSelect defaultValue={null} placeHolder={'Сонгох'} token={session.token}/>
            </FormItem>
          </Col>
          <Col>
            <FormItem
              label='Талбарын нэр'
              name='field'
              rules={[
                {required: true, message: 'Талбарын нэр'},
                {validator: (rule, control, callback) => validateField(rule, control, callback)}
              ]}
              // className='mb10'
            >
              <Input
                suffix={
                  <Tooltip placement={'top'} title={'Зөвхөн латин жижиг үсэг оруулна'}>
                    <InfoCircleOutlined color={'blue'}/>
                  </Tooltip>}
                placeholder='Утга'
              />
            </FormItem>
            <FormItem
              label='Идэвхтэй эсэх'
              name='active'
              valuePropName='checked'
              initialValue={true}
              // className='mb10'
            >
              <Switch checkedChildren='Тийм' unCheckedChildren='Үгүй'/>
            </FormItem>
          </Col>
        </Row>
        <FormItem
          label='Орчуулга'
          name='translation'
          rules={[{required: true, message: 'Орчуулга бичнэ үү'}]}
          // className='mb10'
        >
          <Input.TextArea placeholder='Орчуулга'/>
        </FormItem>
      </Form>
    </Modal>
  )
})

export default LocaleUpdate