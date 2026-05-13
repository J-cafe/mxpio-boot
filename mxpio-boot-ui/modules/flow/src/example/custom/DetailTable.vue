<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { setDataCrud } from '@mxpio/utils/src/common';
  import XEUtils from 'xe-utils';
  import { isNaN } from '@mxpio/utils';
  import { soLineList, positiveNumberPattern, useMaterialLotSelect } from '@mxpio/bizcommon';

  defineOptions({ name: 'SalesDetailTable' });

  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const isBpmn = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const formData: Recordable = {};

  // 使用Hook，传入表格引用和自定义参数
  const { materialSelectConfig, getInsertData } = useMaterialLotSelect({
    tableRef,
    extraMaterialProps: {
      // 可以传入物料选择的额外参数
      filters: {
        'saleAble@EQ': '1',
      },
    },
    defaultValues: {
      closeStatus: 'open',
      presentFlag: '0',
      textMap: {
        closeStatus$DICT_TEXT_: '未关闭',
        presentFlag$DICT_TEXT_: '否',
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
      field: 'closeStatus',
      formatter: 'dictText',
      width: 80,
    },
    {
      title: '物料编码',
      field: 'itemCode',
      editRender: materialSelectConfig, // 使用Hook提供的配置
      width: 140,
    },
    // {
    //   title: '物料编码',
    //   field: 'itemCode',
    //   editRender: {
    //     name: 'MaterialSelect',
    //     placeholder: '请选择',
    //     props: {
    //       filters: {
    //         'saleAble@EQ': '1',
    //       },
    //     },
    //     events: {
    //       change: (params: any, value, items) => {
    //         const { row, $grid } = params;
    //         const insertData: Recordable[] = [];
    //         const insertDataDef = getInsertData();
    //         items.forEach((item: any, i) => {
    //           if (i === 0) {
    //             XEUtils.assign(row, getMaterialData(item));
    //           } else {
    //             insertData.push(
    //               merge(getMaterialData(item), insertDataDef, {
    //                 lineNo: insertDataDef.lineNo++,
    //               }),
    //             );
    //           }
    //         });
    //         $grid.insert(insertData);
    //       },
    //     },
    //   },
    //   width: 140,
    // },
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
      title: '应发数量',
      field: 'quantity',
      editRender: {
        name: 'AInputNumber',
        events: {
          change: calacTotalAmount,
        },
      },
      width: 120,
    },
    {
      title: '交付日期',
      field: 'deliverDate',
      editRender: {
        name: 'ADatePicker',
        props: { valueFormat: 'YYYY-MM-DD' },
      },
      width: 120,
    },
    {
      title: '单价',
      field: 'taxPrice',
      editRender: {
        name: 'AInputNumber',
        events: {
          change: calacTotalAmount,
        },
      },
      width: 120,
    },
    {
      title: '金额',
      field: 'totalAmount',
      width: 100,
    },
    {
      title: '是否赠品',
      field: 'presentFlag',
      editRender: {
        name: 'DictSelect',
        props: {
          dictCode: 'MB_SYSTEM_YES_NO',
        },
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
          { required: true, message: '请输入应发数量', trigger: 'change' },
          {
            type: 'number',
            pattern: positiveNumberPattern,
            message: '应发数量不能小于等于0',
            trigger: 'change',
          },
        ],
        taxPrice: [
          { required: true, message: '请输入单价', trigger: 'change' },
          {
            type: 'number',
            pattern: positiveNumberPattern,
            message: '单价不能小于等于0',
            trigger: 'change',
          },
        ],
        deliverDate: [{ required: true, message: '请选择交付日期', trigger: 'change' }],
      },
      pagerConfig: {
        enabled: false,
      },
    };
  });

  async function setData(data: Recordable) {
    isDisabled.value = !!data?.disabled;
    isUpdate.value = data?.isUpdate || false;
    isBpmn.value = !!data?.isBpmn;
    const record = data.record || {};
    formData.value = record;
    if (isBpmn.value) {
      // 流程实例时，直接从流程数据里获取，避免退回后再次修改数据显示错误
      tableRef.value?.loadData(formData.value.salesOrderLines || []);
      return;
    }
    if (!record.bizNo) {
      return;
    }
    const res = await soLineList(record.bizNo);
    tableRef.value?.loadData(res);
  }

  async function validate() {
    let errMap = await tableRef.value?.validate(true);
    if (errMap) {
      return Promise.reject(errMap);
    }
    return Promise.resolve();
  }

  function calacTotalAmount(params) {
    const { row } = params;
    const quantity = Number(row.quantity) || 0; // 数量
    const taxPrice = Number(row.taxPrice) || 0;
    if (isNaN(quantity) || isNaN(taxPrice)) {
      return false;
    }
    const totalAmount = Number((quantity * taxPrice).toFixed(2)) - 0;
    XEUtils.set(row, 'totalAmount', totalAmount);
  }

  function getData() {
    try {
      const data: Recordable | undefined = tableRef.value?.getRecordset();
      if (!data) {
        return false;
      }
      const salesOrderLines = setDataCrud(data, isUpdate.value);
      return {
        salesOrderLines,
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
