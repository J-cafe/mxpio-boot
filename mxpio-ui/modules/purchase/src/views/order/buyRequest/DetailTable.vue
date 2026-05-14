<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { setDataCrud } from '@mxpio/utils/src/common';
  import { dateUtil } from '@mxpio/utils';
  import { brLineList, positiveNumberPattern, useMaterialLotSelect } from '@mxpio/bizcommon';

  defineOptions({ name: 'DetailTable' });

  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);

  // 使用Hook，传入表格引用和自定义参数
  const { materialSelectConfig, getInsertData } = useMaterialLotSelect({
    tableRef,
    defaultValues: {
      lineStatus: '01',
      suggestOrderDate: dateUtil().format('YYYY-MM-DD'),
      textMap: {
        lineStatus$DICT_TEXT_: '开立',
      },
    },
    extraMaterialProps: {
      // 可以传入物料选择的额外参数
      filters: {
        'itemSource@EQ': '2',
      },
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
      title: '行状态',
      field: 'lineStatus',
      formatter: 'dictText',
      width: 80,
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
      field: 'quantity',
      editRender: {
        name: 'AInputNumber',
      },
      width: 120,
    },
    {
      title: '计划跟踪号',
      field: 'trackingNo',
      editRender: {
        name: 'AInput',
      },
      width: 120,
    },
    {
      title: '建议下单日期',
      field: 'suggestOrderDate',
      editRender: {
        name: 'ADatePicker',
        props: { valueFormat: 'YYYY-MM-DD' },
      },
      width: 140,
    },
    {
      title: '需求到货日期',
      field: 'arriveDate',
      editRender: {
        name: 'ADatePicker',
        props: { valueFormat: 'YYYY-MM-DD' },
      },
      width: 140,
    },
    {
      title: '用途',
      field: 'usage',
      editRender: {
        name: 'AInput',
      },
      width: 120,
    },
    {
      title: '备注',
      field: 'memo',
      editRender: {
        name: 'AInput',
      },
      width: 120,
    },
  ];

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'SalesDetailTable',
      keepSource: true,
      minHeight: '200px',
      tableClass: '!px-0 !py-0',
      columns: detailColumns,
      data: dataSource.value,
      editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true,
        autoClear: false,
        enabled: !isDisabled.value,
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
                disabled: isDisabled.value,
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
                disabled: isDisabled.value,
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
        quantity: [
          { required: true, message: '请输入数量', trigger: 'change' },
          {
            type: 'number',
            pattern: positiveNumberPattern,
            message: '数量不能小于等于0',
            trigger: 'change',
          },
        ],
        arriveDate: [{ required: true, message: '请选择到货日期', trigger: 'change' }],
      },
      pagerConfig: {
        enabled: false,
      },
    };
  });

  async function setData(data: Recordable) {
    isDisabled.value = !!data?.disabled;
    isUpdate.value = data?.isUpdate || false;
    const record = data.record || {};
    if (!record.bizNo) {
      return;
    }
    const res = await brLineList(record.bizNo);
    tableRef.value?.loadData(res);
  }

  async function validate() {
    let errMap = await tableRef.value?.validate(true);
    if (errMap) {
      return Promise.reject(errMap);
    }
    return Promise.resolve();
  }

  function getData() {
    try {
      const data: Recordable | undefined = tableRef.value?.getRecordset();
      if (!data) {
        return false;
      }
      const buyRequestOrders = setDataCrud(data, isUpdate.value);
      return {
        buyRequestOrders,
      };
    } catch (err) {
      console.error(err);
    }
  }

  defineExpose({
    setData,
    validate,
    getData,
  });
</script>
