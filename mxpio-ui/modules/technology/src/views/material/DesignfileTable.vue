<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, unref, computed } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance } from '@mxpio/components';
  import { addDesignfile, editDesignfile, getDesignfile } from '@mxpio/bizcommon';
  import { drawingColumns } from './material.data';
  import { setDataCrud } from '@mxpio/utils/src/common';

  defineOptions({ name: 'DesignfileTable' });

  const isUpdate = ref(true);
  const isDisabled = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const designfileList = ref([]);

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'DesignfileTable',
      keepSource: true,
      minHeight: '200px',
      editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true,
        autoClear: false,
        enabled: !isDisabled.value,
      },
      columns: drawingColumns,
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
                  tableRef.value?.insert({});
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
      data: designfileList.value,
      editRules: {
        fileNo: [{ required: true, message: '请输入选择文件', trigger: 'change' }],
      },
      proxyConfig: { enabled: false },
      pagerConfig: {
        enabled: false,
      },
    };
  });

  async function updateForm(data: Recordable) {
    isDisabled.value = !!data?.disabled;
    const record = data.record || {};
    let res: never[] = [];
    if (record.itemCode) {
      res = await getDesignfile(record.itemCode);
    }
    designfileList.value = res;
    tableRef.value?.loadData(res);
    isUpdate.value = res.length > 0;
  }

  async function submitForm(itemCode: string) {
    try {
      let errMap = await tableRef.value?.validate(true);
      if (errMap) {
        return false;
      }
      const data: Recordable | undefined = tableRef.value?.getRecordset();
      if (!data) {
        return false;
      }
      // 获取待保存的脏数据
      const fileList: Recordable[] = setDataCrud(data, isUpdate.value);
      fileList.forEach((item) => {
        item.itemCode = itemCode;
      });
      if (!fileList.length) {
        return Promise.resolve(true);
      }
      if (!unref(isUpdate)) {
        return await addDesignfile(fileList);
      } else {
        return await editDesignfile(fileList);
      }
    } catch (error) {
      return Promise.reject(error);
    }
  }

  async function validate() {
    let errMap = await tableRef.value?.validate(true);
    if (errMap) {
      return Promise.reject(errMap);
    }
    return Promise.resolve();
  }

  defineExpose({
    updateForm,
    submitForm,
    validate,
  });
</script>
