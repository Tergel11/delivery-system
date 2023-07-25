import {useEffect} from 'react'
import {Form, Input, InputNumber, message, Modal, Select, Switch, Upload} from 'antd'
import {observer} from 'mobx-react-lite'
import {normFile} from '../../../common/util/file'
import {UploadOutlined} from '@ant-design/icons'
import {useStore} from '../../../context/mobxStore'
import {useSession} from 'next-auth/react'
import {getCdnUploadUrl} from '../../../common/base'
import MultiLangField from '../../classification/sys-language/MultiLangField'

const FormItem = Form.Item
const {Option} = Select
const {confirm} = Modal

const ReferenceData = observer(({
  editData,
  open,
  closeModal,
  title,
  refreshTable
}) => {

  const [form] = Form.useForm()
  const {data: session = {}} = useSession()
  const referenceTypeStore = useStore('referenceTypeStore')
  const referenceDataStore = useStore('referenceDataStore')
  const typeList = referenceTypeStore?.selectData
  const loading = referenceTypeStore?.loading

  useEffect(() => {
    if (session?.token) {
      referenceTypeStore.fetchSelect({}, session?.token)
      form.resetFields()
    }
  }, [editData, session])

  const handleCreate = (payload, form) => {
    referenceDataStore.create(payload, session?.token).then((response) => {
      if (response && response.result) {
        message.success('Мэдээлэл амжилттай бүртгэлээ')
        form.resetFields()
        closeModal()
        if (refreshTable)
          refreshTable()
      }
    })

  }

  const handleUpdate = (payload, form) => {
    referenceDataStore.update(payload, session?.token).then((response) => {
      if (response && response.result) {
        message.success('Мэдээлэл амжилттай шинэчиллээ')
        form.resetFields()
        closeModal()
        if (refreshTable)
          refreshTable()
      }
    })
  }

  const createUpdate = (payload, form) => {
    const newPayload = Object.assign({...editData, ...payload})
    if (newPayload?.id) {
      handleUpdate(newPayload, form)
    } else {
      handleCreate(newPayload, form)
    }
  }

  const submitHandle = () => {
    form.validateFields()
      .then(fieldsValue => {
        const {icon, ...rest} = fieldsValue

        let payload = {}
        let correctIcon = {}
        if (icon && icon.length !== 0) {
          icon.map(item => {
            if (item.response && item.status === 'done') {
              correctIcon = {
                uid: item.uid,
                name: item.name,
                url: item.response.url
              }
            } else {
              correctIcon = {}
            }
          })
          payload = Object.assign({icon: correctIcon})
        }

        payload = Object.assign(payload, {...rest})
        createUpdate(payload, form)
      })
      .catch(errorInfo => {
        console.error('Maroonj4f ! --> ~ file: createUpdateModal.js ~ line 102 ~ submitHandle ~ errorInfo', errorInfo)
      })
  }

  const backHandle = () => {
    form.resetFields()
    closeModal()
  }

  const showBackConfirm = () => {
    if (form.isFieldsTouched()) {
      confirm({
        title: 'Та гарахдаа итгэлтэй байна уу?',
        content: '',
        okText: 'Тийм',
        okType: 'danger',
        cancelText: 'Үгүй',
        onOk() {
          backHandle()
        },
        onCancel() {
        }
      })
    } else {
      backHandle()
    }
  }

  const beforeUpload = (file, files) => {
    const isPDF = file.type === 'application/pdf'
    if (!isPDF) {
      files.splice(0, 1)
      message.error('Зөвхөн PDF файл оруулна уу!')
    }
    return isPDF
  }

  const makeOptionReferenceType = item => (
    <Option key={item.key || item.id} value={item.code}>
      {`${item.name.mn} - ${item.code}`}
    </Option>
  )

  const {icon, name, description, ...initialValues} = editData

  return (
    <Modal
      title={`Лавлах сан ${title}`}
      width={800}
      open={open}
      onOk={() => submitHandle()}
      onCancel={backHandle}
      okText='Хадгалах'
      cancelText='Болих'
      confirmLoading={loading}
      forceRender
    >
      <br/>
      <Form
        layout={'vertical'}
        form={form}
        initialValues={initialValues}
      >
        <FormItem
          label='Лавлах сангийн төрөл'
          name='typeCode'
          rules={[{required: true, message: 'Лавлах сангийн төрөл сонгоно уу'}]}
          className='mb10'
        >
          <Select
            showSearch
            optionFilterProp='children'
            allowClear
            // onChange={handleChangeType}
            placeholder='Сонгох'
            style={{width: '100%'}}
          >
            {typeList?.map(makeOptionReferenceType)}
          </Select>
        </FormItem>
        <FormItem
          label='Лавлах сангийн нэр'
          name='name'
          initialValue={name || {mn: ''}}
          rules={[{required: true, message: 'Лавлах сангийн нэр бичнэ үү'}]}
          className='mb10'
          valuePropName={'value'}
        >
          <MultiLangField
            form={form}
            name={'нэр'}
            token={session?.token}
            type={'Input'}
            required={true}
            formItemName={'name'}
          />
        </FormItem>
        <FormItem
          label='Код'
          name='code'
          rules={[{required: true, message: 'Код бичнэ үү!'}]}
          className='mb10'
        >
          <Input placeholder='Код'/>
        </FormItem>
        <FormItem
          initialValue={description || {mn: ''}}
          label='Тайлбар'
          name='description'
          className='mb10'
        >
          <MultiLangField
            form={form}
            name={'тайлбар'}
            token={session?.token}
            type={'TextArea'}
            formItemName={'description'}
          />
        </FormItem>
        <FormItem
          label='Эрэмбэ'
          name='order'
          rules={[{required: true, message: 'Эрэмбэ оруулна уу!'}]}
          className='mb10'
        >
          <InputNumber formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')} min={1} step={1} placeholder='Дугаар'/>
        </FormItem>
        <FormItem
          label='Ашиглах эсэх'
          name='active'
          valuePropName='checked'
          className='mb10'
        >
          <Switch checkedChildren='Тийм' unCheckedChildren='Үгүй'/>
        </FormItem>

        <FormItem
          noStyle
          shouldUpdate={(prevValues, curValues) => prevValues.icon !== curValues.icon}
        >
          {({getFieldValue}) => {
            return <>
              <FormItem
                label='Зураг'
                name='icon'
                valuePropName='fileList'
                getValueFromEvent={normFile}
                initialValue={icon && [icon] || []}
                className='mb10'
              >
                <Upload
                  name='file'
                  accept='image/*'
                  listType='picture-card'
                  headers={{'X-Auth-Token': session?.token}}
                  data={{entity: 'referenceIcon', entityId: Math.random().toString(36).substring(2)}}
                  action={getCdnUploadUrl()}
                  // beforeUpload={beforeUpload}
                >
                  {getFieldValue('icon') && getFieldValue('icon').length > 0 ? null :
                    <UploadOutlined/>
                  }
                </Upload>
              </FormItem>
            </>
          }}
        </FormItem>
      </Form>
    </Modal>
  )
})
export default (ReferenceData)