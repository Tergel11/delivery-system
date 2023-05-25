import React, {useState} from 'react'
import {Button, Form, Input, Modal, Popconfirm, Switch, Table, message, Tooltip, Row, Col} from 'antd'
import {observer} from 'mobx-react-lite'

import {useStore} from '../../../context/mobxStore'
import {DeleteTwoTone, InfoCircleOutlined} from '@ant-design/icons'
import SelectNameSpace from '../name-space/SelectNameSpace'
import {useSession} from 'next-auth/react'
import LanguageSelect from '../../classification/sys-language/LanguageSelect'

const FormItem = Form.Item

const LocaleCreate = observer(({
  modalOpen,
  handleCreate,
  onClose
}) => {

  const [form] = Form.useForm()
  const localeStore = useStore('localeStore')
  const loading = localeStore?.loading
  const [data, setData] = useState([])
  const {data: session={}} = useSession()

  const submitHandle = () => {
    handleCreate(data, form)
  }

  const backHandle = () => {
    form.resetFields()
    onClose()
  }

  const handleDelete = (i) => {
    let newData = data.filter((item) => item !== i)
    setData(newData)
  }

  const add = (items) => {
    setData((prevState) => [...prevState, items])
  }

  const handleAdd = () => {
    if (data.length > 20){
      return message.error('Нэг удаад бүртгэх тоо хэтэрсэн байна.')
    }
    let duplicated = false
    form.validateFields()
      .then(fieldsValue => {
        data.forEach((item) => {
          if (item.field === fieldsValue.field && item.lng === fieldsValue.lng && item.nsId === fieldsValue.nsId)
            return duplicated = true
        })
        if (duplicated)
          return message.error('Талбарын нэр давхцсан байна.')
        else
          add(fieldsValue)
      })
      .catch(errorInfo => {
        console.error('Maroon ! --> ~ file: createModal.js ~ line 60 ~ submitHandle ~ errorInfo', errorInfo)
      })
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

  const columns = [
    {
      title: '№',
      dataIndex: '',
      width: '20px',
      render: (_, __, index) => index + 1
    },
    {
      title: 'Байршил',
      dataIndex: 'nsName',
      align: 'center'
    },
    {
      title: 'Хэл',
      dataIndex: 'lng',
      align: 'center'
    },
    {
      title: 'Талбарын нэр',
      dataIndex: 'field',
      align: 'center'
    },
    {
      title: 'Орчуулга',
      dataIndex: 'translation',
      align: 'center'
    },
    {
      title: 'Үйлдэл',
      width: '100px',
      render: (_, record) => (
        <>
          <Tooltip placement="top" title="Устгах">
            <Popconfirm title="Устгах уу ?" onConfirm={() => handleDelete(record)}>
              <Button icon={<DeleteTwoTone/>} style={{color: 'red'}} type="dashed" shape="circle"/>
            </Popconfirm>
          </Tooltip>
        </>
      )
    }]

  return (
    <Modal
      title={`Орчуулгын мэдээлэл бүртгэх`}
      width={800}
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
        initialValues={{}}
      >
        <Row gutter={25}>
          <Col span={11}>
            <FormItem
              wrapperCol={{span: 20}}
              label="Байршил"
              name="nsId"
              rules={[{required: true, message: 'Нэр бичнэ үү'}]}
              // className="mb10"
            >
              <SelectNameSpace
                onChange={(_, option) => form.setFieldValue('nsName', option.children)}
                placeHolder={'Сонгох'}
                token={session.token}
              />
            </FormItem>
            <FormItem
              wrapperCol={{span: 20}}
              label="Хэл"
              name="lng"
              rules={[{required: true, message: 'Нэр бичнэ үү'}]}
              // className="mb10"
            >
              <LanguageSelect defaultValue={null} placeHolder={'Сонгох'} token={session.token}/>
            </FormItem>
          </Col>
          <Col>
            <FormItem
              wrapperCol={{span: 24}}
              label="Талбарын нэр"
              name="field"
              rules={[
                {required: true, message: 'Талбарын нэр'},
                {validator: (rule, control, callback) => validateField(rule, control, callback)}
              ]}
              // className="mb10"
            >
              <Input
                suffix={
                  <Tooltip placement={'top'} title={'Зөвхөн латин жижиг үсэг оруулна'}>
                    <InfoCircleOutlined color={'blue'}/>
                  </Tooltip>}
                placeholder="Утга"
              />
            </FormItem>
            <FormItem
              // label="Идэвхтэй эсэх"
              name="active"
              valuePropName="checked"
              initialValue={true}
              // className="mb10"
            >
              <Switch checkedChildren="Идэвхтэй" unCheckedChildren="Идэвхгүй"/>
            </FormItem>
          </Col>
        </Row>
        <FormItem
          label="Орчуулга"
          name="translation"
          rules={[{required: true, message: 'Орчуулга бичнэ үү'}]}
          // className="mb10"
        >
          <Input.TextArea placeholder="Орчуулга"/>
        </FormItem>
        <FormItem style={{marginLeft: 8}}>
          <Button type="primary" size={'small'} onClick={handleAdd}>
            Нэмэх
          </Button>
          <Button style={{marginLeft: 8}} size={'small'} onClick={() => form.resetFields()}>
            Цэвэрлэх
          </Button>
        </FormItem>
        <FormItem
          hidden={true}
          name="nsName"
        >
          <Input/>
        </FormItem>
      </Form>
      <br/>
      <Table
        rowKey="index"
        size="small"
        loading={loading}
        columns={columns}
        dataSource={data}
        pagination={false}
      />
    </Modal>
  )
})

export default LocaleCreate