<template>
  <div>
    <VxeBasicTable class="!px-0 !py-0" ref="tableRef" v-bind="gridOptions">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: '编辑',
              onClick: handleEdit.bind(null, row),
            },
          ]"
          :dropDownActions="[
            {
              label: '删除',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                placement: 'left',
                confirm: handleDelete.bind(null, row),
              },
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <ColumnModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { VxeBasicTable, TableAction, useModal } from '@mxpio/components';
  import type { VxeGridPropTypes, VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';
  import { columnList, deleteColumn } from '../../api/dbconsole';
  import ColumnModal from './ColumnModal.vue';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import { useProfile } from '@mxpio/hooks';

  import { message } from 'ant-design-vue';
  import { reactive, ref, defineProps, watch } from 'vue';

  defineOptions({ name: 'DBColumnList' });

  const [registerModal, { openModal }] = useModal();
  const { restoreStore, updateStore } = useProfile();

  const props = defineProps({
    table: {
      type: Object,
      default: () => ({}),
    },
  });

  watch(
    () => props.table,
    (newVal) => {
      console.log('props.table', newVal);
      if (newVal.dbInfoId && newVal.tableName) {
        tableRef.value?.commitProxy('query');
      } else {
        tableRef.value?.clearAll();
      }
    },
    { deep: true },
  );

  const tableRef = ref<VxeGridInstance>();
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'DBColumnList',
    columns: [
      {
        title: '序号',
        type: 'seq',
        fixed: 'left',
        width: '50',
        align: 'center',
      },
      {
        field: 'columnName',
        title: '列名称',
        width: 150,
      },
      {
        field: 'columnType',
        title: '类型',
        width: 150,
      },
      {
        field: 'columnSize',
        title: '长度',
        width: 150,
      },
      {
        field: 'isprimaryKey',
        title: '是否主键',
        width: 150,
      },
      {
        field: 'isnullAble',
        title: '是否可为空',
        width: 150,
      },
      {
        title: '操作',
        field: 'operation',
        width: 150,
        slots: { default: 'action' },
      },
    ],
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
        },
      ],
      tools: [
        {
          toolRender: {
            name: 'ExportButton',
            props: {
              export: 'sys:DBColumnList:export',
            },
          },
        },
      ],
    },
    pagerConfig: {
      enabled: false,
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
    maxHeight: 700,
    proxyConfig: {
      autoLoad: false,
      ajax: {
        query: async ({ page, form, sorts }: VxeGridPropTypes.ProxyAjaxQueryParams<any>) => {
          return loadData({ page, form, sorts });
        },
      },
    },
  });

  async function loadData({ page, form, sorts }: VxeTableQueryParams) {
    const params = getVxeTableQueryParams({ page, form, sorts });
    const res = await columnList(props.table?.dbInfoId, props.table?.tableName, params);
    return res;
  }

  function handleCreate() {
    if (!props.table?.tableName) {
      return message.warning('请先选择表');
    }
    openModal(true, {
      isUpdate: false,
      table: props.table,
    });
  }

  function handleEdit(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: true,
      table: props.table,
    });
  }

  function handleDelete(record: Recordable) {
    deleteColumn(props.table?.dbInfoId, props.table?.tableName, record.columnName).then(() => {
      tableRef.value?.commitProxy('query');
    });
  }

  function handleSuccess() {
    tableRef.value?.commitProxy('query');
  }
</script>
