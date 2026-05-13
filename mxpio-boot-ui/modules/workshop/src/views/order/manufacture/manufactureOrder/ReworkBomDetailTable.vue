<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { setDataCrud } from '@mxpio/utils/src/common';
  import { moLineList, positiveNumberPattern, useMaterialLotSelect } from '@mxpio/bizcommon';
  import XEUtils from 'xe-utils';

  defineOptions({ name: 'ReworkBomDetailTable' });

  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);

  // 使用Hook，传入表格引用和自定义参数
  const { materialSelectConfig, lotSelectConfig, getInsertData } = useMaterialLotSelect({
    tableRef,
    extraLotProps: (row) => ({
      filters: {
        itemCode: row.itemCode,
      },
    }),
    onLotChange: (params, value, item) => {
      const { row } = params;
      XEUtils.set(row, 'whCode', item?.whCode || '');
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
      width: 140,
    },
    {
      title: '齐套数量',
      field: 'analysisColor',
      width: 80,
    },
    {
      title: '供料方式',
      field: 'feedingMode',
      formatter: 'dictText',
      width: 80,
    },
    {
      title: '指定仓库',
      field: 'whCode',
      editRender: {
        name: 'WareHouseSelect',
        props: ({ row }) => ({
          disabled: !!row.lotNo,
        }),
      },
      width: 120,
    },
    {
      title: '实际发料数量',
      field: 'actualQuantity',
      width: 100,
    },
    {
      title: '计划发料数量',
      field: 'planQuantity',
      width: 100,
    },
    {
      title: '实际退料数量',
      field: 'actualRejectQuantity',
      width: 100,
    },
    {
      title: '计划退料数量',
      field: 'planRejectQuantity',
      width: 100,
    },
  ];

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'ReworkBomDetailTable',
      keepSource: true,
      minHeight: '200px',
      tableClass: '!px-0 !py-0',
      columns: detailColumns,
      data: dataSource.value,
      proxyConfig: { enabled: false },
      editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true,
        autoClear: false,
        enabled: !isDisabled.value,
      },
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
      pagerConfig: {
        enabled: false,
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
    const res = await moLineList(record.bizNo);
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
      const manufactureOrderItems = setDataCrud(data, isUpdate.value);
      return {
        manufactureOrderItems,
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
