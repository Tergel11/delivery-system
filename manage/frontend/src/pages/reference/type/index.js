import React, {useEffect, useState} from 'react'
import {Alert, Button, Card, Divider, Form, Input, message, Popconfirm, Table, Tag, Tooltip} from 'antd'
import {observer} from 'mobx-react-lite'

import CreateUpdateModal from './CreateUpdate'
import dynamic from 'next/dynamic'
import {useStore} from '../../../context/mobxStore'
import {DeleteTwoTone, EditTwoTone, PlusOutlined} from '@ant-design/icons'
import {checkAuthRole} from '../../../common/util/auth'
import {useSession} from 'next-auth/react'
import {NextSeo} from 'next-seo'

const FormItem = Form.Item
const PageHeader = dynamic(() => import('../../../components/elements/pageHeader'))

const ReferenceType = observer(() => {
  const [form] = Form.useForm()
  const {data: session = {}} = useSession()
  const referenceTypeStore = useStore('referenceTypeStore')
  const {data, loading, searchFormValues} = referenceTypeStore
  const {list, pagination} = data
  const [modalOpen, setModalOpen] = useState(false)
  const [editData, setEditData] = useState({})

  useEffect(() => {
    if (session?.token) {
      refreshTable()
    }
  }, [session])

  const refreshTable = (params) => {
    referenceTypeStore.fetchList(params, session?.token)
  }

  const handleTableChange = (pagination) => {
    const params = {
      ...searchFormValues,
      currentPage: pagination.current,
      pageSize: pagination.pageSize
    }

    referenceTypeStore.setSearchFormValues(params)
    refreshTable(params)
  }

  const handleFormReset = () => {
    form.resetFields()
    referenceTypeStore.setSearchFormValues({})
    refreshTable()
  }

  const handleSearch = () => {
    referenceTypeStore.setSearchFormValues(form.getFieldsValue())
    refreshTable(form.getFieldsValue())
  }

  const showModal = (action, values) => {
    switch (action) {
      case 'CREATE':
        setEditData({})
        break
      case 'UPDATE':
        setEditData(values)
        break
      default:
        return ''
    }
    setModalOpen(true)
  }

  const handleCreate = (fields, form) => {
    referenceTypeStore.create(fields, session?.token).then(response => {
      if (response.result === true && response.data) {
        message.success('Төрөл амжилттай бүртгэлээ')
        form.resetFields()
        setModalOpen(false)
        refreshTable()
      } else {
        message.error(`Төрөл бүртгэхэд алдаа гарлаа: ${response.message}`)
      }
    }).catch(e => {
      console.log(e)
      message.error(`Төрөл бүртгэхэд алдаа гарлаа: ${e.message}`)
    })
  }

  const handleUpdate = (fields, form) => {
    referenceTypeStore.update(Object.assign({id: editData.id}, fields), session?.token)
      .then(response => {
        if (response.result === true && response.data) {
          message.success('Төрөл амжилттай хадгаллаа')
          form.resetFields()
          setModalOpen(false)
          setEditData({})
          refreshTable()
        } else {
          message.error(`Төрөл засварлахад алдаа гарлаа: ${response.message}`)
        }
      })
      .catch(e => {
        console.log(e)
        message.error(`Төрөл засварлахад алдаа гарлаа: ${e.message}`)
      })
  }

  const handleDelete = record => {
    referenceTypeStore.deleteOne({id: record.id}, session?.token)
      .then(response => {
        if (response.result === true && response.data) {
          message.success('Төрөл амжилттай устгалаа')
          handleFormReset()
        } else {
          message.error(`Төрөл устгахад алдаа гарлаа: ${response.message}`)
        }
      })
      .catch(e => {
        console.log(e)
        message.error(`Төрөл устгахад алдаа гарлаа: ${e.message}`)
      })
  }

  const columns = [
    {
      title: 'Лого',
      dataIndex: 'icon',
      width: '250px',
      render: (text) => text && <img src={text.url} height={50} alt="icon"/>
    },
    {
      title: 'Нэр',
      dataIndex: 'name',
      width: '250px',
      render: (text) => text?.mn
    }, {
      title: 'Код',
      dataIndex: 'code',
      width: '250px'
    }, {
      title: 'Тайлбар',
      dataIndex: 'description',
      render: (text) => text?.mn
    }, {
      title: 'Идэвхтэй эсэх',
      dataIndex: 'active',
      width: '150px',
      render: text => text ? <Tag color="green">Тийм</Tag> : <Tag color="purple">Үгүй</Tag>
    }, {
      title: 'Үйлдэл',
      width: '200px',
      render: (text, record) => (
        //
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
              <Button icon={<DeleteTwoTone/>} style={{color: 'red'}} type="dashed" shape="circle"/>
            </Popconfirm>
          </Tooltip>
        </>
      )
    }]

  const headerActions = (
    checkAuthRole('ROLE_REFERENCE_TYPE_MANAGE', session.applicationRoles) ? (
      <Button icon={<PlusOutlined/>} type="primary" onClick={() => showModal('CREATE')}>
        Бүртгэх
      </Button>
    ) : '')

  const renderFilterForm = () => (
    <Form form={form} onFinish={handleSearch} layout="inline">
      <FormItem
        label="Нэр"
        name="name"
        className="mb10"
      >
        <Input placeholder="Нэр"/>
      </FormItem>
      <FormItem>
        <Button type="primary" htmlType="submit">
          Хайх
        </Button>
        <Button style={{marginLeft: 8}} onClick={handleFormReset}>
          Цэвэрлэх
        </Button>
      </FormItem>
    </Form>
  )

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
        title="Лавлах сангийн төрөл"
        action={headerActions}
      />
      <Card bordered={false}>
        {renderFilterForm()}
        <br/>
        <Table
          rowKey="id"
          size="small"
          loading={loading}
          columns={columns}
          dataSource={list || []}
          pagination={pagination && {...pagination, showQuickJumper: true, showSizeChanger: true} || []}
          onChange={handleTableChange}
          title={
            () => <Alert
              message={
                <span style={{marginLeft: 8}}>
                      Нийт
                      <span style={{fontWeight: 600}}>
                        <a style={{fontWeight: 600, marginLeft: '8px'}}>{pagination ? pagination.total : 0}</a>{' '}
                      </span>
                    </span>
              }
              type="info"
              showIcon
            />
          }
        />
        {modalOpen &&
          <CreateUpdateModal
            modalOpen={modalOpen}
            handleCreate={handleCreate}
            handleUpdate={handleUpdate}
            onClose={() => setModalOpen(false)}
            editData={editData}
          />
        }
      </Card>
    </>
  )
})

export default (ReferenceType)
