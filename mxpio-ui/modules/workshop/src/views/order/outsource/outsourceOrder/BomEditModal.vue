<template>
  <BasicModal
    width="1200px"
    title="编辑订单BOM"
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
  import { setDataCrud } from '@mxpio/utils/src/common';
  import {
    positiveNumberPattern,
    useMaterialLotSelect,
    ooLineListApi,
    saveOutsourceBomApi,
  } from '@mxpio/bizcommon';

  defineOptions({ name: 'BomEditModal' });
  const emit = defineEmits(['success', 'register']);

  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const formData = ref<Recordable>({});

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    formData.value = data.record;
    const res = await ooLineListApi(formData?.value?.bizNo);
    tableRef.value?.loadData(res);
  });

  async function handleSubmit() {
    try {
      setModalProps({ confirmLoading: true });
      let errMap = await tableRef.value?.validate(true);
      if (errMap) {
        return;
      }
      const bomData = classifyIntoFormData() || [];
      await saveOutsourceBomApi(bomData);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  // 使用Hook，传入表格引用和自定义参数
  const { materialSelectConfig, lotSelectConfig, getInsertData } = useMaterialLotSelect({
    tableRef,
    extraMaterialProps: (row) => {
      return {
        disabled: getIsPicking(row),
      };
    },
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
    extraMaterialFileds: (item, isClear) => {
      if (isClear) {
        return {
          feedingMode: '',
          makeLength: '',
          makeNum: '',
          textMap: {},
        };
      }
      return {
        feedingMode: item.itemPlanProp?.feedingMode,
        materialType: item.itemMaterialProp?.materialType,
        materialBrand: item.materialBrand,
        textMap: {
          feedingMode$DICT_TEXT_: item.itemPlanProp.textMap?.feedingMode$DICT_TEXT_,
          materialType$DICT_TEXT_: item.itemMaterialProp.textMap?.materialType$DICT_TEXT_,
        },
        bizNo: formData.value?.bizNo,
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
      title: '供料方式',
      field: 'feedingMode',
      formatter: 'dictText',
      width: 100,
    },
    {
      title: '材料类型',
      field: 'materialType',
      formatter: 'dictText',
      width: 100,
    },
    {
      title: '材料牌号',
      field: 'materialBrand',
      width: 100,
    },
    {
      title: '下料长度',
      field: 'makeLength',
      width: 100,
    },
    {
      title: '可制数量',
      field: 'makeNum',
      width: 100,
    },
    {
      title: '数量',
      field: 'quantity',
      editRender: {
        name: 'AInputNumber',
        props: ({ row }) => ({
          // 根据行数据动态设置禁用状态
          disabled: !!row.lotNo,
        }),
      },
      width: 120,
    },
    {
      title: '指定仓库',
      field: 'whCode',
      editRender: {
        name: 'WareHouseSelect',
        props: ({ row }) => ({
          filters: { 'whType@EQ': '1' },
          // 根据行数据动态设置禁用状态
          disabled: !!row.lotNo,
        }),
      },
      width: 100,
    },
    {
      title: '批次号',
      field: 'lotNo',
      width: 120,
      // editRender: {
      //   name: 'InvLotSelect',
      //   events: {
      //     change: (params: any, value, items) => {
      //       const { row } = params;
      //       const executeQuantity = items.reduce((total: number, item: any) => {
      //         return total + (Number(item.executeQuantity) || 0);
      //       }, 0);
      //       XEUtils.set(row, 'quantity', executeQuantity);
      //     },
      //   },
      //   props: ({ row }) => ({
      //     // 根据行数据动态设置禁用状态
      //     filters: {
      //       whCode: row.whCode,
      //       itemCode: row.itemCode,
      //     },
      //     disabled: !row.whCode || !row.itemCode,
      //   }),
      // },
      editRender: lotSelectConfig,
    },
  ];

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'BomEditTable',
      keepSource: true,
      minHeight: 400,
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
      checkboxConfig: { checkMethod: checkMethod },
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
      pagerConfig: {
        enabled: false,
      },
    };
  });

  function getIsPicking(row: Recordable) {
    if (isNaN(Number(row.actualQuantity)) && isNaN(Number(row.planQuantity))) {
      return false;
    }
    return Number(row.actualQuantity) !== 0 || Number(row.planQuantity) !== 0;
  }

  function checkMethod({ row }) {
    const isPicking = getIsPicking(row);
    return !isPicking;
  }

  function classifyIntoFormData() {
    const data: Recordable | undefined = tableRef.value?.getRecordset();
    if (!data) {
      return false;
    }
    const bomData = setDataCrud(data, true);
    return bomData;
  }
</script>
