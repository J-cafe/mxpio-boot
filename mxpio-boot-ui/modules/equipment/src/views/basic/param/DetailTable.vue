<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, computed, nextTick } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance } from '@mxpio/components';
  import { getEqpParamDetailList } from '@mxpio/bizcommon';
  import { detailColumns } from './eqpParam.data';
  import { setDataCrud } from '@mxpio/utils/src/common';

  defineOptions({ name: 'EqpParamDetailTable' });

  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'EqpParamDetailTable',
      keepSource: true,
      minHeight: '200px',
      tableClass: '!px-0 !py-0',
      columns: detailColumns,
      data: dataSource.value,
      proxyConfig: { enabled: false },
      toolbarConfig: {
        buttons: [
          {
            content: '新增',
            buttonRender: {
              name: 'AButton',
              attrs: {
                class: 'ml-2 mr-2',
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
                click: () => {
                  tableRef.value?.removeCheckboxRow();
                },
              },
            },
          },
        ],
        import: false,
        print: false,
        export: false,
      },
      editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true,
        autoClear: false,
        enabled: !isDisabled.value,
      },
      editRules: {
        memberId: [{ required: true, message: '请选择成员', trigger: 'change' }],
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
    const res = await getEqpParamDetailList(record.id);
    dataSource.value = res;
    nextTick(() => {
      tableRef.value?.loadData(dataSource.value);
    });
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
      const commParametersDetails = setDataCrud(data, isUpdate.value);
      return {
        commParametersDetails,
      };
    } catch (err) {
      console.error(err);
    }
  }

  function getMaxNum() {
    const instance = tableRef.value;
    if (!instance) return 0;
    const { removeRecords = [] }: { removeRecords?: Recordable[] } =
      tableRef.value?.getRecordset() || {};
    const { fullData = [] }: { fullData?: Recordable[] } = tableRef.value?.getTableData() || {};
    let maxNum = 0;
    removeRecords.concat(fullData).forEach((item) => {
      if (Number(item.num) > maxNum) {
        maxNum = Number(item.num);
      }
    });
    return maxNum;
  }

  function getInsertData() {
    const maxNum = getMaxNum();
    return {
      num: maxNum + 1,
      isEnable: '1',
    };
  }

  defineExpose({
    setData,
    validate,
    getData,
  });
</script>
