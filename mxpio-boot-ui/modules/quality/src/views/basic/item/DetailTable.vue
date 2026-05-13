<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { setDataCrud } from '@mxpio/utils/src/common';
  import { paramlineListApi } from '@mxpio/bizcommon';
  import XEUtils from 'xe-utils';

  defineOptions({ name: 'ItemDetailTable' });

  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const formData: Recordable = {};

  const detailColumns: VxeGridPropTypes.Columns = [
    { type: 'checkbox', width: 40 },
    {
      title: '序号',
      field: 'num',
      width: 50,
      align: 'center',
    },
    {
      title: '选项',
      field: 'parameter',
      editRender: {
        name: 'AInput',
      },
      width: 150,
    },
    {
      title: '是否默认',
      field: 'defaultParam',
      editRender: {
        name: 'ASwitch',
        props: {
          checkedValue: true,
        },
        events: {
          change: handleDefaultParam,
        },
      },
      width: 150,
    },
  ];

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'ItemDetailTable',
      keepSource: true,
      minHeight: '200px',
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
            content: '新增',
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
                  const insertData = getInsertData();
                  tableRef.value?.insert(insertData);
                },
              },
            },
          },
          {
            content: '删除',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                danger: true,
                preIcon: 'mdi:delete-forever',
                disabled: isDisabled.value,
              },
              events: {
                click: async () => {
                  tableRef.value?.removeCheckboxRow();
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
        parameter: [{ required: true, message: '请输入', trigger: 'change' }],
      },
      pagerConfig: {
        enabled: false,
      },
    };
  });

  async function setData(data: Recordable) {
    isDisabled.value = !!data?.disabled;
    isUpdate.value = data?.isUpdate || false;
    const record = data.record || {};
    formData.value = record;
    if (!record.code) {
      return;
    }
    const res = await paramlineListApi(record.code);
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
      const paramList = setDataCrud(data, isUpdate.value);
      return {
        paramList,
      };
    } catch (err) {
      console.error(err);
    }
  }

  function getInsertData() {
    const maxNum = getMaxNum();
    return {
      num: maxNum ? maxNum + 1 : 1,
    };
  }

  function getTableData() {
    return tableRef.value?.getTableData();
  }

  function getMaxNum() {
    let maxNum = 0;
    const { fullData = [] } = tableRef.value?.getTableData() || {};
    if (fullData) {
      const numList = fullData.map((item: Recordable) => item.num);
      maxNum = numList.length > 0 ? Math.max(...numList) : 0;
    }
    return maxNum;
  }

  function handleDefaultParam(params: any, value: boolean) {
    const { row } = params;
    if (value) {
      const { fullData = [] } = tableRef.value?.getTableData() || {};
      fullData.forEach((item) => {
        if (item.defaultParam && item._X_ROW_KEY !== row._X_ROW_KEY) {
          XEUtils.set(item, 'defaultParam', false);
        }
      });
    }
  }

  defineExpose({
    setData,
    validate,
    getData,
    getTableData,
  });
</script>
