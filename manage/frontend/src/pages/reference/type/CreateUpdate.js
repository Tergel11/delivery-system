import React, {useEffect} from 'react'
import {Form, Input, message, Modal, Switch, Upload} from 'antd'
import {observer} from 'mobx-react-lite'

import {UploadOutlined} from '@ant-design/icons'
import {normFile} from '../../../common/util/file'
import {useStore} from '../../../context/mobxStore'
import {useSession} from 'next-auth/react'
import {getCdnUploadUrl} from '../../../common/base'
import MultiLangField from '../../classification/sys-language/MultiLangField'

const FormItem = Form.Item

const ReferenceType = observer(({
  modalOpen,
  handleUpdate,
  handleCreate,
  editData,
  onClose
}) => {

  const [form] = Form.useForm()
  const referenceTypeStore = useStore('referenceTypeStore')
  const {data: session = {}} = useSession()
  const loading = referenceTypeStore?.loading
  const isCreate = !editData?.id

  useEffect(() => {
    form.resetFields()
  }, [editData])

  const submitHandle = () => {
    form.validateFields()
      .then(fieldsValue => {
        const {icon, ...rest} = fieldsValue
        let typeIcon

        if (icon && icon.length !== 0) {
          icon.map(item => {
            if (item.response && item.status === 'done') {
              typeIcon = {
                uid: item.uid,
                name: item.name,
                url: item.response.url
              }
            } else {
              typeIcon = {}
            }
          })
        }
        createUpdate(Object.assign(fieldsValue, {icon: typeIcon}))
      })
      .catch(errorInfo => {
        console.error('maroon ! --> ~ file: createUpdateModal.js ~ line 54 ~ submitHandle ~ errorInfo', errorInfo)
      })
  }

  const createUpdate = (payload) => {
    const newPayload = Object.assign({...editData, ...payload})
    if (newPayload?.id) {
      handleUpdate(newPayload, form)
    } else {
      handleCreate(newPayload, form)
    }
  }

  const backHandle = () => {
    form.resetFields()
    onClose()
  }

  const beforeUpload = (file, contentImage) => {
    const isImg = file.type.startsWith('image/')
    if (!isImg) {
      contentImage.splice(0, 1)
      message.error('Зөвхөн зураг оруулна уу!')
    }
    return isImg
  }

  const validateCode = (_rule, control, callback) => {
    const regex = /^[\d+A-Z_]*|$/
    if (control) {
      const result = control.match(regex)
      if (control && (control === result[0])) {
        callback()
      } else
        callback('Төрлийн код буруу байна!')
    } else
      callback()
  }

  const {icon, ...initialValues} = editData

  return (
    <Modal
      title={`Лавлах сангийн төрөл ${isCreate ? 'бүртгэх' : 'засварлах'}`}
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
        form={form}
        layout={'vertical'}
        initialValues={initialValues}
      >
        <FormItem
          label="Нэр"
          name="name"
          initialValue={{mn: ''}}
          rules={[{required: true, message: 'Төрлийн нэр бичнэ үү'}]}
          className="mb10"
        >
          <MultiLangField
            form={form}
            placeholder="Нэр"
            name={'нэр'}
            token={session?.token}
            type={'Input'}
            required={true}
            formItemName={'name'}
          />
        </FormItem>
        <FormItem
          label="Код"
          name="code"
          rules={[
            {required: true, message: 'Төрлийн нэр бичнэ үү'},
            {validator: (rule, control, callback) => validateCode(rule, control, callback)}
          ]}
          extra="Зөвхөн латин том үсэг болон доогуур зураас тооноос бүрдэнэ"
          className="mb10"
        >
          <Input disabled={!isCreate} placeholder="Код"/>
        </FormItem>
        <FormItem
          label="Тайлбар"
          name="description"
          initialValue={{mn: ''}}
          className="mb10"
        >
          <MultiLangField
            form={form}
            placeholder="Тайлбар"
            name={'Тайлбар'}
            token={session?.token}
            type={'TextArea'}
            formItemName={'description'}
          />
        </FormItem>

        <FormItem
          noStyle
          shouldUpdate={(prevValues, curValues) => prevValues.icon !== curValues.icon}
        >
          {({getFieldValue}) => {
            return <>
              <FormItem
                label="Зураг"
                name="icon"
                valuePropName="fileList"
                getValueFromEvent={normFile}
                initialValue={icon && [icon] || []}
                className="mb10"
              >
                <Upload
                  name="file"
                  accept="image/*"
                  listType="picture-card"
                  headers={{'X-Auth-Token': session?.token}}
                  data={{entity: 'referenceTypeIcon', entityId: Math.random().toString(36).substring(2)}}
                  action={getCdnUploadUrl()}
                  beforeUpload={beforeUpload}
                >
                  {getFieldValue('icon') && getFieldValue('icon').length > 0 ? null :
                    <UploadOutlined/>
                  }
                </Upload>
              </FormItem>
            </>
          }}
        </FormItem>

        <FormItem
          label="Идэвхтэй эсэх"
          name="active"
          valuePropName="checked"
          className="mb10"
        >
          <Switch checkedChildren="Тийм" unCheckedChildren="Үгүй"/>
        </FormItem>
      </Form>
    </Modal>
  )
})

export default ReferenceType