<template>
  <div class="m-3">
    <Row :gutter="[16, 16]">
      <Col :span="7">
        <Card
          title="数据库连接信息"
          size="small"
          :bodyStyle="{ 'overflow-y': 'auto', height: 'calc(100vh - 150px)' }"
        >
          <template #extra>
            <CloudServerOutlined style="font-size: 16px" title="创建新连接" @click="handleAddDb" />
          </template>
          <a-tree
            ref="asyncTreeRef"
            v-model:expandedKeys="expandedKeys"
            :treeData="treeData"
            :load-data="onLoadData"
            :loadedKeys="loadedKeys"
            @select="handleSelect"
          >
            <template #title="{ title, type, key, data }">
              <a-dropdown :trigger="['contextmenu']">
                <span>
                  <DatabaseOutlined v-if="type === 'db'" style="font-size: 16px" class="mr-2" />
                  <TableOutlined
                    v-if="type === 'table' || type === 'view_table'"
                    style="font-size: 16px"
                    class="mr-2"
                  />
                  <ContainerOutlined
                    v-if="type === 'proc' || type === 'view_proc' || type === 'data_set'"
                    style="font-size: 16px"
                    class="mr-2"
                  />
                  <EyeOutlined v-if="type === 'view'" style="font-size: 16px" class="mr-2" />
                  {{ title }}
                </span>
                <template #overlay>
                  <a-menu
                    v-if="type === 'db'"
                    @click="({ key: menuKey }) => onContextMenuClickDB(key, menuKey, data)"
                  >
                    <a-menu-item key="1">编辑连接</a-menu-item>
                    <a-menu-item key="2">查看属性</a-menu-item>
                    <a-menu-item key="3">创建新表</a-menu-item>
                    <a-menu-item key="4">删除连接</a-menu-item>
                  </a-menu>
                  <a-menu
                    v-else-if="type === 'table'"
                    @click="({ key: menuKey }) => onContextMenuClickTable(key, menuKey, data)"
                  >
                    <a-menu-item key="1">重命名</a-menu-item>
                    <a-menu-item key="2">删除当前表</a-menu-item>
                    <a-menu-item key="3">清空当前表数据</a-menu-item>
                  </a-menu>
                </template>
              </a-dropdown>
            </template>
          </a-tree>
        </Card>
      </Col>
      <Col :span="17">
        <Card size="small">
          <a-tabs>
            <a-tab-pane key="1" tab="对象浏览器">
              <ColumnList :table="treeCurrentData" />
            </a-tab-pane>
            <a-tab-pane key="2" tab="数据浏览器">
              <TableDataList :table="treeCurrentData" />
            </a-tab-pane>
            <a-tab-pane key="3" tab="SQL编辑器">
              <SQL :table="treeCurrentData" />
            </a-tab-pane>
          </a-tabs>
        </Card>
      </Col>
    </Row>
    <DbconsoleModal @register="registerModal" @success="handleSuccess" />
    <DbTabelModal @register="registerDbTabelModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { onMounted, ref } from 'vue';
  import { Row, Col, Card, Dropdown as ADropdown, Menu as AMenu, message } from 'ant-design-vue';
  import { TreeItem, useModal } from '@mxpio/components';
  import DbconsoleModal from './DbconsoleModal.vue';
  import DbTabelModal from './DbTabelModal.vue';
  import ColumnList from './ColumnList.vue';
  import TableDataList from './TableDataList.vue';
  import SQL from './SQL.vue';

  import {
    DatabaseOutlined,
    TableOutlined,
    ContainerOutlined,
    EyeOutlined,
    CloudServerOutlined,
  } from '@ant-design/icons-vue';

  import {
    dbList,
    tableList,
    procList,
    deleteDB,
    deleteTable,
    deleteTableData,
  } from '../../api/dbconsole';

  const AMenuItem = AMenu.Item;
  const [registerModal, { openModal }] = useModal();
  const [registerDbTabelModal, { openModal: openDbTabelModal }] = useModal();

  let treeData = ref<TreeItem[]>([]);
  const expandedKeys = ref<string[]>([]);
  const loadedKeys = ref<string[]>([]);
  const treeCurrentData = ref<Recordable>();

  function onContextMenuClickDB(treeKey: string, menuKey, data: Recordable) {
    switch (menuKey) {
      case '1':
        openModal(true, {
          isUpdate: true,
          record: data.data,
        });
        break;
      case '2':
        openModal(true, {
          isUpdate: true,
          disabled: true,
          record: data.data,
        });
        break;
      case '3':
        openDbTabelModal(true, {
          isUpdate: false,
          record: data.data,
        });
        break;
      case '4':
        deleteDB(data.data.id).then(() => {
          message.success('操作成功');
          getTreeData();
        });
        break;
      default:
        break;
    }
  }
  function onContextMenuClickTable(id: string, menuKey, data: Recordable) {
    switch (menuKey) {
      case '1':
        openDbTabelModal(true, {
          isUpdate: true,
          record: {
            id: data.data.dbInfoId,
            tableName: data.data.tableName,
          },
        });
        break;
      case '2':
        deleteTable(data.data.dbInfoId, data.data.tableName).then(() => {
          message.success('操作成功');
          getTreeData();
        });
        break;
      case '3':
        deleteTableData(data.data.dbInfoId, data.data.tableName).then(() => {
          message.success('清空成功');
        });
        break;
      default:
        break;
    }
  }

  // 获取数据库连接信息
  const getTreeData = () => {
    dbList().then((res) => {
      treeData.value = res.map((item: any) => {
        return {
          key: item.id,
          title: item.name,
          type: 'db',
          data: item,
          children: [
            {
              key: 'table' + item.id,
              title: '表',
              type: 'view_table',
              isLeaf: false,
              expanded: false,
              data: { dbData: item },
            },
            {
              key: 'proc' + item.id,
              title: '存储过程',
              type: 'view_proc',
              isLeaf: false,
              expanded: false,
              data: { dbData: item },
            },
            {
              key: 'view' + item.id,
              title: '视图',
              type: 'view',
              isLeaf: false,
              expanded: false,
              data: { dbData: item },
            },
            {
              key: 'data_set' + item.id,
              title: '数据集',
              type: 'data_set',
              isLeaf: false,
              expanded: false,
              data: { dbData: item },
            },
          ],
        };
      });
      expandedKeys.value = [];
      loadedKeys.value = [];
    });
  };

  // 数据库连接信息加载
  function onLoadData(treeNode) {
    return new Promise((resolve: (value?: unknown) => void) => {
      const { data, type } = treeNode;
      // if (type === 'db') {
      //   getTreeData();
      // }
      if (treeNode.dataRef.children) {
        resolve();
        return;
      }
      if (type === 'db') {
        resolve();
        return;
      } else if (type === 'view_proc') {
        getProcList(treeNode, data?.dbData?.id).then(() => {
          resolve();
        });
        return;
      } else if (type === 'view') {
        // 暂未实现
        console.log('view');
      } else if (type === 'data_set') {
        // 暂未实现
        console.log('data_set');
      } else if (type === 'view_table') {
        console.log('view_table');
        getTableList(treeNode, treeNode.data?.dbData?.id).then(() => {
          resolve();
        });
        return;
      }
      resolve();
    });
  }

  // 获取表列表
  async function getTableList(treeNode, dbInfoId: string) {
    const res = await tableList(dbInfoId);
    const children = res.map((item: any) => {
      item.dbData = { ...treeNode?.data?.dbData };
      return {
        key: item.tableName,
        title: item.tableName,
        data: item,
        type: 'table',
        isLeaf: true,
      };
    });
    treeNode.dataRef.children = children;
  }

  // 获取存储过程
  async function getProcList(treeNode, dbInfoId: string) {
    const res = await procList(dbInfoId);
    const children = res.map((item: any) => {
      item.dbData = { ...treeNode?.data?.dbData };
      return {
        key: item.procName,
        title: item.procName,
        data: item,
        type: 'proc',
        isLeaf: true,
      };
    });
    treeNode.dataRef.children = children;
  }

  // 新增数据库连接
  function handleAddDb() {
    openModal(true, {
      isUpdate: false,
    });
  }

  function handleSuccess() {
    getTreeData();
  }

  function handleSelect(selectedKeys, e) {
    treeCurrentData.value = e.node.data;
  }

  onMounted(() => {
    getTreeData();
  });
</script>
