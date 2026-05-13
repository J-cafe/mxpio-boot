<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { setDataCrud } from '@mxpio/utils/src/common';
  import XEUtils from 'xe-utils';
  import { wolineList, positiveNumberPattern, useMaterialLotSelect } from '@mxpio/bizcommon';

  defineOptions({ name: 'RawOutOrderDetailTable' });

  const props = defineProps({
    formData: { type: Object, default: () => ({}) },
  });

  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);

  // 使用Hook，传入表格引用和自定义参数
  const { materialSelectConfig, lotSelectConfig, getInsertData } = useMaterialLotSelect({
    tableRef,
    extraLotProps: (row) => ({
      filters: {
        whCode: props.formData.whCode,
        itemCode: row.itemCode,
      },
      disabled: !props.formData.whCode,
      multiple: false,
    }),
    defaultValues: {
      closeStatus: 'open',
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
      editRender: materialSelectConfig,
      // editRender: {
      //   name: 'MaterialSelect',
      //   placeholder: '请选择',
      //   props: {
      //     filters: {
      //       'saleAble@EQ': '1',
      //     },
      //   },
      //   events: {
      //     change: (params: any, value, items) => {
      //       const { row, $grid } = params;
      //       const insertData: Recordable[] = [];
      //       const insertDataDef = getInsertData();
      //       if (items.length === 0) {
      //         XEUtils.assign(row, {
      //           itemName: '',
      //           itemSpec: '',
      //           drawingNo: '',
      //           unitCode: '',
      //           quantity: '',
      //           lotNo: '',
      //           subLotIndex: '',
      //           textMap: {},
      //         });
      //         return;
      //       }
      //       items.forEach((item: any, i) => {
      //         if (i === 0) {
      //           XEUtils.assign(row, getMaterialData(item));
      //         } else {
      //           insertData.push(
      //             merge(getMaterialData(item), insertDataDef, {
      //               lineNo: insertDataDef.lineNo++,
      //             }),
      //           );
      //         }
      //       });
      //       $grid.insert(insertData);
      //     },
      //   },
      // },
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
        props: ({ row }) => ({
          disabled: !!row.lotNo,
        }),
      },
      width: 120,
    },
    {
      title: '批次号',
      field: 'lotNo',
      editRender: lotSelectConfig,
      //   editRender: {
      //     name: 'InvLotSelect',
      //     events: {
      //       change: (params: any, value, items) => {
      //         const { row } = params;
      //         XEUtils.set(row, 'quantity', items.executeQuantity);
      //         XEUtils.set(row, 'subLotIndex', items.subLotIndex);
      //       },
      //     },
      //     props: ({ row }) => {
      //       return {
      //         // 根据行数据动态设置禁用状态
      //         filters: {
      //           whCode: props.formData.whCode,
      //           itemCode: row.itemCode,
      //         },
      //         disabled: !row.itemCode || !props.formData.whCode,
      //         multiple: false,
      //       };
      //     },
      //   },
      width: 120,
    },
    {
      title: '已执行数量',
      field: 'actualQuantity',
      width: 100,
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
      id: 'RawOutOrderDetailTable',
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
        // lotNo: [{ required: true, message: '请输入批次号', trigger: 'change' }],
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
    const res = await wolineList(record.bizNo);
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
      const warehouseOrderLines = setDataCrud(data, isUpdate.value);
      warehouseOrderLines.forEach((item: Recordable) => {
        item.whCode = props.formData.whCode;
      });
      return {
        warehouseOrderLines,
      };
    } catch (err) {
      console.error(err);
    }
  }

  function clearLot() {
    const { fullData = [] } = tableRef.value?.getTableData() || {};
    fullData.forEach((item: Recordable) => {
      XEUtils.set(item, 'lotNo', '');
      XEUtils.set(item, 'subLotIndex', '');
    });
  }

  defineExpose({
    setData,
    validate,
    getData,
    clearLot,
  });
</script>
