<template>
  <div class="m-3">
    <row :gutter="10">
      <Col :span="6">
        <Card title="物料组" :bodyStyle="{ height: '700px' }">
          <template #extra>
            <a-button
              type="primary"
              shape="circle"
              class="mr-2"
              :icon="h(PlusOutlined)"
              @click="handleAddGroup"
              v-if="hasPermission(auth.add)"
            />
            <a-button
              type="primary"
              danger
              shape="circle"
              :icon="h(MinusOutlined)"
              @click="handleDelGroup"
            />
          </template>
          <BasicTree
            v-model:selectedKeys="selectedKeys"
            ref="materialGroupRef"
            :field-names="fieldNames"
            :treeData="treeData"
            @select="treeSelect"
          >
            <template #title="item">
              <a-dropdown :trigger="['contextmenu']" :key="item.groupCode">
                <span class="!w-full" :key="item.groupCode">{{ item.groupName }}</span>
                <template #overlay>
                  <a-menu @click="({ key }) => onContextMenuClick(item.groupCode, key, item)">
                    <a-menu-item key="1" v-if="hasPermission(auth.add)">新增分类</a-menu-item>
                    <a-menu-item key="2" v-if="hasPermission(auth.add)">新增子分类</a-menu-item>
                    <a-menu-item key="3" v-if="hasPermission(auth.edit)">编辑分类</a-menu-item>
                    <a-menu-item key="4" v-if="hasPermission(auth.delete)">删除</a-menu-item>
                  </a-menu>
                </template>
              </a-dropdown>
            </template>
          </BasicTree>
        </Card>
      </Col>
      <Col :span="18">
        <VxeBasicTable ref="tableRef" v-bind="gridOptions">
          <template #action="{ row }">
            <TableAction
              :outside="true"
              :actions="[
                {
                  label: '编辑',
                  onClick: handleEdit.bind(null, row),
                  auth: auth.edit,
                },
              ]"
              :dropDownActions="[
                {
                  label: '详情',
                  onClick: handleDetail.bind(null, row),
                },
                {
                  label: '删除',
                  color: 'error',
                  popConfirm: {
                    title: '是否确认删除',
                    placement: 'left',
                    confirm: handleDelete.bind(null, row),
                  },
                  auth: auth.delete,
                },
              ]"
            />
          </template>
        </VxeBasicTable>
      </Col>
    </row>
    <MaterialModal width="80%" @register="registerModal" @success="handleSuccess" />
    <MaterialGroupModal width="600px" @register="registerGroupModal" @success="handleGroupSuccess"
  /></div>
</template>
<script lang="ts" setup>
  import { Card, Row, Col, Dropdown as ADropdown, Menu as AMenu } from 'ant-design-vue';
  import { PlusOutlined, MinusOutlined } from '@ant-design/icons-vue';
  import { onMounted, reactive, ref, h } from 'vue';
  import { TableAction, VxeBasicTable, useModal, BasicTree } from '@mxpio/components';
  import type {
    VxeGridPropTypes,
    VxeGridInstance,
    BasicVxeTableProps,
    TreeProps,
  } from '@mxpio/components';
  import { deleteItem, itemList, itemGroup, deleteItemGroup, itemConfig } from '@mxpio/bizcommon';
  import { columns, searchFormSchema } from './material.data';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import { useProfile, useMessage } from '@mxpio/hooks';
  import MaterialModal from './MaterialModal.vue';
  import MaterialGroupModal from './MaterialGroupModal.vue';
  import { usePermission } from '@mxpio/hooks/src/web/usePermission';

  defineOptions({ name: 'MaterialList' });

  const { hasPermission } = usePermission();
  const [registerModal, { openModal }] = useModal();

  const [registerGroupModal, { openModal: openGroupModal }] = useModal();

  const { restoreStore, updateStore } = useProfile();

  const AMenuItem = AMenu.Item;
  const auth = {
    add: 'erp:MaterialList:add',
    edit: 'erp:MaterialList:edit',
    delete: 'erp:MaterialList:delete',
    import: 'erp:MaterialList:import',
  };
  const treeData = ref([]);
  const selectedKeys = ref([]);
  const filters = reactive({
    itemGroupCode: '',
  });
  const { createConfirm, createMessage } = useMessage();
  const fieldNames: TreeProps['fieldNames'] = {
    children: 'children',
    title: 'groupName',
    key: 'groupCode',
  };
  const importCode = ref();
  const tableRef = ref<VxeGridInstance>();
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'MaterialList',
    columns: columns,
    toolbarConfig: {
      buttons: [
        {
          content: '新增',
          buttonRender: {
            name: 'AButton',
            props: {
              type: 'primary',
              preIcon: 'mdi:page-next-outline',
            },
            attrs: {
              class: 'ml-2',
            },
            events: {
              click: () => {
                handleCreate();
              },
            },
          },
          visible: hasPermission(auth.add),
        },
      ],
      tools: [
        {
          toolRender: {
            name: 'ExportButton',
            attrs: {
              class: 'ml-2',
            },
            props: {
              export: 'sys:MaterialList:export',
            },
          },
        },
        {
          toolRender: {
            name: 'ImportButton',
            props: {
              importCode: () => importCode.value,
            },
          },
          visible: hasPermission(auth.import),
        },
      ],
    },
    formConfig: {
      enabled: true,
      items: searchFormSchema,
    },
    customConfig: {
      storage: {
        visible: true,
        resizable: true,
        sort: true,
        fixed: true,
      }, // 启用自定义列状态保存功能
      restoreStore: restoreStore,
      updateStore: updateStore,
    },
    minHeight: 700,
    proxyConfig: {
      ajax: {
        query: async ({ page, form, sorts }: VxeGridPropTypes.ProxyAjaxQueryParams<any>) => {
          return loadData({ page, form, sorts });
        },
      },
    },
  });

  async function loadData({ page, form, sorts }: VxeTableQueryParams) {
    const params = getVxeTableQueryParams({ page, form, sorts, filters });
    const res = await itemList(params);
    return res;
  }

  async function loadTreeData() {
    const res = await itemGroup();
    if (res) {
      treeData.value = res;
    }
  }

  function handleCreate() {
    openModal(true, {
      isUpdate: false,
    });
  }

  function handleEdit(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: true,
    });
  }

  function handleDetail(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: true,
      disabled: true,
    });
  }

  function handleDelete(record: Recordable) {
    deleteItem(record.itemCode).then(() => {
      tableRef.value?.commitProxy('query');
    });
  }

  function handleSuccess() {
    tableRef.value?.commitProxy('query');
  }

  function handleAddGroup() {
    openGroupModal(true, {
      isUpdate: false,
    });
  }

  function handleDelGroup() {
    if (selectedKeys.value.length > 0) {
      handleDeleteGroup(selectedKeys.value[0]);
    } else {
      createMessage.warning('请选择要删除的分类');
    }
  }

  function handleGroupSuccess() {
    loadTreeData();
  }

  function handleDeleteGroup(groupCode: string) {
    createConfirm({
      iconType: 'warning',
      title: () => h('span', '确认删除'),
      content: () => h('span', '是否删除选中数据?'),
      onOk: async () => {
        await deleteItemGroup(groupCode);
        loadTreeData();
      },
    });
  }

  function onContextMenuClick(groupCode: string, menuKey: any, treeData: any) {
    switch (menuKey) {
      case '1':
        openGroupModal(true, {
          isUpdate: false,
        });
        break;
      case '2':
        openGroupModal(true, {
          isUpdate: false,
          faGroupCode: groupCode,
        });
        break;
      case '3':
        openGroupModal(true, {
          isUpdate: true,
          record: treeData,
        });
        break;
      case '4':
        handleDeleteGroup(groupCode);
        break;
    }
  }

  function treeSelect(keys) {
    filters.itemGroupCode = keys[0];
    tableRef.value?.commitProxy('query');
  }

  onMounted(async () => {
    loadTreeData();
    const res = await itemConfig();
    if (res) {
      importCode.value = res.importTemplate;
    }
  });
</script>
