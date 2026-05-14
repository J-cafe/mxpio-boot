<template>
  <BasicModal
    width="1200px"
    title="请填写需要领用的备件和数量"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable, BasicModal, useModalInner } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import {
    positiveNumberPattern,
    useMaterialLotSelect,
    pickingEqpRepairApi,
    useExecuteClassifyData,
  } from '@mxpio/bizcommon';

  defineOptions({ name: 'EqpUpkeepPickingModal' });
  const emit = defineEmits(['success', 'register']);

  const { classifyOrderFormData } = useExecuteClassifyData();
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const bizNo = ref('');
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    bizNo.value = data.bizNo;
  });

  async function handleSubmit() {
    try {
      setModalProps({ confirmLoading: true });
      let errMap = await tableRef.value?.validate(true);
      if (errMap) {
        return;
      }
      const { fullData = [] } = tableRef.value?.getTableData() || {};
      const submitData = classifyOrderFormData(fullData, (item) => {
        return {
          bizOrderNo: bizNo.value,
          lineQuantity: item.executeQuantity,
        };
      });
      await pickingEqpRepairApi(bizNo.value, { lines: submitData });
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  // 使用Hook，传入表格引用和自定义参数
  const { materialSelectConfig, getInsertData } = useMaterialLotSelect({
    tableRef,
    extraLotProps: (row) => {
      return {
        filters: {
          whCode: row.whCode,
          itemCode: row.itemCode,
        },
        multiple: false,
        disabled: !row.itemCode || !row.whCode,
      };
    },
  });

  const detailColumns: VxeGridPropTypes.Columns = [
    { type: 'checkbox', width: 40 },
    {
      title: '序号',
      type: 'seq',
      width: 50,
      align: 'center',
    },
    {
      title: '行号',
      field: 'lineNo',
      width: 50,
      align: 'center',
    },
    {
      title: '物料编码',
      field: 'itemCode',
      editRender: materialSelectConfig, // 使用Hook提供的配置
      width: 140,
    },
    {
      title: '物料名称',
      field: 'itemName',
      width: 100,
    },
    {
      title: '规格型号',
      field: 'itemSpec',
      width: 100,
    },
    {
      title: '图号',
      field: 'drawingNo',
      width: 100,
    },
    {
      title: '单位',
      field: 'unitCode',
      formatter: 'dictText',
      width: 80,
    },
    {
      title: '数量',
      field: 'executeQuantity',
      editRender: {
        name: 'AInputNumber',
      },
      width: 140,
    },
    {
      title: '指定仓库',
      field: 'whCode',
      editRender: {
        name: 'WareHouseSelect',
        props: {
          filters: { 'whType@EQ': '1' },
        },
      },
      width: 140,
    },
  ];

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'EqpRepairPickingTable',
      keepSource: true,
      height: 400,
      tableClass: '!px-0 !py-0',
      columns: detailColumns,
      data: dataSource.value,
      editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true,
        autoClear: false,
      },
      proxyConfig: { enabled: false },
      toolbarConfig: {
        buttons: [
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
                  const insertData = getInsertData();
                  tableRef.value?.insert(insertData);
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
              attrs: {
                class: 'ml-2',
              },
              events: {
                click: async () => {
                  tableRef.value?.removeCheckboxRow();
                },
              },
            },
          },
        ],
        import: false,
        print: false,
        export: false,
        refresh: false,
        custom: false,
      },
      editRules: {
        itemCode: [{ required: true, message: '请选择物料', trigger: 'change' }],
        executeQuantity: [
          { required: true, message: '请输入数量', trigger: 'change' },
          {
            type: 'number',
            pattern: positiveNumberPattern,
            message: '数量不能小于等于0',
            trigger: 'change',
          },
        ],
        whCode: [{ required: true, message: '请选择指定仓库', trigger: 'change' }],
      },
      pagerConfig: {
        enabled: false,
      },
    };
  });
</script>
