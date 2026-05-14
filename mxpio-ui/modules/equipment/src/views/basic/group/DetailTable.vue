<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, computed, nextTick } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance } from '@mxpio/components';
  import { getEqpGroupMember } from '@mxpio/bizcommon';
  import { teamUserColumns } from './eqpGroup.data';
  import { setDataCrud } from '@mxpio/utils/src/common';

  defineOptions({ name: 'EqpGroupDetailTable' });

  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'EqpGroupDetailTable',
      keepSource: true,
      minHeight: '200px',
      tableClass: '!px-0 !py-0',
      columns: teamUserColumns,
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
                  tableRef.value?.insert({
                    isEnable: '1',
                  });
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
    const res = await getEqpGroupMember(record.id);
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
      const personGroupDetails = setDataCrud(data, isUpdate.value);
      return {
        personGroupDetails,
      };
    } catch (err) {
      console.error(err);
    }
  }

  defineExpose({
    setData,
    validate,
    getData,
  });
</script>
