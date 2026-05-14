<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { setDataCrud } from '@mxpio/utils/src/common';
  import { spLineApi, positiveNumberPattern, useMaterialLotSelect } from '@mxpio/bizcommon';

  defineOptions({ name: 'SaleForecastDetailTable' });

  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const formData: Recordable = {};

  // 使用Hook，传入表格引用和自定义参数
  const { materialSelectConfig, getInsertData } = useMaterialLotSelect({
    tableRef,
    defaultValues: {
      closeStatus: 'open',
    },
    extraMaterialFileds: (item) => {
      return {
        itemGroupCode: item.itemGroupCode,
        textMap: {
          itemGroupCode$DICT_TEXT_: item?.textMap?.itemGroupCode$DICT_TEXT_,
        },
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
      title: '行状态',
      field: 'closeStatus',
      editRender: {
        name: 'DictSelect',
        props: {
          dictCode: 'ERP_COMMON_CLOSE_STATUS',
          disabled: true,
        },
      },
      width: 100,
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
      title: '物料组',
      field: 'itemGroupCode',
      formatter: 'dictText',
      width: 80,
    },
    {
      title: '参考日期',
      field: 'referenceDate',
      editRender: {
        name: 'ADatePicker',
        props: { valueFormat: 'YYYY-MM-DD' },
      },
      width: 120,
    },
    {
      title: '预测数量',
      field: 'quantity',
      editRender: {
        name: 'AInputNumber',
      },
      width: 120,
    },
    {
      title: '需求日期',
      field: 'demandDate',
      editRender: {
        name: 'ADatePicker',
        props: { valueFormat: 'YYYY-MM-DD' },
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
      id: 'saleForecastDetailTable',
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
          { required: true, message: '请输入预测数量', trigger: 'change' },
          {
            type: 'number',
            pattern: positiveNumberPattern,
            message: '预测数量不能小于等于0',
            trigger: 'change',
          },
        ],
        demandDate: [{ required: true, message: '请选择需求日期', trigger: 'change' }],
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
    formData.value = record;
    if (!record.bizNo) {
      return;
    }
    const res = await spLineApi(record.bizNo);
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
      const salePredictionLines = setDataCrud(data, isUpdate.value);
      return {
        salePredictionLines,
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
