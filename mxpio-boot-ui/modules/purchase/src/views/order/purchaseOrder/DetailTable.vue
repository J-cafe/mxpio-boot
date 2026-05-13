<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions">
    <template #itemCodeText="{ row }">
      {{ row.itemCode }}
    </template>
    <template #itemCode="{ row }">
      <a-input-search
        readonly
        :disabled="!formData.pnCode"
        :value="row.itemCode"
        @search="handleMaterial(row)"
      />
    </template>
  </VxeBasicTable>
  <MaterialModal @register="registerModal" :pnCode="formData.pnCode" @success="selectMaterial" />
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { setDataCrud } from '@mxpio/utils/src/common';
  import XEUtils from 'xe-utils';
  import { isNaN } from '@mxpio/utils';
  import { merge } from 'lodash-es';
  import { poLineList, positiveNumberPattern, useMaterialLotSelect } from '@mxpio/bizcommon';
  import MaterialModal from './MaterialModal.vue';

  defineOptions({ name: 'DetailTable' });

  const props = defineProps({
    formData: { type: Object, default: () => ({}) },
  });

  const [registerModal, { openModal }] = useModal();
  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const currentRow = ref({});
  // 使用Hook，传入表格引用和自定义参数
  const { getInsertData, getMaterialData } = useMaterialLotSelect({
    tableRef,
    defaultValues: {
      closeStatus: 'open',
      textMap: {
        closeStatus$DICT_TEXT_: '未关闭',
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
      editRender: {},
      slots: {
        default: 'itemCodeText',
        edit: 'itemCode',
      },
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
      title: '采购数量',
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
      title: '到货日期',
      field: 'suggestArriveDate',
      editRender: {
        name: 'ADatePicker',
        props: { valueFormat: 'YYYY-MM-DD' },
      },
      width: 120,
    },
    { title: '请购单号', field: 'reqBizNo', width: 120 },
    { title: '请购单行号', field: 'reqLineNo', width: 120 },
    { title: '请购单数量', field: 'reqQuantity', width: 120 },
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
      minHeight: 350,
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
          { required: true, message: '请输入采购数量', trigger: 'change' },
          {
            type: 'number',
            pattern: positiveNumberPattern,
            message: '采购数量不能小于等于0',
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
        suggestArriveDate: [{ required: true, message: '请选择到货日期', trigger: 'change' }],
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
    const res = await poLineList(record.bizNo);
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
      const purchaseOrderLine = setDataCrud(data, isUpdate.value);
      return {
        purchaseOrderLine,
      };
    } catch (err) {
      console.error(err);
    }
  }

  function handleMaterial(row) {
    currentRow.value = row;
    openModal(true, { pnCode: props.formData.pnCode });
  }

  function selectMaterial(itemCodes: string, rows: Recordable[]) {
    const insertData: Recordable[] = [];
    const insertDataDef = getInsertData();

    if (rows.length === 0) {
      XEUtils.assign(currentRow.value, {
        itemCode: '',
        itemName: '',
        itemSpec: '',
        drawingNo: '',
        unitCode: '',
        quantity: '',
        lotNo: '',
        subLotIndex: '',
        textMap: {},
      });
    } else {
      rows.forEach((item: any, i: number) => {
        if (i === 0) {
          XEUtils.assign(currentRow.value, getMaterialData(item));
        } else {
          insertData.push(
            merge(getMaterialData(item), insertDataDef, {
              lineNo: insertDataDef.lineNo++,
            }),
          );
        }
      });
      tableRef.value?.insert(insertData);
    }
  }

  defineExpose({
    setData,
    validate,
    getData,
  });
</script>
