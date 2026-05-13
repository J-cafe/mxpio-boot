<template>
  <BasicModal
    width="80%"
    :height="600"
    title="录入盘点数量"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :showOkBtn="false"
    cancelText="关闭"
  >
    <VxeBasicTable ref="tableRef" v-bind="gridOptions2" />
    <InventoryAppendModal @register="registerAddModal" @success="handleSuccess" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { computed, h, ref } from 'vue';
  import { BasicModal, useModalInner, VxeBasicTable, useModal } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridPropTypes } from '@mxpio/components';
  import { executeSearchFormSchema } from './inventoryTask.data';
  import { saveInvLineApi, invlinePageApi, removeInvLineApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { CuryTypeEnum } from '@mxpio/enums';
  import { Tag } from 'ant-design-vue';
  import InventoryAppendModal from './InventoryAppendModal.vue';

  const componentName = 'InvTaskExecuteModal';
  defineOptions({ name: componentName });

  const [registerAddModal, { openModal: openAddModal }] = useModal();
  // const emit = defineEmits(['success', 'register']);
  const formData = ref<Recordable>({});
  const executeColumns: VxeGridPropTypes.Columns = [
    { type: 'checkbox', width: 40 },
    {
      title: '序号',
      type: 'seq',
      width: '50',
      align: 'center',
    },
    { title: '行号', field: 'lineNo', width: 80, sortable: true },
    { title: '物料编码', field: 'itemCode', width: 100 },
    { title: '物料名称', field: 'itemName', width: 120 },
    { title: '规格型号', field: 'itemSpec', width: 80 },
    { title: '单位', field: 'unitCode', formatter: 'dictText', width: 80 },
    { title: '图号', field: 'drawingNo', width: 100 },
    { title: '物料组', field: 'itemGroupCode', formatter: 'dictText', width: 100 },
    { title: '批次号', field: 'lotNo', width: 120 },
    { title: '批次索引号', field: 'subLotIndex', width: 80 },
    { title: '库存数量', field: 'quantityStr', width: 100 },
    { title: '账实差异', field: 'inventoryDiffNum', width: 100 },
    {
      title: '处理方式',
      field: 'handleMethod',
      slots: {
        default: ({ row }) => {
          const handleMethod = row.handleMethod;
          if (!handleMethod) {
            return ' ';
          }
          let color = 'green';
          if (handleMethod === '-1') {
            color = '#f50';
          } else if (handleMethod === '1') {
            color = 'red';
          }
          return h(
            Tag,
            {
              color: color,
            },
            () => row.textMap?.handleMethod$DICT_TEXT_ || '',
          );
        },
      },
      width: 100,
    },
    { title: '备注', field: 'memo', width: 120 },
    {
      title: '实盘数量',
      field: 'inventoryNum',
      editRender: {
        name: 'AInputNumber',
        events: { blur: handleInvItem },
      },
      width: 120,
      fixed: 'right',
    },
    {
      title: '差异原因',
      field: 'diffReason',
      editRender: {
        name: 'AInput',
        events: { blur: handleInvItem },
      },
      width: 150,
      fixed: 'right',
    },
    {
      title: '责任单位',
      field: 'responsibleUnit',
      editRender: {
        name: 'DeptSelect',
        events: { change: handleInvItem },
      },
      width: 150,
      fixed: 'right',
    },
  ];

  const buttons = [
    {
      content: '新增',
      buttonRender: {
        name: 'AButton',
        attrs: {
          class: 'ml-2',
        },
        props: {
          type: 'primary',
          preIcon: 'mdi:page-next-outline',
        },
        events: {
          click: () => {
            console.log('新增', formData);
            openAddModal(true, {
              invData: formData.value,
            });
          },
        },
      },
    },
    {
      content: '删除',
      buttonRender: {
        name: 'AButton',
        props: {
          type: 'primary',
          danger: true,
          preIcon: 'mdi:delete-forever',
        },
        events: {
          click: async () => {
            deleteInvLine();
          },
        },
      },
    },
  ];

  // const toolbarConfig = computed<BasicVxeTableProps['toolbarConfig']>(() => {
  //   return {
  //     buttons: formData.value.isAppend ? buttons : [],
  //     tools: [],
  //   };
  // });
  const vxeGridOptions = computed<BasicVxeTableProps>(() => {
    return {
      rowConfig: {
        keyField: 'id',
      },
      proxyConfig: {
        autoLoad: false,
      },
      toolbarConfig: {
        buttons: formData.value.isAppend ? buttons : [],
        tools: [],
      },
      sortConfig: {
        defaultSort: {
          field: 'lineNo',
          order: 'asc',
        },
      },
      height: '500px',
    };
  });
  const { tableRef, gridOptions, handleSuccess } = useListCrudHook({
    componentName,
    columns: executeColumns,
    searchFormSchema: executeSearchFormSchema,
    pageApi: invlinePageApi,
    module: 'erp',
    vxeGridOptions: vxeGridOptions,
    filters: () => ({
      'inventoryCode@EQ': formData.value.inventoryCode,
    }),
  });

  const gridOptions2 = computed<BasicVxeTableProps>(() => {
    return {
      ...gridOptions.value,
      keepSource: true,
      minHeight: '200px',
      tableClass: '!px-0 !py-0',
      editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true,
        autoClear: false,
      },

      editRules: {
        inventoryNum: [
          { required: true, message: '请输入实盘数量', trigger: 'change' },
          {
            validator: ({ cellValue }) => {
              if (cellValue < 0) {
                return new Error('实盘数量不能小于0');
              }
            },
            trigger: 'change',
          },
        ],
        responsibleUnit: [
          {
            validator: ({ cellValue, row }) => {
              if (!cellValue && Number(row.inventoryNum) !== Number(row.quantityStr)) {
                return new Error('请选择责任单位');
              }
            },
            trigger: 'change',
          },
        ],
      },
      checkboxConfig: { checkMethod: checkMethod, visibleMethod: checkMethod },
    };
  });

  const [registerModal, { setModalProps }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    formData.value = data.record;
    tableRef.value?.commitProxy('query');
  });

  function handleInvItem({ row }) {
    const isUpdate = tableRef.value?.isUpdateByRow(row);
    if (isUpdate) {
      save(row);
    }
  }

  async function save(row) {
    try {
      let errMap = await tableRef.value?.validate(row);
      if (errMap) {
        return;
      }
      setModalProps({ confirmLoading: true });
      const submitData = classifyIntoFormData(row);
      await saveInvLineApi(submitData);
      tableRef.value?.commitProxy('query');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  function classifyIntoFormData(row) {
    formData.value.crudType = CuryTypeEnum.UPDATE;
    row.crudType = CuryTypeEnum.UPDATE;
    return {
      ...formData.value,
      itemList: [row],
    };
  }

  function deleteInvLine() {
    const selectedRows = tableRef.value?.getCheckboxRecords();
    if (!selectedRows || selectedRows.length === 0) {
      return;
    }
    const ids = selectedRows.map((row) => row.id);
    removeInvLineApi(ids.join(',')).then(() => {
      tableRef.value?.commitProxy('query');
    });
  }

  function checkMethod({ row }) {
    if (row.planGenerate) {
      return false;
    }
    return true;
  }
</script>
