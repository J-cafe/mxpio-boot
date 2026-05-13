<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions">
    <template #measurement="{ row }">
      <a @click="handleBySample(row)">{{ row.checkDetectNum }}/{{ row.detectQuantity }}</a>
    </template>
  </VxeBasicTable>
  <QualityBySampleModal @register="registerModal" />
</template>
<script lang="ts" setup>
  import { ref, computed, onMounted, onUnmounted } from 'vue';
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { ibsimpleListApi } from '@mxpio/bizcommon';
  import XEUtils from 'xe-utils';
  import { useMessage } from '@mxpio/hooks';
  import { setDataCrud } from '@mxpio/utils';
  import QualityBySampleModal from './QualityBySampleModal.vue';
  import emitter from './eventBus.js';

  defineOptions({ name: 'SimpleDetailTable' });

  const { createMessage } = useMessage();
  const [registerModal, { openModal }] = useModal();
  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const loading = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const formData: Recordable = {};

  const detailColumns: VxeGridPropTypes.Columns = [
    { type: 'checkbox', width: 40 },
    { type: 'seq', width: 60 },
    { title: '样本编码', field: 'simpleCode' },
    {
      title: '样本检验结论',
      field: 'testResult',
      editRender: {
        name: 'DictSelect',
        props: (params) => {
          console.log(params);
          const { row } = params;
          return {
            disabled: row.checkDetectNum !== row.detectQuantity,
            dictCode: 'MB_QUALITY_CHECK_RESULT',
          };
        },
      },
    },
    {
      title: '实测值',
      field: 'measurement',
      slots: { default: 'measurement' },
    },
    { title: '备注', field: 'memo', editRender: { name: 'AInput' } },
  ];

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'SimpleDetailTable',
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
            content: '合格',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:check-underline',
                disabled: isDisabled.value,
              },
              attrs: {
                class: 'ml-2',
              },
              events: {
                click: handleCheck,
              },
            },
          },
          {
            content: '不合格',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:close',
                disabled: isDisabled.value,
              },
              attrs: {
                class: 'ml-2',
              },
              events: {
                click: handleClose,
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
        testResult: [{ required: true, message: '请选择样本检验结论', trigger: 'change' }],
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
      const res = await ibsimpleListApi(formData.value.bizNo);
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

  async function getData() {
    try {
      const data: Recordable | undefined = tableRef.value?.getRecordset();
      if (!data) {
        return false;
      }
      const inspectionBillSimpleList = setDataCrud(data, isUpdate.value);
      return {
        inspectionBillSimpleList,
      };
    } catch (err) {
      console.error(err);
    }
  }

  function handleBySample(row: Recordable) {
    openModal(true, {
      bizNo: formData.value.bizNo,
      simpleCode: row.simpleCode,
      sampleList: dataSource.value,
      isUpdate: isUpdate.value,
      disabled: isDisabled.value,
    });
  }

  function handleCheck() {
    const selectRows = tableRef.value?.getCheckboxRecords() || [];
    if (selectRows.length === 0) {
      createMessage.warning('请选择样本');
    } else {
      selectRows.forEach((item: Recordable) => {
        XEUtils.assign(item, {
          testResult: '1',
          textMap: {
            testResult$DICT_TEXT_: '合格',
          },
        });
      });
    }
  }

  function handleClose() {
    const selectRows = tableRef.value?.getCheckboxRecords() || [];
    if (selectRows.length === 0) {
      createMessage.warning('请选择样本');
    } else {
      selectRows.forEach((item: Recordable) => {
        XEUtils.assign(item, {
          testResult: '0',
          textMap: {
            testResult$DICT_TEXT_: '不合格',
          },
        });
      });
    }
  }

  onMounted(() => {
    emitter.on('refresh-all', loadData);
  });

  onUnmounted(() => {
    emitter.off('refresh-all', loadData);
  });
  // function handleSuccess() {
  //   loadData();
  // }

  defineExpose({
    setData,
    validate,
    getData,
  });
</script>
