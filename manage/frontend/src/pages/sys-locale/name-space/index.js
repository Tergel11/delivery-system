import React, {useEffect, useState} from 'react'
import {Alert, Button, Card, Divider, Form, Input, message, Popconfirm, Table, Tag, Tooltip} from 'antd'
import {observer} from 'mobx-react-lite'

import Create from './Create'
import dynamic from 'next/dynamic'
import {useStore} from '../../../context/mobxStore'
import {DeleteTwoTone, EditTwoTone, PlusOutlined} from '@ant-design/icons'
import {checkAuthRole} from '../../../common/util/auth'
import {useSession} from 'next-auth/react'
import {NextSeo} from 'next-seo'
import Update from './Update'

const FormItem = Form.Item
const PageHeader = dynamic(() => import('../../../components/elements/pageHeader'))

const NameSpace = observer(() => {
  const [form] = Form.useForm()
  const {data: session = {}} = useSession()
  const nameSpaceStore = useStore('nameSpaceStore')
  const {data, loading, searchFormValues} = nameSpaceStore
  const {list, pagination} = data
  const [createOpen, setCreateOpen] = useState(false)
  const [updateOpen, setUpdateOpen] = useState(false)
  const [editData, setEditData] = useState({})
  const canManage = checkAuthRole('ROLE_LOCALE_MANAGE', session?.applicationRoles)

  useEffect(() => {
    if (session?.token) {
      refreshTable()
    }
  }, [session])

  const refreshTable = (params) => {
    nameSpaceStore.fetchList(params, session?.token)
  }

  const handleTableChange = (pagination) => {
    const params = {
      ...searchFormValues,
      currentPage: pagination.current,
      pageSize: pagination.pageSize
    }

    nameSpaceStore.setSearchFormValues(params)
    refreshTable(params)
  }

  const handleFormReset = () => {
    form.resetFields()
    nameSpaceStore.setSearchFormValues({})
    refreshTable()
  }

  const handleSearch = () => {
    nameSpaceStore.setSearchFormValues(form.getFieldsValue())
    refreshTable(form.getFieldsValue())
  }

  const showModal = (action, values) => {
    switch (action) {
      case 'CREATE':
        setEditData({})
        setCreateOpen(true)
        break
      case 'UPDATE':
        setEditData(values)
        setUpdateOpen(true)
        break
      default:
        return ''
    }
  }

  const handleCreate = (fields, form) => {
    nameSpaceStore.create(fields, session?.token).then(response => {
      if (response.result === true && response.data) {
        message.success('Name space амжилттай бүртгэлээ')
        form.resetFields()
        setCreateOpen(false)
        refreshTable()
      } else {
        message.error(`Name space бүртгэхэд алдаа гарлаа: ${response.message}`)
      }
    }).catch(e => {
      console.log(e)
      message.error(`Name space бүртгэхэд алдаа гарлаа: ${e.message}`)
    })
  }

  const handleUpdate = (fields, form) => {
    nameSpaceStore.update(Object.assign({id: editData.id}, fields), session?.token)
      .then(response => {
        if (response.result === true && response.data) {
          message.success('Name space амжилттай хадгаллаа')
          form.resetFields()
          setUpdateOpen(false)
          setEditData({})
          refreshTable()
        } else {
          message.error(`Name space засварлахад алдаа гарлаа: ${response.message}`)
        }
      })
      .catch(e => {
        console.log(e)
        message.error(`Name space засварлахад алдаа гарлаа: ${e.message}`)
      })
  }

  const handleDelete = record => {
    nameSpaceStore.deleteOne({id: record.id}, session?.token)
      .then(response => {
        if (response.result === true && response.data) {
          message.success('Name space амжилттай устгалаа')
          handleFormReset()
        } else {
          message.error(`Name space устгахад алдаа гарлаа: ${response.message}`)
        }
      })
      .catch(e => {
        console.log(e)
        message.error(`Name space устгахад алдаа гарлаа: ${e.message}`)
      })
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
      render: text => <Tag color="blue">{text}</Tag>
    },
    {
      title: 'Идэвхтэй эсэх',
      dataIndex: 'active',
      width: '150px',
      render: text => text ? <Tag color="green">Тийм</Tag> : <Tag color="purple">Үгүй</Tag>
    },
    {
      title: 'Үйлдэл',
      width: '200px',
      render: (text, record) => (
        //
        <>
          {canManage &&
            <>
              <Tooltip placement="top" title="Засах">
                <Button
                  icon={<EditTwoTone />}
                  onClick={() => showModal('UPDATE', record)}
                  style={{color: 'green'}}
                  type="dashed"
                  shape="circle"
                />
              </Tooltip>
              <Divider type="vertical" />
              <Tooltip placement="top" title="Устгах">
                <Popconfirm title="Устгах уу ?" onConfirm={() => handleDelete(record)}>
                  <Button icon={<DeleteTwoTone />} style={{color: 'red'}} type="dashed" shape="circle" />
                </Popconfirm>
              </Tooltip>
            </>
          }
        </>
      )
    }]

  const headerActions = (
    canManage ? (
      <Button icon={<PlusOutlined />} type="primary" onClick={() => showModal('CREATE')}>
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
        <Input placeholder="Нэр" />
      </FormItem>
      <FormItem
        label="Утга"
        name="value"
        className="mb10"
      >
        <Input placeholder="Утга" />
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
      <NextSeo title="Хэрэглэгчийн эрх - Astvision Starter" />
      <PageHeader
        routes={[
          {
            title: 'Нүүр',
            link: '/'
          }
        ]}
        title="Name space"
        action={headerActions}
      />
      <Card bordered={false}>
        {renderFilterForm()}
        <br />
        <Table
          rowKey="key"
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
        {createOpen &&
          <Create
            modalOpen={createOpen}
            handleCreate={handleCreate}
            onClose={() => setCreateOpen(false)}
          />
        }
        {updateOpen ?
          <Update
            modalOpen={updateOpen}
            handleUpdate={handleUpdate}
            onClose={() => setUpdateOpen(false)
              // console.log('onClose ')
            }
            editData={editData}
          /> : null
        }
      </Card>
    </>
  )
})

export default (NameSpace)
