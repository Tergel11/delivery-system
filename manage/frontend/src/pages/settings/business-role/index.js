import {useEffect, useState} from 'react'
import dynamic from 'next/dynamic'
import {useSession} from 'next-auth/react'
import {NextSeo} from 'next-seo'
import {observer} from 'mobx-react-lite'
import {
  Alert,
  Button,
  Card,
  Divider,
  Form,
  Input,
  Modal,
  message,
  Select,
  Table,
  Tag,
} from 'antd'
import {
  DeleteOutlined,
  EditOutlined,
  PlusOutlined,
} from '@ant-design/icons'

import {useStore} from '../../../context/mobxStore'
import {deleteOne} from '../../../common/services/auth/businessRole'
import CreateUpdateModal from './createUpdate'

const PageHeader = dynamic(() => import('../../../components/elements/pageHeader'))

const BusinessRoleList = observer(() => {
  const {data: session = {}} = useSession()
  const businessRoleStore = useStore('businessRoleStore')
  const [form] = Form.useForm()
  const [searchFormValues, setSearchFormValues] = useState({})
  const [updateModalVisible, setUpdateModalVisible] = useState(false)
  const [updateData, setUpdateData] = useState({})

  useEffect(() => {
    if (session?.token) {
      businessRoleStore.fetchApplicationRoles(session.token)
      refreshTable()
    }
  }, [session])

  const refreshTable = (params) => {
    businessRoleStore.fetchSelect(session?.token, params)
    businessRoleStore.fetchList(session?.token, params)
  }

  const handleSearch = () => {
    setSearchFormValues(form.getFieldsValue())
    refreshTable(form.getFieldsValue())
  }

  const handleSearchFormReset = () => {
    form.resetFields()
    setSearchFormValues({})
    refreshTable()
  }

  const handleTableChange = (pagination, _filtersArg, _sorter) => {
    const params = {
      currentPage: pagination.current,
      pageSize: pagination.pageSize,
      ...searchFormValues,
    }
    // if (sorter.field) {
    //   params.sorter = `${sorter.field}_${sorter.order}`
    // }
    refreshTable(params)
  }

  const renderFilterForm = () => {
    return (
      <Form
        form={form}
        layout='inline'
        onFinish={handleSearch}
      >
        <Form.Item
          label='Цахим ажлын байр'
          name='role'
        >
          <Input placeholder='Цахим ажлын байр'/>
        </Form.Item>
        <Form.Item
          label='Хандах эрхүүд'
          name='applicationRoles'
        >
          <Select style={{width: 300}}>
            <Select.Option key='' value=''>Бүгд</Select.Option>
            {
              businessRoleStore.applicationRoles && businessRoleStore.applicationRoles
                .map(appRole =>
                  <Select.Option key={appRole} value={appRole}>{appRole}</Select.Option>
                )
            }
          </Select>
        </Form.Item>
        <Form.Item>
          <Button
            type='primary'
            htmlType='submit'>
            Хайх
          </Button>
        </Form.Item>
        <Form.Item>
          <Button
            onClick={handleSearchFormReset}>
            Цэвэрлэх
          </Button>
        </Form.Item>
      </Form>
    )
  }

  const columns = [
    {
      title: 'Нэр',
      dataIndex: 'name',
    },
    {
      title: 'Хэрэглэгчийн эрх',
      dataIndex: 'role',
    },
    {
      title: 'Хандах эрхүүд',
      dataIndex: 'applicationRoles',
      render: (text, record) => (
        <span>
          {
            record.applicationRoles != null
              ? record.applicationRoles.map(appRole =>
                <Tag color='blue' key={`${record.role}_${appRole}`}>{appRole}</Tag>,
              ) : ''
          }
        </span>
      ),
    },
    {
      title: 'Үйлдэл',
      render: (text, record) => (
        <>
          <Button type='link' icon={<EditOutlined/>} onClick={() => showModal(record)}/>
          <Divider type='vertical'/>
          <Button type='link' icon={<DeleteOutlined/>} onClick={() => showDeleteConfirm(record.key)}/>
        </>
      ),
      width: 160,
      align: 'center'
    },
  ]

  const showModal = (values) => {
    setUpdateData(values)
    setUpdateModalVisible(true)
  }

  const closeModal = (refresh) => {
    setUpdateModalVisible(false)
    setUpdateData({})
    if (refresh)
      handleSearch()
  }

  const showDeleteConfirm = clickedId => {
    const parentMethods = {handleDelete: handleDelete}

    Modal.confirm({
      title: 'Анхааруулга',
      content: 'Хэрэглэгчийн эрхийг устгах уу? Тухайн төрөлд хамаарах хэрэглэгчид системд нэвтрэх боломжгүй болно',
      okText: 'Тийм',
      okType: 'danger',
      cancelText: 'Үгүй',
      onOk() {
        parentMethods.handleDelete(clickedId)
      },
    })
  }

  const handleDelete = id => {
    deleteOne(session.token, {id})
      .then(response => {
        if (!response.result)
          message.error(`Хэрэглэгчийн эрх устгахад алдаа гарлаа: ${response.message}`)
        refreshTable(searchFormValues)
      })
      .catch(e => {
        message.error(`Хэрэглэгчийн эрх устгахад алдаа гарлаа: ${e.message}`)
      })
  }

  const headerActions = (
    <Button icon={<PlusOutlined/>} type='primary' onClick={() => showModal({})}>
      Бүртгэх
    </Button>
  )

  return (
    <>
      <NextSeo title='Хэрэглэгчийн эрх - Astvision Starter'/>
      <PageHeader
        routes={[
          {
            title: 'Нүүр',
            link: '/',
          }
        ]}
        title='Хэрэглэгчийн эрх'
        action={headerActions}
      />
      <Card bordered={false}>
        {renderFilterForm()}
        <br/>
        <Alert
          message={
            <p style={{marginBottom: 0}}>
              Нийт тоо:{' '}
              <b>{(businessRoleStore.data
                && businessRoleStore.data.pagination
                && businessRoleStore.data.pagination.total) || '-'}</b>
            </p>
          }
          type='info'
        />
        <br/>
        <Table
          rowKey='key'
          bordered
          loading={businessRoleStore.loading}
          columns={columns}
          dataSource={businessRoleStore.data && businessRoleStore.data.list || []}
          pagination={businessRoleStore.data && businessRoleStore.data.pagination || {}}
          onChange={handleTableChange}
        />
      </Card>
      <CreateUpdateModal
        visible={updateModalVisible}
        updateData={updateData}
        closeModal={closeModal}
      />
    </>
  )
})

export default BusinessRoleList
