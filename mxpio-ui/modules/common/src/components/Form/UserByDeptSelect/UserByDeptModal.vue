<template>
  <BasicModal
    destroyOnClose
    width="1200px"
    @register="registerModal"
    title="请选择用户"
    @ok="handleSubmit"
    :bodyStyle="{ padding: '2px' }"
  >
    <FormItemRest>
      <a-row :gutter="10">
        <a-col :span="7">
          <a-card :style="{ minHeight: '550px', overflow: 'auto' }" title="部门列表" size="small">
            <ATree
              v-if="treeData.length"
              :defaultExpandAll="true"
              :checkStrictly="false"
              :treeData="treeData"
              :fieldNames="{ key: 'id', title: 'deptName' }"
              @select="treeSelect"
            />
          </a-card>
        </a-col>
        <a-col :span="17">
          <a-card
            :style="{ minHeight: '550px', overflow: 'auto' }"
            title="用户列表"
            size="small"
            class="user-warp"
          >
            <FormItem>
              <VxeBasicTable
                ref="tableRef"
                v-bind="gridOptions"
                @checkbox-change="handleCheckboxChange"
                @checkbox-all="handleCheckboxAll"
                @data-change="handleDataChange"
              >
                <template #tools>
                  <a-popover title="已选择" v-if="multiple">
                    <template #content>
                      <PopoverTable :dataSource="selectionRows" @remove="hanldleRemove" />
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
  import { ref, reactive, nextTick } from 'vue';
  import { BasicModal, useModalInner, VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridPropTypes, VxeGridInstance } from '@mxpio/components';
  import {
    Tree as ATree,
    Row as ARow,
    Col as ACol,
    Card as ACard,
    FormItemRest,
    FormItem,
    Popover as APopover,
  } from 'ant-design-vue';
  import { deptList, deptWithinUser, getUserList } from '@mxpio/api';
  import { getVxeTableQueryParams } from '@mxpio/utils';
  import type { VxeTableQueryParams } from '@mxpio/utils';
  import { columns, searchFormSchema } from './user.data';
  import PopoverTable from './PopoverTable.vue';

  defineOptions({ name: 'UserByDeptModal' });

  const props = defineProps({
    multiple: { type: Boolean, default: true },
  });

  const emit = defineEmits(['success', 'register']);
  let treeData = ref([]);
  let deptCode = ref('');
  const selectionRows = ref<any[]>([]);
  const tableRef = ref<VxeGridInstance>();
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    getDept();
    selectionRows.value = data?.selectUser || [];

    await tableRef.value?.commitProxy('query');
  });

  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'UserByDeptSelectList',
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
      keyField: 'username',
    },
  });

  // 获取部门树
  async function getDept() {
    const res = await deptList();
    treeData.value = res;
  }

  // 获取用户列表
  function loadData({ page, form, sorts }: VxeTableQueryParams) {
    const params = getVxeTableQueryParams({
      page,
      form,
      sorts,
    });
    // 查询部门用户
    if (deptCode.value) {
      return deptWithinUser(deptCode.value, params);
    }
    // 查询所有用户
    return getUserList(params);
  }

  // 选择
  async function handleSubmit() {
    try {
      if (props.multiple) {
        const ids = selectionRows.value?.map((item) => item.username);
        emit('success', ids, selectionRows.value);
      } else {
        const row = tableRef.value?.getRadioRecord(true);
        emit('success', [row?.username], [row]);
      }
      closeModal();
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  // 选择部门
  function treeSelect(selectedKeys: string[]) {
    deptCode.value = selectedKeys[0];
    tableRef.value?.commitProxy('query');
  }

  // 移除已选择的行
  function hanldleRemove(row: Recordable) {
    const index = selectionRows.value.findIndex((item) => item.username === row.username);
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
      const existUserNames = selectionRows.value.map((item) => item.username);
      if (!existUserNames.includes(row.username)) {
        selectionRows.value.push(row);
      }
    } else {
      // 取消选中：移除行数据
      selectionRows.value = selectionRows.value.filter((item) => item.username !== row.username);
    }
  }

  // 全选/取消全选
  function handleCheckboxAll({ checked }) {
    const allData = tableRef.value?.getData() || [];
    if (checked) {
      // 全选：添加当前页所有数据
      const existUserNames = selectionRows.value.map((item) => item.username);
      allData.forEach((r) => {
        if (!existUserNames.includes(r.username)) {
          selectionRows.value.push(r);
        }
      });
    } else {
      // 取消全选：移除当前页所有数据
      const allUserNames = allData.map((r) => r.username);
      selectionRows.value = selectionRows.value.filter(
        (item) => !allUserNames.includes(item.username),
      );
    }
  }

  // 表格数据变化
  function handleDataChange() {
    nextTick(() => {
      if (selectionRows.value.length) {
        tableRef.value?.setCheckboxRow(selectionRows.value, true);
      }
    });
  }
</script>
<style lang="less">
  .user-warp {
    .ant-pagination-options-size-changer {
      width: auto !important;
    }
  }
</style>
