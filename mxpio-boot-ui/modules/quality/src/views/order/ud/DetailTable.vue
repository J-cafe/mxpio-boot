<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions">
    <template #inspectionBillNo="{ row }">
      <a @click="handleOrder(row)">{{ row.inspectionBillNo }}</a>
    </template>
  </VxeBasicTable>
  <QualityUDSplitModal @register="registerModal" @success="handleSplitOk" />
  <QualityOrderModal @register="registerOrderModal" />
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { setDataCrud } from '@mxpio/utils/src/common';
  import { udLineListApi, qualityByCodeApi } from '@mxpio/bizcommon';
  import QualityUDSplitModal from './QualityUDSplitModal.vue';
  import QualityOrderModal from '../quality/QualityOrderModal.vue';
  import { useMessage } from '@mxpio/hooks';
  import XEUtils from 'xe-utils';
  import Big from 'big.js';

  defineOptions({ name: 'DetailTable' });

  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const formData: Recordable = {};
  const [registerModal, { openModal }] = useModal();
  const [registerOrderModal, { openModal: openOrderModal }] = useModal();
  const { createMessage } = useMessage();
  const options = [
    { label: '让步接收', value: '1' },
    { label: '判退', value: '2' },
    { label: '返工', value: '3' },
    { label: '返修', value: '4' },
    { label: '报废', value: '5' },
  ];

  const detailColumns: VxeGridPropTypes.Columns = [
    { type: 'radio', width: 40 },
    { title: '序号', type: 'seq', width: 50, align: 'center' },
    {
      title: '使用决策',
      field: 'applyDecision',
      editRender: {
        name: 'ASelect',
        props: (params) => {
          const { row } = params;
          const originBizType = row.inspectionBill?.originBizType;
          const options = [
            { label: '让步接收', value: '1' },
            {
              label: '判退',
              value: '2',
              disabled: ['2', '4', '5'].includes(originBizType),
            },
            { label: '返工', value: '3', disabled: originBizType === '1' },
            {
              label: '返修',
              value: '4',
              disabled: ['1', '4', '5'].includes(originBizType),
            },
            { label: '报废', value: '5', disabled: originBizType === '1' },
          ];
          return {
            options: options,
          };
        },
        options: options,
      },
      // formatter: 'dictText',
      width: 150,
    },
    { title: '行号', field: 'lineNo', width: 60 },
    { title: '数量', field: 'applyQuantity', width: 100 },
    {
      title: '质检单号',
      field: 'inspectionBillNo',
      width: 120,
      slots: { default: 'inspectionBillNo' },
    },
    { title: '业务类型', field: 'inspectionBill.textMap.busiType$DICT_TEXT_', width: 100 },
    { title: '源单类型', field: 'inspectionBill.textMap.originBizType$DICT_TEXT_', width: 100 },
    { title: '源单编号', field: 'inspectionBill.originBizNo', width: 100 },
    { title: '报检组织编号', field: 'inspectionBill.applyOrgCode', width: 120 },
    { title: '报检组织名称', field: 'inspectionBill.applyOrgName', width: 120 },
    { title: '物料编码', field: 'inspectionBill.itemCode', width: 100 },
    { title: '物料名称', field: 'inspectionBill.itemName', width: 100 },
    { title: '规格型号', field: 'inspectionBill.itemSpec', width: 100 },
    { title: '图号', field: 'inspectionBill.drawingNo', width: 100 },
    { title: '单位', field: 'inspectionBill.textMap.unitCode$DICT_TEXT_', width: 100 },
    { title: '报检数量', field: 'inspectionBill.quantity', width: 100 },
    { title: '检验数量', field: 'inspectionBill.checkQuantity', width: 100 },
    { title: '不合格数量', field: 'inspectionBill.unqualifiedQuantity', width: 100 },
    {
      title: '检验员',
      field: 'inspectionBill.textMap.inspector$DICT_TEXT_',
      formatter: 'dictText',
      width: 100,
    },
    { title: '检验开始时间', field: 'inspectionBill.inspectionStartTime', width: 120 },
    { title: '检验完成时间', field: 'inspectionBill.inspectionFinishTime', width: 120 },
  ];

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'udDetailTable',
      keepSource: true,
      height: 300,
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
            content: '拆分',
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
                  handleSplit();
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
        applyDecision: [{ required: true, message: '请选择使用决策', trigger: 'change' }],
      },
      pagerConfig: {
        enabled: false,
      },
      radioConfig: {
        trigger: 'row',
      },
    };
  });

  async function setData(data: Recordable) {
    isDisabled.value = !!data?.disabled;
    isUpdate.value = data?.isUpdate || false;
    const record = data.record || {};
    formData.value = record;
    const res = await udLineListApi(record.bizNo);
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
      const unqualifiedDisposalLines = setDataCrud(data, isUpdate.value);
      return {
        unqualifiedDisposalLines,
      };
    } catch (err) {
      console.error(err);
    }
  }

  function handleSplit() {
    const row = tableRef.value?.getRadioRecord();
    if (!row) {
      createMessage.error('请选择一条数据');
      return;
    }
    openModal(true, {
      record: row,
    });
  }

  function handleSplitOk(data) {
    const row = tableRef.value?.getRadioRecord();
    if (!row) {
      return;
    }
    XEUtils.set(row, 'applyQuantity', Big(row.applyQuantity).minus(data.splitQuantity).toNumber());
    const splitRow = {
      ...row,
      applyQuantity: data.splitQuantity,
      applyDecision: '',
      textMap: {
        ...row.textMap,
        applyDecision$DICT_TEXT_: '',
      },
    };
    delete splitRow._X_ROW_KEY;
    delete splitRow.id;
    tableRef.value?.insertAt(splitRow, -1);
  }

  async function handleOrder(row) {
    try {
      const res = await qualityByCodeApi(row.inspectionBillNo);

      openOrderModal(true, {
        record: res,
        disabled: true,
        isUpdate: true,
      });
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
