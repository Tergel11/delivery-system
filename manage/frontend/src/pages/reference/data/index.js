import React, {useEffect, useState} from 'react'
import {Alert, Button, Card, Col, Divider, Form, Input, Popconfirm, Row, Select, Table, Tag, Tooltip} from 'antd'
import {observer} from 'mobx-react-lite'

import ReferenceDataModal from './Createupdate'
import {useStore} from '../../../context/mobxStore'
import dynamic from 'next/dynamic'
import {DeleteOutlined, EditTwoTone, PlusOutlined} from '@ant-design/icons'
import {NextSeo} from 'next-seo'
import {checkAuthRole} from '../../../common/util/auth'
import {useSession} from 'next-auth/react'

const FormItem = Form.Item
const {Option} = Select
const PageHeader = dynamic(() => import('../../../components/elements/pageHeader'))

const ReferenceData = observer(({}) => {
  const [form] = Form.useForm()
  const referenceDataStore = useStore('referenceDataStore')
  const referenceTypeStore = useStore('referenceTypeStore')
  const {data: session = {}} = useSession()
  const {data, loading, searchFormValues} = referenceDataStore
  const {list, pagination} = data
  const [modalOpen, setModalOpen] = useState(false)
  const [editData, setEditData] = useState({})
  const [title, setTitle] = useState('')

  useEffect(() => {
    if (session?.token) {
      referenceTypeStore.fetchSelect(null, session?.token)
      refreshTable(searchFormValues)
    }
  }, [session])

  const refreshTable = (params) => {
    referenceDataStore.fetchList(params, session?.token)
  }

  const handleTableChange = (pagination) => {
    const params = {
      ...searchFormValues,
      currentPage: pagination.current,
      pageSize: pagination.pageSize
    }
    referenceDataStore.setSearchFormValues(params)
    refreshTable(params)
  }

  const handleSearch = () => {
    referenceDataStore.setSearchFormValues(form.getFieldsValue())
    refreshTable(form.getFieldsValue())
  }

  const handleFormReset = () => {
    form.resetFields()
    referenceDataStore.setSearchFormValues({})
    refreshTable()
  }

  const showModal = (action, values) => {
    switch (action) {
      case 'CREATE':
        setEditData({})
        setTitle('бүртгэх')
        setModalOpen(true)
        break
      case 'UPDATE':
        setEditData(values)
        setTitle('засах')
        setModalOpen(true)
        break
      default:
        return ''
    }
  }

  const handleDelete = (record) => {
    referenceDataStore.deleteOne({id: record.id}, session?.token).then((response) => {
      if (response && response.result) {
        refreshTable()
      }
    })
  }

  const renderModal = () => (
    <ReferenceDataModal
      editData={editData}
      open={modalOpen}
      closeModal={() => setModalOpen(false)}
      title={title}
      refreshTable={refreshTable}
    />
  )

  const paginationProps = {
    showSizeChanger: true,
    showQuickJumper: true,
    ...pagination
  }

  const renderFilterForm = () => {
    const makeOptionReferenceType = (item) => (
      <Option key={item.key || item.id} value={item.code}>
        {`${item.name.mn} - ${item.code}`}
      </Option>
    )

    return (
      <>
        <Form form={form} onFinish={handleSearch}>
          <Row gutter={25}>
            <Col span={6}>
              <FormItem label="Лавлах сангийн төрөл" name="typeCode" className="mb10">
                <Select showSearch optionFilterProp="children" allowClear placeholder="Сонгох" style={{width: '100%'}}>
                  {referenceTypeStore?.selectData?.map(makeOptionReferenceType)}
                </Select>
              </FormItem>
            </Col>
            <Col span={6}>
              <FormItem label="Лавлах сангийн нэр" name="name" className="mb10">
                <Input placeholder="Лавлах сангийн нэр"/>
              </FormItem>
            </Col>
            <Col span={2}>
              <FormItem label="Код" name="code" className="mb10">
                <Input placeholder="Код"/>
              </FormItem>
            </Col>
            <Col span={4}>
              <FormItem>
                <Button type="primary" htmlType="submit">
                  Хайх
                </Button>
                <Button style={{marginLeft: 8}} onClick={handleFormReset}>
                  Цэвэрлэх
                </Button>
              </FormItem>
            </Col>
          </Row>
        </Form>
      </>
    )
  }

  const columns = [
    {
      title: 'Лого',
      dataIndex: 'icon',
      render: (text) => text && <img src={text.url} height={50} alt="icon"/>
    },
    {
      title: 'Төрөл',
      dataIndex: 'typeName',
      render: (text) => text?.mn
    },
    {
      title: 'Төрлийн код',
      dataIndex: 'typeCode'
    },
    {
      title: 'Код',
      dataIndex: 'code'
    },
    {
      title: 'Нэр',
      dataIndex: 'name',
      render: (text) => text?.mn
    },
    {
      title: 'Тайлбар',
      dataIndex: 'description',
      render: (text) => text?.mn
    },
    {
      title: 'Эрэмбэ',
      dataIndex: 'order'
    },
    {
      title: 'Ашиглах эсэх',
      dataIndex: 'active',
      render: (text) => (text ? <Tag color="green">Тийм</Tag> : <Tag color="purple">Үгүй</Tag>)
    },
    {
      title: 'Үйлдэл',
      dataIndex: 'operation',
      width: '200px',
      render: (text, record) => {
        return (
          <>
            <Tooltip placement="top" title="Засах">
              <Button
                icon={<EditTwoTone/>}
                onClick={() => showModal('UPDATE', record)}
                style={{color: 'green'}}
                type="dashed"
                shape="circle"
              />
            </Tooltip>
            <Divider type="vertical"/>
            <Tooltip placement="top" title="Устгах">
              <Popconfirm title="Устгах уу ?" onConfirm={() => handleDelete(record)}>
                <Button icon={<DeleteOutlined/>} style={{color: 'red'}} type="dashed" shape="circle"/>
              </Popconfirm>
            </Tooltip>
          </>
        )
      }
    }
  ]

  const headerActions = (
    checkAuthRole('ROLE_REFERENCE_TYPE_MANAGE', session.applicationRoles) ? (
      <Button icon={<PlusOutlined/>} type="primary" onClick={() => showModal('CREATE')}>
        Лавлах сан нэмэх
      </Button>
    ) : '')

  return (
    <>
      <NextSeo title="Хэрэглэгчийн эрх - Astvision Starter"/>
      <PageHeader
        routes={[
          {
            title: 'Нүүр',
            link: '/'
          }
        ]}
        title="Лавлах сангийн жагсаалт"
        action={headerActions}
      />
      <Card>
        {renderFilterForm()}
        <Table
          title={() => (
            <Alert
              message={
                <span style={{marginLeft: 8}}>
                  Нийт:
                  <span style={{fontWeight: 600}}>
                    <a style={{fontWeight: 600, marginLeft: 8}}>{pagination ? pagination.total : 0}</a>{' '}
                  </span>
                </span>
              }
              type="info"
              showIcon
            />
          )}
          loading={loading}
          rowKey="key"
          size="small"
          columns={columns}
          dataSource={list}
          pagination={paginationProps}
          onChange={handleTableChange}
        />
        {modalOpen && renderModal()}
      </Card>
    </>
  )
})
export default ReferenceData
