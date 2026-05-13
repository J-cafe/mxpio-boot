<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" @row-dragend="rowDragend">
    <template #codeText="{ row }">
      {{ row.code }}
    </template>
    <template #code="{ row }">
      <a-input-search readonly :value="row.code" @search="handleItem(row)" />
    </template>
    <template #targetValue="scope">
      <a-input
        :value="scope.row.targetValue"
        v-if="scope.row.inspectionItem && scope.row.inspectionItem.itemType === '1'"
        :type="
          scope.row.inspectionItem && scope.row.inspectionItem.itemType === '1' ? 'number' : 'text'
        "
        @change="(e) => changeTargetValue(e.target.value, scope)"
      />
      <a-select
        v-else
        class="!w-full"
        :mode="scope.row.comparator === 'include' ? 'multiple' : 'default'"
        :value="scope.row.targetValue"
        :options="targetValueOptions"
        @dropdown-visible-change="(open) => dropdownVisibleChange(scope.row, open)"
        @change="(value) => changeTargetValue(value, scope)"
      />
    </template>
    <template #targetValueText="{ row }">
      <span>{{ getTargetValueText(row.targetValue) }}</span>
    </template>
    <template #lineOrder="{ row }">
      <span>{{ row.lineOrder }}</span>
    </template>
    <template #comparatorText="{ row }">
      <span>{{ row.comparator === 'include' ? '[]' : row.comparator }}</span>
    </template>
  </VxeBasicTable>
  <ItemSelectModal @register="registerModal" @success="selectItem" />
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { setDataCrud } from '@mxpio/utils/src/common';
  import { paramlineListApi, qtLineListApi } from '@mxpio/bizcommon';
  import ItemSelectModal from './ItemSelectModal.vue';
  import XEUtils from 'xe-utils';

  defineOptions({ name: 'TemplateDetailTable' });

  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const targetValueOptions = ref([]);
  const formData: Recordable = {};
  let currentRow: Recordable = {};
  const [registerModal, { openModal }] = useModal();

  const detailColumns: VxeGridPropTypes.Columns = [
    { type: 'checkbox', width: 40 },
    { type: 'seq', width: 60, title: '序号', dragSort: true },
    {
      title: '检验顺序',
      field: 'lineOrder',
      editRender: {},
      slots: { edit: 'lineOrder', default: 'lineOrder' },
      width: 120,
      sortable: true,
    },
    {
      title: '检测项目',
      field: 'code',
      editRender: {},
      slots: { edit: 'code', default: 'codeText' },
      width: 120,
    },
    { title: '检测项目名称', field: 'inspectionItem.name', width: 100 },
    {
      title: '项目类型',
      field: 'inspectionItem.textMap.itemType$DICT_TEXT_',
      width: 100,
      formatter: 'dictText',
    },
    {
      title: '项目分类',
      field: 'inspectionItem.textMap.itemClass$DICT_TEXT_',
      width: 100,
      formatter: 'dictText',
    },
    {
      title: '缺陷等级',
      field: 'inspectionItem.textMap.defectGrade$DICT_TEXT_',
      width: 100,
      formatter: 'dictText',
    },
    { title: '检验标准', field: 'inspectionItem.standard', width: 100 },
    { title: '检测工具', field: 'inspectionItem.detectionTool', width: 100 },
    { title: '检验方法', field: 'inspectionItem.detectionMethod', width: 100 },
    {
      title: '比较符',
      field: 'comparator',
      width: 120,
      editRender: {
        name: 'ASelect',
        props: (params) => {
          const { row } = params;
          const itemType = row.inspectionItem?.itemType;
          const options =
            itemType === '1'
              ? [
                  { label: '≠', value: '≠' },
                  { label: '>', value: '>' },
                  { label: '>=', value: '>=' },
                  { label: '<=', value: '<=' },
                  { label: '[]', value: '[]' },
                  { label: '=', value: '=' },
                  { label: '<', value: '<' },
                ]
              : [
                  { label: '≠', value: '≠' },
                  { label: '=', value: '=' },
                  { label: '[]', value: 'include' },
                ];
          return {
            options: options,
          };
        },
      },
      slots: { default: 'comparatorText' },
    },
    {
      title: '目标值',
      field: 'targetValue',
      editRender: {},
      slots: { edit: 'targetValue', default: 'targetValueText' },
      width: 150,
    },
    {
      title: '单位',
      field: 'units',
      editRender: { name: 'DictSelect', props: { dictCode: 'ERP_QUAL_UNITS' } },
      width: 120,
    },
    {
      title: '最大值',
      field: 'maxValue',
      editRender: { name: 'AInput', props: { type: 'number' } },
      width: 120,
    },
    {
      title: '最小值',
      field: 'minValue',
      editRender: { name: 'AInput', props: { type: 'number' } },
      width: 120,
    },
    { title: '备注', field: 'memo', editRender: { name: 'AInput' }, width: 120 },
  ];

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'ItemDetailTable',
      keepSource: true,
      minHeight: 350,
      tableClass: '!px-0 !py-0',
      columns: detailColumns,
      data: dataSource.value,
      rowConfig: {
        drag: true,
      },
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
        code: [{ required: true, message: '请选择检测项目', trigger: 'change' }],
        targetValue: [
          {
            validator: ({ row, cellValue }) => {
              if (
                row.comparator !== '[]' &&
                (cellValue === '' || cellValue === null || cellValue === undefined)
              ) {
                return new Error(`请输入目标值`);
              }
            },
          },
        ],
        maxValue: [
          {
            validator: ({ row, cellValue }) => {
              if (
                row.comparator === '[]' &&
                (cellValue === '' || cellValue === null || cellValue === undefined)
              ) {
                return new Error(`请输入最大值`);
              }
            },
          },
        ],
        minValue: [
          {
            validator: ({ row, cellValue }) => {
              if (
                row.comparator === '[]' &&
                (cellValue === '' || cellValue === null || cellValue === undefined)
              ) {
                return new Error(`请输入最小值`);
              }
            },
          },
          {
            validator: ({ row, cellValue }) => {
              if (cellValue === '' || cellValue === undefined || cellValue === null) {
                return;
              }
              if (Number(cellValue) >= Number(row.maxValue)) {
                return new Error(`请输入小于${row.maxValue}的数字`);
              }
            },
          },
        ],
        comparator: [{ required: true, message: '请选择比较符', trigger: 'change' }],
      },
      pagerConfig: {
        enabled: false,
      },
      sortConfig: {
        trigger: 'cell',
        remote: false,
        defaultSort: { field: 'lineOrder', order: 'asc' },
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
    const res = await qtLineListApi(record.code);
    res.forEach((item) => {
      item.targetValue =
        item.comparator === 'include' ? item.targetValue.split(',') : item.targetValue;
    });
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
      const lineList = setDataCrud(data, isUpdate.value);
      lineList.forEach((item: Recordable) => {
        item.targetValue =
          item.targetValue instanceof Array ? item.targetValue.join(',') : item.targetValue;
        delete item.inspectionItem;
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
      lineOrder: maxNum ? maxNum + 1 : 1,
    };
  }

  function getTableData() {
    return tableRef.value?.getTableData();
  }

  function getMaxNum() {
    let maxNum = 0;
    const { fullData = [] } = tableRef.value?.getTableData() || {};
    if (fullData) {
      const numList = fullData.map((item: Recordable) => item.lineOrder);
      maxNum = numList.length > 0 ? Math.max(...numList) : 0;
    }
    return maxNum;
  }

  function handleItem(row) {
    currentRow = row;
    openModal(true, {});
  }

  function selectItem(codes: string, rows: Recordable[]) {
    const insertData: Recordable[] = [];
    const insertDataDef = getInsertData();

    if (rows.length === 0) {
      XEUtils.assign(currentRow, {
        code: '',
        name: '',
        inspectionItem: {},
      });
    } else {
      rows.forEach((item: any, i: number) => {
        if (i === 0) {
          XEUtils.merge(currentRow, {
            code: item.code,
            name: item.name,
            inspectionItem: {
              ...item,
            },
          });
        } else {
          insertData.push(
            XEUtils.merge(
              {
                code: item.code,
                name: item.name,
                inspectionItem: {
                  ...item,
                },
              },
              {
                lineOrder: insertDataDef.lineOrder++,
              },
            ),
          );
        }
      });
      tableRef.value?.insert(insertData);
    }
  }

  function dropdownVisibleChange(row, open) {
    if (open) {
      paramlineListApi(row.code).then((res) => {
        targetValueOptions.value = res.map((item) => {
          return {
            label: item.parameter,
            value: item.parameter,
          };
        });
      });
    }
  }

  function changeTargetValue(value, scope) {
    XEUtils.set(scope.row, 'targetValue', value);
    tableRef.value?.updateStatus(scope);
  }

  function getTargetValueText(targetValue) {
    if (targetValue instanceof Array) {
      return targetValue.join(',');
    } else {
      return targetValue;
    }
  }

  function rowDragend() {
    const { fullData = [] } = tableRef.value?.getTableData() || {};
    fullData.forEach((item, i) => {
      XEUtils.set(item, 'lineOrder', i + 1);
    });
  }

  defineExpose({
    setData,
    validate,
    getData,
    getTableData,
  });
</script>
