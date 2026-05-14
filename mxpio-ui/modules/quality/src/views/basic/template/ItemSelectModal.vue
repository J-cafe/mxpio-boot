<template>
  <BasicModal
    destroyOnClose
    width="1200px"
    @register="registerModal"
    title="请选择检测项"
    @ok="handleSubmit"
    :bodyStyle="{ padding: '2px' }"
  >
    <FormItemRest>
      <FormItem>
        <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
      </FormItem>
    </FormItemRest>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, useModalInner, VxeBasicTable } from '@mxpio/components';
  import { FormItemRest, FormItem } from 'ant-design-vue';
  import { iiPageApi } from '@mxpio/bizcommon';
  import { columns, searchFormSchema } from './item.data';
  import { useListCrudHook } from '@mxpio/common';

  const componentName = 'QualityItemSelectModal';
  defineOptions({ name: componentName });

  const props = defineProps({
    multiple: { type: Boolean, default: true },
    filters: { type: Object, default: () => ({}) },
  });

  const emit = defineEmits(['success', 'register']);
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async () => {
    setModalProps({ confirmLoading: false });
    tableRef.value?.commitProxy('query');
  });

  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    columns: [
      {
        type: props.multiple ? 'checkbox' : 'radio',
        width: 40,
      },
      ...columns,
    ],
    searchFormSchema,
    pageApi: iiPageApi,
    vxeGridOptions: {
      toolbarConfig: {
        buttons: [],
        import: false,
      },
      rowConfig: {
        keyField: 'code',
      },
      height: 500,
      proxyConfig: {
        autoLoad: false,
      },
      checkboxConfig: {
        trigger: 'row',
      },
      radioConfig: {
        trigger: 'row',
      },
    },
    filters: {
      'bpmnStatus@EQ': '03',
    },
  });

  // 选择
  async function handleSubmit() {
    try {
      if (props.multiple) {
        const rows = tableRef.value?.getCheckboxRecords(true);
        const ids = rows?.map((item) => item.code);
        emit('success', ids?.join(','), rows);
      } else {
        const row = tableRef.value?.getRadioRecord(true);
        emit('success', row.code, row);
      }
      closeModal();
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
<style lang="less"></style>
