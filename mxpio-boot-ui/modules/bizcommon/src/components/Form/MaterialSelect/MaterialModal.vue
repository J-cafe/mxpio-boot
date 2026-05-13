<template>
  <BasicModal
    destroyOnClose
    width="1200px"
    @register="registerModal"
    title="请选择物料"
    @ok="handleSubmit"
    :bodyStyle="{ padding: '2px' }"
  >
    <FormItemRest>
      <a-row :gutter="10">
        <a-col :span="7">
          <a-card :style="{ minHeight: '550px', overflow: 'auto' }" title="物料组" size="small">
            <BasicTree :field-names="fieldNames" :treeData="treeData" @select="treeSelect" />
          </a-card>
        </a-col>
        <a-col :span="17">
          <a-card
            :style="{ minHeight: '550px', overflow: 'auto' }"
            title="物料列表"
            size="small"
            class="user-warp"
          >
            <FormItem>
              <VxeBasicTable
                ref="tableRef"
                v-bind="gridOptions"
                @checkbox-change="handleCheckboxChange"
                @checkbox-all="handleCheckboxAll"
              >
                <template #tools>
                  <a-popover title="已选择" v-if="multiple">
                    <template #content>
                      <PopoverTable :dataSource="selectionRows" @remove="handleRemove" />
                    </template>
                    <a style="margin-left: 10px">已选择:{{ selectionRows.length }}条</a>
                  </a-popover>
                </template>
              </VxeBasicTable>
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
  import {
    Row as ARow,
    Col as ACol,
    Card as ACard,
    FormItemRest,
    FormItem,
    Popover as APopover,
  } from 'ant-design-vue';
  import { itemGroup, itemWithSubExtends } from '../../../api/technology/material';
  import { getVxeTableQueryParams } from '@mxpio/utils';
  import type { VxeTableQueryParams } from '@mxpio/utils';
  import type { TreeProps } from '@mxpio/components';
  import { columns, searchFormSchema } from './material.data';
  import PopoverTable from './PopoverTable.vue';

  defineOptions({ name: 'MaterialSelectModal' });

  const props = defineProps({
    multiple: { type: Boolean, default: true },
    filters: { type: Object, default: () => ({}) },
  });

  const fieldNames: TreeProps['fieldNames'] = {
    children: 'children',
    title: 'groupName',
    key: 'groupCode',
  };

  const itemGroupCode = ref('');

  const emit = defineEmits(['success', 'register']);
  let treeData = ref([]);
  const selectionRows = ref<any[]>([]);

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async () => {
    setModalProps({ confirmLoading: false });
    loadTreeData();
    await tableRef.value?.commitProxy('query');
  });

  const tableRef = ref<VxeGridInstance>();
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'MaterialSelectList',
    tableClass: '!px-2 !py-0',
    height: 500,
    columns: [
      {
        type: props.multiple ? 'checkbox' : 'radio',
        width: 40,
      },
      ...columns,
    ],
    toolbarConfig: {
      slots: {
        buttons: 'tools',
      },
    },
    formConfig: {
      enabled: true,
      items: searchFormSchema,
    },
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
    rowConfig: {
      keyField: 'itemCode',
    },
  });

  async function loadData({ page, form, sorts }: VxeTableQueryParams) {
    const params = getVxeTableQueryParams({
      page,
      form,
      sorts,
      filters: Object.assign({}, props.filters, {
        'itemGroupCode@EQ': itemGroupCode.value || undefined,
      }),
    });
    const res = await itemWithSubExtends(params);
    return res;
  }

  // 获取物料组
  async function loadTreeData() {
    const res = await itemGroup();
    if (res) {
      treeData.value = res;
    }
  }

  function treeSelect(keys: string[]) {
    itemGroupCode.value = keys[0];
    tableRef.value?.commitProxy('query');
  }

  // 选择
  async function handleSubmit() {
    try {
      if (props.multiple) {
        const ids = selectionRows.value?.map((item) => item.itemCode);
        emit('success', ids?.join(','), selectionRows.value);
      } else {
        const row = tableRef.value?.getRadioRecord(true);
        emit('success', row?.itemCode, [row]);
      }
      closeModal();
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  // 移除已选择的行
  function handleRemove(row: Recordable) {
    const index = selectionRows.value.findIndex((item) => item.itemCode === row.itemCode);
    if (index > -1) {
      selectionRows.value.splice(index, 1);
    }
    // 同步更新表格选中状态
    tableRef.value?.clearCheckboxRow();
    tableRef.value?.setCheckboxRow(selectionRows.value, true);
  }

  // 表格选择变化（单选/单行勾选）
  function handleCheckboxChange({ checked, row }) {
    if (checked) {
      // 选中：添加行数据
      const existItemCodes = selectionRows.value.map((item) => item.itemCode);
      if (!existItemCodes.includes(row.itemCode)) {
        selectionRows.value.push(row);
      }
    } else {
      // 取消选中：移除行数据
      selectionRows.value = selectionRows.value.filter((item) => item.itemCode !== row.itemCode);
    }
  }

  // 全选/取消全选
  function handleCheckboxAll({ checked }) {
    const allData = tableRef.value?.getData() || [];
    if (checked) {
      // 全选：添加当前页所有数据
      const existItemCodes = selectionRows.value.map((item) => item.itemCode);
      allData.forEach((r) => {
        if (!existItemCodes.includes(r.itemCode)) {
          selectionRows.value.push(r);
        }
      });
    } else {
      // 取消全选：移除当前页所有数据
      const allItemCodes = allData.map((r) => r.itemCode);
      selectionRows.value = selectionRows.value.filter(
        (item) => !allItemCodes.includes(item.itemCode),
      );
    }
  }
</script>
<style lang="less"></style>
