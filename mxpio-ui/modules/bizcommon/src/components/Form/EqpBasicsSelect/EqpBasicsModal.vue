<template>
  <BasicModal
    destroyOnClose
    width="1200px"
    @register="registerModal"
    title="请选择设备"
    @ok="handleSubmit"
    :bodyStyle="{ padding: '2px' }"
  >
    <FormItemRest>
      <a-row :gutter="10">
        <a-col :span="7">
          <a-card :style="{ minHeight: '550px', overflow: 'auto' }" title="设备分类" size="small">
            <BasicTree :field-names="fieldNames" :treeData="treeData" @select="treeSelect" />
          </a-card>
        </a-col>
        <a-col :span="17">
          <a-card
            :style="{ minHeight: '550px', overflow: 'auto' }"
            title="设备列表"
            size="small"
            class="user-warp"
          >
            <FormItem>
              <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
            </FormItem>
          </a-card>
        </a-col>
      </a-row>
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
    BasicTree,
  } from '@mxpio/components';
  import { Row as ARow, Col as ACol, Card as ACard, FormItemRest, FormItem } from 'ant-design-vue';
  import { eqpCategoryTreeApi, eqpBasicsPageApi } from '../../../api';
  import { getVxeTableQueryParams } from '@mxpio/utils';
  import type { VxeTableQueryParams } from '@mxpio/utils';
  import type { TreeProps } from '@mxpio/components';
  import { columns, searchFormSchema } from './eqpBasics.data';

  defineOptions({ name: 'EqpBasicsModal' });

  const props = defineProps({
    multiple: { type: Boolean, default: true },
    filters: { type: Object, default: () => ({}) },
  });

  const fieldNames: TreeProps['fieldNames'] = {
    children: 'children',
    title: 'name',
    key: 'code',
  };

  const eqpTypeCode = ref(null);

  const emit = defineEmits(['success', 'register']);
  let treeData = ref([]);

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async () => {
    setModalProps({ confirmLoading: false });
    loadTreeData();
    tableRef.value?.commitProxy('query');
  });

  const tableRef = ref<VxeGridInstance>();
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'EqpSelectList',
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
      filters: Object.assign({}, props.filters, { 'eqpTypeId@EQ': eqpTypeCode.value }),
    });
    const res = await eqpBasicsPageApi(params);
    return res;
  }

  // 获取物料组
  async function loadTreeData() {
    const res = await eqpCategoryTreeApi();
    if (res) {
      treeData.value = res;
    }
  }

  function treeSelect(keys) {
    eqpTypeCode.value = keys[0];
    tableRef.value?.commitProxy('query');
  }

  // 选择
  async function handleSubmit() {
    try {
      if (props.multiple) {
        const rows = tableRef.value?.getCheckboxRecords(true);
        const ids = rows?.map((item) => item.basicsCode);
        emit('success', ids?.join(','), rows);
      } else {
        const row = tableRef.value?.getRadioRecord(true);
        emit('success', row?.basicsCode, row);
      }
      closeModal();
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
<style lang="less"></style>
