<template>
  <div>
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
    <FullcalendarModal
      ref="fullcalendarModalRef"
      @success="handleSuccess"
      @register="registerFullcalendarModal"
    />
  </div>
</template>
<script lang="ts" setup>
  import { useListCrudHook } from '@mxpio/common';
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import { mainPlanLineColumns } from './mainPlan.data';
  import { useMessage } from '@mxpio/hooks';
  import { mpsLineListApi } from '@mxpio/bizcommon';
  import { watch } from 'vue';
  import FullcalendarModal from './FullcalendarModal.vue';

  const componentName = 'QualityChainsWithInItem';
  defineOptions({ name: componentName });

  const props = defineProps({
    code: {
      type: String,
      default: () => '',
    },
  });

  const { createMessage } = useMessage();
  const [registerFullcalendarModal, { openModal: openFullcalendarModal }] = useModal();

  watch(
    () => props.code,
    () => {
      if (!props?.code) {
        tableRef.value?.loadData([]);
        return;
      }
      tableRef.value?.commitProxy('query');
    },
    {
      deep: true,
    },
  );

  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    columns: mainPlanLineColumns,
    pageApi: () => {
      return mpsLineListApi(props.code);
    },
    vxeGridOptions: {
      rowConfig: {
        keyField: 'itemCode',
      },
      toolbarConfig: {
        buttons: [
          {
            content: '日历',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:calendar-month',
              },
              attrs: {
                class: 'ml-2',
              },
              events: {
                click: () => {
                  handleFullcalendar();
                },
              },
            },
          },
        ],
        export: false,
        import: false,
      },
      proxyConfig: {
        autoLoad: false,
      },
      pagerConfig: {
        enabled: false,
      },
      formConfig: {
        enabled: false,
      },
      tableClass: '!px-0 !py-0',
      height: 600,
    },
    module: 'erp',
  });

  function handleFullcalendar() {
    if (!props?.code) {
      createMessage.error('请先选择主数据');
      return;
    }
    openFullcalendarModal(true, {
      code: props.code,
    });
  }

  function handleSuccess() {
    tableRef.value?.commitProxy('query');
  }
</script>
