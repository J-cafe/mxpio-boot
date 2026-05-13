<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions" @row-dragend="rowDragend" />
</template>
<script lang="ts" setup>
  import { ref, computed, nextTick } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { procGroupLineList } from '@mxpio/bizcommon';
  import { setDataCrud } from '@mxpio/utils/src/common';
  import XEUtils from 'xe-utils';

  defineOptions({ name: 'ProcGroupDetailTable' });

  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);

  const procInfoColumns: VxeGridPropTypes.Columns = [
    { type: 'checkbox', width: 40 },
    {
      title: '序号',
      field: 'processOrder',
      width: '80',
      align: 'center',
      dragSort: true,
      sortable: true,
    },
    {
      title: '工序编码',
      field: 'processCode',
      editRender: {
        name: 'ProcInfoSelect',
        placeholder: '请点击输入',
        events: {
          change: (params: any, value, items) => {
            const { row, $grid } = params;
            const insertData: Recordable[] = [];
            const insertDataDef = getInsertData();
            items.forEach((item: any, i) => {
              if (i === 0) {
                XEUtils.set(row, 'processCode', item.processCode);
                XEUtils.set(row, 'processInfo', { ...item });
              } else {
                insertData.push(
                  XEUtils.merge(
                    {
                      processCode: item.processCode,
                      processInfo: { ...item },
                    },
                    {
                      processOrder: insertDataDef.processOrder++,
                    },
                  ),
                );
              }
            });
            $grid.insert(insertData);
          },
        },
      },
    },
    {
      title: '工序名称',
      field: 'processInfo.processName',
    },
    {
      title: '工序分类',
      field: 'processInfo.textMap.processClass$DICT_TEXT_',
    },
    {
      title: '工序类型',
      field: 'processInfo.textMap.processType$DICT_TEXT_',
    },
    {
      title: '报工方式',
      field: 'processInfo.textMap.reportMode$DICT_TEXT_',
    },
    {
      title: '是否分批报工',
      field: 'processInfo.textMap.batchReportAble$DICT_TEXT_',
    },
    {
      title: '是否委外',
      field: 'processInfo.textMap.outsourceAble$DICT_TEXT_',
    },
    {
      title: '工序描述',
      field: 'processInfo.craftDesc',
    },
  ];

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'DetailTable',
      keepSource: true,
      height: '400px',
      tableClass: '!px-0 !py-0',
      columns: procInfoColumns,
      data: dataSource.value,
      proxyConfig: { enabled: false },
      rowConfig: {
        drag: true,
      },
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
      sortConfig: {
        trigger: 'cell',
        remote: false,
        defaultSort: { field: 'processOrder', order: 'asc' },
      },
    };
  });

  async function setData(data: Recordable) {
    isDisabled.value = !!data?.disabled;
    isUpdate.value = data?.isUpdate || false;
    const record = data.record || {};
    const res = await procGroupLineList(record.procGroupCode);
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
      const lineList = setDataCrud(data, isUpdate.value);
      lineList.forEach((item: Recordable) => {
        item.saveTransient = false;
      });
      return {
        lineList,
      };
    } catch (err) {
      console.error(err);
    }
  }

  function getInsertData() {
    const maxNum = getMaxNum();
    return {
      processOrder: maxNum ? maxNum + 1 : 1,
    };
  }

  function getMaxNum() {
    let maxNum = 0;
    const { fullData = [] } = tableRef.value?.getTableData() || {};
    if (fullData) {
      const numList = fullData.map((item: Recordable) => item.processOrder);
      maxNum = numList.length > 0 ? Math.max(...numList) : 0;
    }
    return maxNum;
  }

  function rowDragend() {
    const { fullData = [] } = tableRef.value?.getTableData() || {};
    fullData.forEach((item, i) => {
      XEUtils.set(item, 'processOrder', i + 1);
    });
  }

  defineExpose({
    setData,
    validate,
    getData,
  });
</script>
