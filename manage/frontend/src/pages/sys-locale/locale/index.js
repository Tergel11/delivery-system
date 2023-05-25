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
import SelectNameSpace from '../name-space/SelectNameSpace'
import LanguageSelect from '../../classification/sys-language/LanguageSelect'

const FormItem = Form.Item
const PageHeader = dynamic(() => import('../../../components/elements/pageHeader'))

const LocaleList = observer(() => {
  const [form] = Form.useForm()
  const {data: session = {}} = useSession()
  const localeStore = useStore('localeStore')
  const {data, loading, searchFormValues} = localeStore
  const {list, pagination} = data
  const [createOpen, setCreateOpen] = useState(false)
  const [updateOpen, setUpdateOpen] = useState(false)
  const [editData, setEditData] = useState({})
  const canManage = checkAuthRole('ROLE_LOCALE_MANAGE', session.applicationRoles)

  useEffect(() => {
    if (session?.token) {
      refreshTable()
    }
  }, [session])

  const refreshTable = (params) => {
    localeStore.fetchList(params, session?.token)
  }

  const handleTableChange = (pagination) => {
    const params = {
      ...searchFormValues,
      currentPage: pagination.current,
      pageSize: pagination.pageSize
    }

    localeStore.setSearchFormValues(params)
    refreshTable(params)
  }

  const handleFormReset = () => {
    form.resetFields()
    localeStore.setSearchFormValues({})
    refreshTable()
  }

  const handleSearch = () => {
    localeStore.setSearchFormValues(form.getFieldsValue())
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
    localeStore.create(fields, session?.token).then(response => {
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
    localeStore.update(Object.assign({id: editData.id}, fields), session?.token)
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
    localeStore.deleteOne({id: record.id}, session?.token)
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
      title: 'Байршил',
      dataIndex: 'nsName',
      align: 'center'
    },
    {
      title: 'Хэл',
      dataIndex: 'lng',
      render: (_, record) =>
        <div>
          <span>Нэр: {record.lngName}</span><br/>
          <span>Код: {record.lng}</span>
        </div>
    },
    {
      title: 'Орчуулга',
      dataIndex: 'translation',
      align: 'center'
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
          }
        </>
      )
    }]

  const headerActions = (
    canManage ? (
      <Button icon={<PlusOutlined/>} type="primary" onClick={() => showModal('CREATE')}>
        Бүртгэх
      </Button>
    ) : '')

  const renderFilterForm = () => (
    <Form form={form} onFinish={handleSearch} layout="inline">
      <FormItem
        label="Байршил"
        name="nsId"
        className="mb10"
      >
        <SelectNameSpace placeholder="Сонгох" token={session?.token}/>
      </FormItem>
      <FormItem
        label="Хэл"
        name="lng"
        className="mb10"
      >
        <LanguageSelect defaultValue={null} placeholder="Сонгох" token={session?.token}/>
      </FormItem>
      <FormItem
        label="Талбарын нэр"
        name="field"
        className="mb10"
      >
        <Input placeholder="Талбар"/>
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
        title="Орчуулгын мэдээлэл"
        action={headerActions}
      />
      <Card bordered={false}>
        {renderFilterForm()}
        <br/>
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

export default (LocaleList)
