<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions">
    <template #measurement="{ row }">
      <a @click="handleBySample(row)">{{ row.checkSimpleNum }}/{{ row.simpleQuantity }}</a>
    </template>
  </VxeBasicTable>
  <QualityByDetectModal @register="registerModal" @success="handleSuccess" />
</template>
<script lang="ts" setup>
  import { ref, computed, onMounted, onUnmounted } from 'vue';
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { ibdetectListApi } from '@mxpio/bizcommon';
  import QualityByDetectModal from './QualityByDetectModal.vue';
  import emitter from './eventBus.js';

  defineOptions({ name: 'DetectDetailTable' });

  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const loading = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const formData: Recordable = {};
  const [registerModal, { openModal }] = useModal();

  const detailColumns: VxeGridPropTypes.Columns = [
    { type: 'seq', width: 60 },
    { title: '检验顺序', field: 'sort', width: 100, sortable: true },
    {
      title: '项目来源',
      field: 'detectSource',
      width: 100,
      formatter: 'dictText',
    },
    { title: '检测项目编号', field: 'inspectionItemCode', width: 140 },
    { title: '检测项目名称', field: 'inspectionItemName', width: 120 },
    { title: '项目样本数量', field: 'simpleQuantity', width: 120 },
    { title: '项目合格数量', field: 'qualifiedQuantity', width: 120 },
    { title: '项目不合格数量', field: 'unqualifiedQuantity', width: 120 },
    {
      title: '实测值',
      field: 'measurement',
      slots: { default: 'measurement' },
      width: 100,
    },
    {
      title: '项目类型',
      field: 'inspectionItemType',
      width: 100,
      formatter: 'dictText',
    },
    {
      title: '项目分类',
      field: 'inspectionItemClass',
      width: 100,
      formatter: 'dictText',
    },
    {
      title: '缺陷等级',
      field: 'inspectionDefectGrade',
      width: 100,
      formatter: 'dictText',
    },
    { title: '检验标准', field: 'inspectionDtandard', width: 100 },
    { title: '检测工具', field: 'inspectionDetectionTool', width: 100 },
    { title: '检验方法', field: 'inspectionDetectionMethod', width: 100 },
    {
      title: '比较符',
      field: 'comparator',
      width: 120,
      slots: {
        default: ({ row }) => {
          return row.comparator === 'include' ? '[]' : row.comparator;
        },
      },
    },
    { title: '目标值', field: 'targetValue', width: 120 },
    { title: '单位', field: 'units', width: 120, formatter: 'dictText' },
    { title: '最大值', field: 'maxValue', width: 120 },
    { title: '最小值', field: 'minValue', width: 120 },
    { title: '备注', field: 'memo', width: 120 },
  ];

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'DetectDetailTable',
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
        buttons: [],
        import: false,
        print: false,
        export: false,
        refresh: false,
        custom: false,
      },
      editRules: {
        testResult: [
          {
            required: true,
            message: '请选择样本检验结论',
            trigger: 'change',
          },
        ],
      },
      pagerConfig: {
        enabled: false,
      },
      loading: loading.value,
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
    loadData();
  }

  async function loadData() {
    loading.value = true;
    try {
      const res = await ibdetectListApi(formData.value.bizNo);
      dataSource.value = res;
      tableRef.value?.loadData(res);
    } finally {
      setTimeout(() => {
        loading.value = false;
      }, 200);
    }
  }

  async function validate() {
    let errMap = await tableRef.value?.validate(true);
    if (errMap) {
      return Promise.reject(errMap);
    }
    return Promise.resolve();
  }

  function handleBySample(row: Recordable) {
    openModal(true, {
      bizNo: formData.value.bizNo,
      detectCode: row.detectCode,
      itemList: dataSource.value,
      isUpdate: isUpdate.value,
      disabled: isDisabled.value,
    });
  }

  function handleSuccess() {
    loadData();
  }
  onMounted(() => {
    emitter.on('refresh-all', loadData);
  });

  onUnmounted(() => {
    emitter.off('refresh-all', loadData);
  });

  defineExpose({
    setData,
    validate,
  });
</script>
