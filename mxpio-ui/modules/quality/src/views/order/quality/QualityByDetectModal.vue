<template>
  <BasicModal
    width="80%"
    title="按项目检测"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <VxeBasicTable
      ref="tableRef"
      :tableClass="tableClass"
      :loading="loading"
      :editConfig="editConfig"
      v-bind="gridOptions"
    >
      <template #measurement="scope">
        <a-input
          :value="scope.row.measurement"
          v-if="scope.row.inspectionItemType === '1'"
          :type="
            scope.row.inspectionItem && scope.row.inspectionItemType === '1' ? 'number' : 'text'
          "
          @change="(e) => changeTargetValue(e.target.value, scope)"
        />
        <a-select
          v-else
          class="!w-full"
          :mode="scope.row.comparator === 'include' ? 'multiple' : 'default'"
          :value="scope.row.measurement"
          :options="targetValueOptions"
          @dropdown-visible-change="(open) => dropdownVisibleChange(scope.row, open)"
          @change="(value) => changeTargetValue(value, scope)"
        />
      </template>
      <template #measurementText="{ row }">
        <span>{{ getTargetValueText(row.measurement) }}</span>
      </template>
    </VxeBasicTable>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, reactive, computed } from 'vue';
  import { BasicModal, useModalInner, VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps } from '@mxpio/components';
  import { byDetectColumns } from './qualityOrder.data';
  import { ibdetectDetailListApi, qualityCheckApi, paramlineListApi } from '@mxpio/bizcommon';
  import { useMessage } from '@mxpio/hooks';
  import XEUtils from 'xe-utils';
  import { CuryTypeEnum } from '@mxpio/enums';
  import emitter from './eventBus.js';

  const componentName = 'QualityByDetectModal';
  defineOptions({ name: componentName });

  const emit = defineEmits(['success', 'register']);

  const tableRef = ref();
  let bizNo = ref<string>('');
  let detectCode = ref<string>('');
  let itemList = ref<Recordable[]>([]);
  let itemIndex = ref(0);
  const disabled = ref(false);
  const dataSource = ref([]);
  const loading = ref(false);
  const tableClass = '!px-0 !py-0';
  const targetValueOptions = ref([]);
  const { createMessage } = useMessage();
  const editConfig = computed(() => {
    return {
      trigger: 'click',
      mode: 'row',
      showStatus: true,
      autoClear: false,
      enabled: !disabled.value,
    };
  });

  const gridOptions = reactive<BasicVxeTableProps>({
    id: componentName,
    columns: byDetectColumns,
    data: dataSource.value,
    toolbarConfig: {
      buttons: [
        {
          content: '上一项',
          buttonRender: {
            name: 'AButton',
            props: {
              type: 'primary',
              preIcon: 'mdi:skip-previous',
            },
            attrs: {
              class: 'ml-2',
            },
            events: {
              click: handlePre,
            },
          },
        },
        {
          content: '下一项',
          buttonRender: {
            name: 'AButton',
            props: {
              type: 'primary',
              preIcon: 'mdi:skip-next',
            },
            attrs: {
              class: 'ml-2',
            },
            events: {
              click: handleNext,
            },
          },
        },
      ],
      tools: [],
    },
    pagerConfig: {
      enabled: false,
    },
    proxyConfig: {
      autoLoad: false,
    },
    keepSource: true,
    height: '450px',

    editRules: {
      measurement: [{ required: true, message: '请输入实测值', trigger: 'change' }],
    },
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    bizNo.value = data.bizNo;
    detectCode.value = data.detectCode;
    itemList.value = data.itemList;
    disabled.value = data.disabled;
    data.itemList.forEach((item, index) => {
      if (item.detectCode === detectCode.value) {
        itemIndex.value = index;
      }
    });
    loadData();
  });

  async function loadData() {
    loading.value = true;
    try {
      dataSource.value = await ibdetectDetailListApi(bizNo.value, detectCode.value);
      tableRef.value?.loadData(dataSource.value);
    } finally {
      setTimeout(() => {
        loading.value = false;
      }, 200);
    }
  }

  async function handleSubmit() {
    try {
      setModalProps({ confirmLoading: true });
      let errMap = await tableRef.value?.validate(true);
      if (errMap) {
        return;
      }
      await save();
      emit('success');
      emitter.emit('refresh-all');
      closeModal();
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  async function handleCancel() {
    emit('success');
    emitter.emit('refresh-all');
    closeModal();
  }

  async function handlePre() {
    if (itemIndex.value === 0) {
      createMessage.error('已经是第一项');
      return;
    }
    const { updateRecords } = tableRef.value?.getRecordset() || {};
    if (!disabled.value && updateRecords?.length > 0) {
      let errMap = await tableRef.value?.validate(true);
      if (errMap) {
        return;
      }
      await save();
    }
    itemIndex.value--;
    detectCode.value = itemList.value[itemIndex.value]?.detectCode;
    loadData();
  }

  async function handleNext() {
    if (itemIndex.value === itemList.value.length - 1) {
      createMessage.error('已经是最后一项');
      return;
    }
    const { updateRecords } = tableRef.value?.getRecordset() || {};
    if (!disabled.value && updateRecords?.length > 0) {
      let errMap = await tableRef.value?.validate(true);
      if (errMap) {
        return;
      }
      await save();
    }
    itemIndex.value++;
    detectCode.value = itemList.value[itemIndex.value]?.detectCode;
    loadData();
  }

  async function save() {
    const submitData = classifyIntoFormData();
    await qualityCheckApi(bizNo.value, submitData);
  }

  function dropdownVisibleChange(row, open) {
    if (open) {
      paramlineListApi(row.inspectionItemCode).then((res) => {
        targetValueOptions.value = res.map((item) => {
          return {
            label: item.parameter,
            value: item.parameter,
          };
        });
      });
    }
  }

  function changeTargetValue(value: string, scope) {
    XEUtils.set(scope.row, 'measurement', value);
    tableRef.value?.updateStatus(scope);
  }

  function getTargetValueText(targetValue) {
    if (targetValue instanceof Array) {
      return targetValue.join(',');
    } else {
      return targetValue;
    }
  }

  function classifyIntoFormData() {
    const { fullData = [] } = tableRef.value?.getTableData() || {};
    const data: Recordable[] = [];
    if (fullData.length > 0) {
      fullData.forEach((item) => {
        data.push({
          ...item,
          crudType: CuryTypeEnum.UPDATE,
        });
      });
    }
    return data;
  }
</script>
