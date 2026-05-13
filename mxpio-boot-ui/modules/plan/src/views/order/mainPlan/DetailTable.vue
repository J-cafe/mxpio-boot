<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
  <SaleLinesModal ref="saleLinesModalRef" @register="registerSaleModal" @success="selectSale" />
  <SaleForecastLineModal
    ref="saleForecastLinesModalRef"
    @register="registerSaleForecastModal"
    @success="selectSaleForecast"
  />
  <OrganizeOredrLinesModal
    ref="organizeOredrLinesModalRef"
    @register="registerOrganizeOredrModal"
    @success="selectOrganizeOredr"
  />
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { setDataCrud } from '@mxpio/utils/src/common';
  import { mpsLineListApi } from '@mxpio/bizcommon';
  import SaleLinesModal from './SaleLinesModal.vue';
  import SaleForecastLineModal from './SaleForecastLineModal.vue';
  import OrganizeOredrLinesModal from './OrganizeOredrLinesModal.vue';

  defineOptions({ name: 'DetailTable' });

  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const formData = ref<Recordable>({});
  const [registerSaleModal, { openModal: openSaleModal }] = useModal();
  const [registerSaleForecastModal, { openModal: openSaleForecastModal }] = useModal();
  const [registerOrganizeOredrModal, { openModal: openOrganizeOredrModal }] = useModal();

  // 使用Hook，传入表格引用和自定义参
  const detailColumns: VxeGridPropTypes.Columns = [
    { type: 'checkbox', width: 40 },
    { title: '序号', type: 'seq', width: 50, align: 'center' },
    { title: '需求类型', field: 'dataType', width: 100, formatter: 'dictText' },
    { title: '业务单号', field: 'bizNo', width: 100 },
    { title: '业务单行号', field: 'bizLineNo', width: 100 },
    { title: '物料编码', field: 'itemCode', width: 120 },
    { title: '物料名称', field: 'itemName', width: 100 },
    { title: '规格型号', field: 'itemSpec', width: 100 },
    { title: '单位', field: 'unitCode', formatter: 'dictText', width: 80 },
    { title: '图号', field: 'drawingNo', width: 100 },
    { title: '需求数量', field: 'needQuantity', width: 120 },
    { title: '需求日期', field: 'needDate', width: 120 },
    {
      title: '计划交付日期',
      field: 'deliveryDate',
      editRender: {
        name: 'ADatePicker',
        props: { valueFormat: 'YYYY-MM-DD' },
      },
      width: 140,
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
      id: 'MainPlanDetailTable',
      keepSource: true,
      height: 350,
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
            content: '新增销售需求',
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
                  const salesKey = getLineKeyByType('01');
                  openSaleModal(true, {
                    versionCode: formData.value.code,
                    existKeys: salesKey,
                  });
                },
              },
            },
          },
          {
            content: '新增组织需求',
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
                  const lineKey = getLineKeyByType('02');
                  openOrganizeOredrModal(true, {
                    versionCode: formData.value.code,
                    existKeys: lineKey,
                  });
                },
              },
            },
          },
          {
            content: '新增销售预测',
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
                  const forecastKey = getLineKeyByType('04');
                  openSaleForecastModal(true, {
                    versionCode: formData.value.code,
                    existKeys: forecastKey,
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
        deliveryDate: [{ required: true, message: '请选择计划交付日期', trigger: 'change' }],
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
    if (!record.code) {
      return;
    }
    const res = await mpsLineListApi(record.code);
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
      const mpsLineList = setDataCrud(data, isUpdate.value);
      return {
        mpsLineList,
      };
    } catch (err) {
      console.error(err);
    }
  }

  function selectSale(rows: Recordable) {
    const salesKey = getLineKeyByType('01');
    const datas = rows
      .filter((item: Recordable) => {
        return !salesKey.includes(`${item.bizNo}:${item.lineNo}`);
      })
      .map((item: Recordable) => {
        return {
          itemName: item.itemName,
          itemCode: item.itemCode,
          unitCode: item.unitCode,
          itemSpec: item.itemSpec,
          drawingNo: item.drawingNo,
          quantity: item.quantity,
          deliveryDate: item.deliverDate,
          needQuantity: item.quantity,
          needDate: item.deliverDate,
          bizNo: item.bizNo,
          bizLineNo: item.lineNo,
          dataType: '01',
          textMap: {
            dataType$DICT_TEXT_: '销售订单',
          },
        };
      });
    tableRef.value?.insert(datas);
  }

  function selectSaleForecast(rows: Recordable) {
    const forecastKey = getLineKeyByType('04');
    const datas = rows
      .filter((item: Recordable) => {
        return !forecastKey.includes(`${item.bizNo}:${item.lineNo}`);
      })
      .map((item: Recordable) => {
        return {
          itemName: item.itemName,
          itemCode: item.itemCode,
          unitCode: item.unitCode,
          itemSpec: item.itemSpec,
          drawingNo: item.drawingNo,
          quantity: item.quantity,
          deliveryDate: item.demandDate,
          needQuantity: item.quantity,
          needDate: item.demandDate,
          bizNo: item.bizNo,
          bizLineNo: item.lineNo,
          dataType: '04',
          textMap: {
            dataType$DICT_TEXT_: '销售预测',
          },
        };
      });
    tableRef.value?.insert(datas);
  }

  function selectOrganizeOredr(rows: Recordable) {
    const organizeKey = getLineKeyByType('02');
    const datas = rows
      .filter((item: Recordable) => {
        return !organizeKey.includes(`${item.bizNo}:${item.lineNo}`);
      })
      .map((item: Recordable) => {
        return {
          itemName: item.itemName,
          itemCode: item.itemCode,
          unitCode: item.unitCode,
          itemSpec: item.itemSpec,
          drawingNo: item.drawingNo,
          quantity: item.quantity,
          deliveryDate: item.demandDate,
          needQuantity: item.quantity,
          needDate: item.demandDate,
          bizNo: item.bizNo,
          bizLineNo: item.lineNo,
          dataType: '02',
          textMap: {
            dataType$DICT_TEXT_: '组织需求',
          },
        };
      });
    tableRef.value?.insert(datas);
  }

  function getLineKeyByType(type: string) {
    const { fullData = [] } = tableRef.value?.getTableData() || {};
    const salesKey: string[] = [];
    fullData.forEach((item: Recordable) => {
      if (item.dataType === type) {
        salesKey.push(`${item.bizNo}:${item.bizLineNo}`);
      }
    });
    return salesKey;
  }

  defineExpose({
    setData,
    validate,
    getData,
  });
</script>
