import {useState} from 'react'
import {Button, Form, Input, Modal, Popconfirm, Switch, Table, message, Tooltip} from 'antd'
import {observer} from 'mobx-react-lite'

import {useStore} from '../../../context/mobxStore'
import {DeleteTwoTone, InfoCircleOutlined} from '@ant-design/icons'

const FormItem = Form.Item

const NameSpaceCreate = observer(({
  modalOpen,
  handleCreate,
  onClose
}) => {

  const [form] = Form.useForm()
  const nameSpaceStore = useStore('nameSpaceStore')
  const loading = nameSpaceStore?.loading
  const [data, setData] = useState([])

  const submitHandle = () => {
    handleCreate(data, form)
  }

  const backHandle = () => {
    form.resetFields()
    onClose()
  }

  const handleDelete = (value) => {
    let newData = data.filter((item) => item.value !== value)
    setData(newData)
  }

  const add = (items) => {
    setData((prevState) => [...prevState, items])
  }

  const handleAdd = () => {
    let duplicated = false
    form.validateFields()
      .then(fieldsValue => {
        data.forEach((item) => {
          if (item.value === fieldsValue.value)
            return duplicated = true
        })
        if (duplicated)
          return message.error('Утга давхцсан байна.')
        else
          add(fieldsValue)
      })
      .catch(errorInfo => {
        console.error('Maroon ! --> ~ file: createModal.js ~ line 54 ~ submitHandle ~ errorInfo', errorInfo)
      })
  }

  const validateValue = (_rule, control, callback) => {
    const regex = /^[a-z]+$/
    if (control) {
      const result = regex.test(control)
      if (result) {
        callback()
      } else
        callback('Утгын формат буруу байна!')
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
      title: 'Нэр',
      dataIndex: 'name',
      align: 'center'
    },
    {
      title: 'Утга',
      dataIndex: 'value',
      align: 'center'
    },
    {
      title: 'Үйлдэл',
      width: '100px',
      render: (_, record) => (
        <>
          <Tooltip placement='top' title='Устгах'>
            <Popconfirm title='Устгах уу ?' onConfirm={() => handleDelete(record?.value)}>
              <Button icon={<DeleteTwoTone/>} style={{color: 'red'}} type='dashed' shape='circle'/>
            </Popconfirm>
          </Tooltip>
        </>
      )
    }]

  return (
    <Modal
      title={'Орчуулгын харяалах байрлал бүртгэх'}
      width={800}
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
        layout={'inline'}
        form={form}
        initialValues={{}}
      >
        <FormItem
          wrapperCol={{span: 15}}
          style={{width: '200px'}}
          label='Нэр'
          name='name'
          rules={[{required: true, message: 'Нэр бичнэ үү'}]}
          // className='mb10'
        >
          <Input placeholder='Нэр'/>
        </FormItem>
        <FormItem
          wrapperCol={{span: 15}}
          style={{width: '200px'}}
          label='Утга'
          name='value'
          rules={[
            {required: true, message: 'Утга'},
            {validator: (rule, control, callback) => validateValue(rule, control, callback)}
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
          // label='Идэвхтэй эсэх'
          name='active'
          valuePropName='checked'
          initialValue={true}
          // className='mb10'
        >
          <Switch checkedChildren='Идэвхтэй' unCheckedChildren='Идэвхгүй'/>
        </FormItem>
        <FormItem style={{marginLeft: 8}}>
          <Button type='primary' size={'small'} onClick={handleAdd}>
            Нэмэх
          </Button>
          <Button style={{marginLeft: 8}} size={'small'} onClick={() => form.resetFields()}>
            Цэвэрлэх
          </Button>
        </FormItem>
      </Form>
      <br/>
      <Table
        rowKey='value'
        size='small'
        loading={loading}
        columns={columns}
        dataSource={data}
        pagination={false}
      />
    </Modal>
  )
})

export default NameSpaceCreate