<template>
  <BasicModal
    destroyOnClose
    width="1200px"
    @register="registerModal"
    title="请选择客户"
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
  import { ref, reactive } from 'vue';
  import {
    BasicModal,
    useModalInner,
    VxeBasicTable,
    VxeGridInstance,
    BasicVxeTableProps,
    VxeGridPropTypes,
  } from '@mxpio/components';
  import { FormItemRest, FormItem } from 'ant-design-vue';
  import { customerPage } from '../../../api/sales/customer';
  import { getVxeTableQueryParams } from '@mxpio/utils';
  import type { VxeTableQueryParams } from '@mxpio/utils';
  import { columns, searchFormSchema } from './customer.data';

  defineOptions({ name: 'CustomerSelectModal' });

  const props = defineProps({
    multiple: { type: Boolean, default: true },
    filters: { type: Object, default: () => ({}) },
  });

  const emit = defineEmits(['success', 'register']);

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async () => {
    setModalProps({ confirmLoading: false });
    tableRef.value?.commitProxy('query');
  });

  const tableRef = ref<VxeGridInstance>();
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'MaterialSelectList',
    columns: [
      {
        type: props.multiple ? 'checkbox' : 'radio',
        width: 40,
      },
      ...columns,
    ],
    toolbarConfig: {},
    formConfig: {
      enabled: true,
      items: searchFormSchema,
    },
    minHeight: 500,
    proxyConfig: {
      ajax: {
        query: async ({ page, form, sorts }: VxeGridPropTypes.ProxyAjaxQueryParams<any>) => {
          return loadData({ page, form, sorts });
        },
      },
      autoLoad: false,
    },
    checkboxConfig: {
      trigger: 'row',
    },
    radioConfig: {
      trigger: 'row',
    },
  });

  async function loadData({ page, form, sorts }: VxeTableQueryParams) {
    const params = getVxeTableQueryParams({
      page,
      form,
      sorts,
      filters: Object.assign({}, props.filters),
    });
    const res = await customerPage(params);
    return res;
  }

  // 选择
  async function handleSubmit() {
    try {
      if (props.multiple) {
        const rows = tableRef.value?.getCheckboxRecords(true);
        const ids = rows?.map((item) => item.pnCode);
        emit('success', ids?.join(','), rows);
      } else {
        const row = tableRef.value?.getRadioRecord(true);
        emit('success', row.pnCode, row);
      }
      closeModal();
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
<style lang="less"></style>
