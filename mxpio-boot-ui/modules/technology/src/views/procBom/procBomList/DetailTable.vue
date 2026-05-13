<template>
  <VxeBasicTable class="pt-0 proc-detail-table" ref="tableRef" v-bind="gridOptions">
    <!-- <template #schemeCode="scope">
      <QualityTemplateSelect
        :modalWidth="1100"
        :value="scope.row.schemeCode"
        :multiple="false"
        :filters="{ 'busiType@EQ': '2', 'bpmnStatus@EQ': '03' }"
        @change="(ids, selectRow) => handleSchemeCodeChange(scope, selectRow)"
    /></template>
    <template #schemeCodeText="{ row }">
      <span>{{ row.schemeCode }}</span>
    </template> -->
  </VxeBasicTable>
  <ProcGroupModal
    @register="registerGroupModal"
    :filters="{ 'status@EQ': '1' }"
    :multiple="false"
    @success="handleSuccess"
  />
  <ProcInfoModal
    @register="registerInfoModal"
    :filters="{ 'status@EQ': '1' }"
    :multiple="true"
    @success="handleInfoSuccess"
  />
  <ProcStandardModal
    @register="registerStandardModal"
    :filters="{ 'status@EQ': '1' }"
    :multiple="false"
    @success="handleStandardSuccess"
  />
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { CuryTypeEnum } from '@mxpio/enums';
  import { procGroupLineList, procStandroutLineList } from '@mxpio/bizcommon';
  import ProcGroupModal from '@mxpio/bizcommon/src/components/Form/ProcGroupSelect/ProcGroupModal.vue';
  import ProcInfoModal from '@mxpio/bizcommon/src/components/Form/ProcInfoSelect/ProcInfoModal.vue';
  import ProcStandardModal from './ProcStandardModal.vue';
  import XEUtils from 'xe-utils';
  import { useMessage } from '@mxpio/hooks';

  defineOptions({ name: 'DetailTable' });

  const isUpdate = ref(true);
  const isDisabled = ref(false);
  const isTmp = ref(false);
  const isCopy = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const formData: Recordable = {};
  const { createMessage } = useMessage();
  const [registerGroupModal, { openModal: openGroupModal }] = useModal();
  const [registerInfoModal, { openModal: openInfoModal }] = useModal();
  const [registerStandardModal, { openModal: openStandardModal }] = useModal();

  const detailColumns: VxeGridPropTypes.Columns = [
    { type: 'checkbox', width: 40 },
    {
      title: '工艺序号',
      field: 'processOrder',
      editRender: {
        name: 'AInputNumber',
        placeholder: '请点击输入',
      },
      width: 150,
      sortable: true,
    },
    { title: '工序段编码', field: 'procGroupCode', width: 120 },
    { title: '工序段名称', field: 'procGroupName', width: 120 },
    { title: '工序编码', field: 'processCode', width: 100 },
    { title: '工序名称', field: 'processInfo.processName', width: 100 },
    { title: '工序类型', field: 'processInfo.textMap.processType$DICT_TEXT_', width: 100 },
    { title: '报工方式', field: 'processInfo.textMap.reportMode$DICT_TEXT_', width: 100 },
    { title: '是否分批报工', field: 'processInfo.textMap.batchReportAble$DICT_TEXT_', width: 120 },
    { title: '是否自制', field: 'processInfo.textMap.manufactureAble$DICT_TEXT_', width: 100 },
    { title: '是否委外', field: 'processInfo.textMap.outsourceAble$DICT_TEXT_', width: 100 },
    { title: '误差范围（%）', field: 'processInfo.toleranceRange', width: 120 },
    { title: '是否自动报工', field: 'processInfo.textMap.autoReportAble$DICT_TEXT_', width: 120 },
    {
      title: '标准准备工时（S）',
      field: 'preparationTime',
      editRender: { name: 'AInput', props: { type: 'number' } },
      width: 180,
    },
    {
      title: '单件标准工时（S）',
      field: 'singlePieceTime',
      editRender: { name: 'AInput', props: { type: 'number' } },
      width: 180,
    },
    {
      title: '质检方案编码',
      field: 'schemeCode',
      editRender: {
        name: 'QualityTemplateSelect',
        props: {
          multiple: false,
          filters: { 'busiType@EQ': '2', 'bpmnStatus@EQ': '03' },
        },
        events: {
          change: (params: any, value: any, items: any) => {
            const { row } = params;
            XEUtils.set(row, 'schemeCode', items?.code);
            XEUtils.set(row, 'schemeName', items?.name);
          },
        },
      },
      width: 150,
    },
    { title: '质检方案名称', field: 'schemeName', width: 150 },
  ];
  const pattern = /^(0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*)$/;
  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'DetailTable',
      keepSource: true,
      minHeight: '400px',
      tableClass: '!px-0 !py-0',
      editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true,
        autoClear: false,
        enabled: !isDisabled.value,
      },
      columns: detailColumns,
      toolbarConfig: {
        buttons: [
          {
            content: '载入标准工艺',
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
                  openStandardModal(true, {});
                },
              },
            },
          },
          {
            content: '新增工序段',
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
                  openGroupModal(true, {});
                },
              },
            },
          },
          {
            content: '新增工序',
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
                  openInfoModal(true, {});
                },
              },
            },
          },
          {
            content: '上移',
            buttonRender: {
              name: 'AButton',
              attrs: {
                class: 'ml-2',
              },
              props: {
                type: 'primary',
                preIcon: 'mdi:arrow-up',
                disabled: isDisabled.value,
              },
              events: {
                click: () => {
                  handelUp();
                },
              },
            },
          },
          {
            content: '下移',
            buttonRender: {
              name: 'AButton',
              attrs: {
                class: 'ml-2',
              },
              props: {
                type: 'primary',
                preIcon: 'mdi:arrow-down',
                disabled: isDisabled.value,
              },
              events: {
                click: () => {
                  handelDown();
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
                  const selectRecords = tableRef.value?.getCheckboxRecords() || [];
                  if (selectRecords.length > 0) {
                    const removeList: Recordable[] = [];
                    const pendingList: Recordable[] = [];
                    selectRecords.forEach((item: Recordable) => {
                      if (item.tmpCrudType === CuryTypeEnum.SAVE || !item.tmpCrudType) {
                        // tmpCrudType状态为save的直接在草稿表删除
                        removeList.push(item);
                      } else {
                        pendingList.push(item);
                      }
                    });
                    removeList.length > 0 && tableRef.value?.remove(removeList);
                    pendingList.length > 0 && tableRef.value?.setPendingRow(pendingList, true);
                  }
                },
              },
            },
          },
          {
            content: '取消删除',
            buttonRender: {
              name: 'AButton',
              props: {
                // type: 'primary',
                preIcon: 'mdi:undo-variant',
                disabled: isDisabled.value,
              },
              events: {
                click: async () => {
                  const selectRecords = tableRef.value?.getCheckboxRecords() || [];
                  if (selectRecords.length > 0) {
                    await tableRef.value?.setPendingRow(selectRecords, false);
                  }
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
      data: dataSource.value,
      editRules: {
        processOrder: [
          { required: true, message: '请输入工序', trigger: 'change' },
          { validator: validatorOrder, trigger: 'manual' },
        ],
        preparationTime: [
          {
            type: 'number',
            pattern: pattern,
            message: '不能小于0',
            trigger: 'change',
          },
        ],
        singlePieceTime: [
          {
            type: 'number',
            pattern: pattern,
            message: '不能小于0',
            trigger: 'change',
          },
        ],
      },
      proxyConfig: { enabled: false },
      'cell-class-name': ({ row }) => {
        if (row.tmpCrudType === CuryTypeEnum.SAVE || !row.tmpCrudType) {
          return 'row--add';
        }
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
    isUpdate.value = !!data?.isUpdate;
    isTmp.value = !!data?.isTmp;
    isCopy.value = !!data?.isCopy;
    const record = data.record || {};
    const dataSource = record.lineList || [];
    formData.value = record;
    const pendingList: Recordable[] = [];
    dataSource.forEach((item: Recordable) => {
      item.tmpCrudType = item.tmpCrudType || CuryTypeEnum.NONE; // 数据对于bom表为未修改
      if (!isTmp.value && !isDisabled.value) {
        // 当前为非草稿编辑状态时，bomLineId、bomId 需要先清空
        item.routId = null;
        item.id = null;
      }
      if (item.tmpCrudType === CuryTypeEnum.DELETE) {
        pendingList.push(item);
      }
    });
    if (isCopy.value) {
      // 复制时，需要直接插入数据
      tableRef.value?.insert(dataSource || []);
    } else {
      tableRef.value?.loadData(dataSource || []);
      pendingList.length > 0 && tableRef.value?.setPendingRow(pendingList, true);
    }
  }

  async function validate() {
    let errMap = await tableRef.value?.validate(true);
    if (errMap) {
      return Promise.reject(errMap);
    }
    return Promise.resolve();
  }

  function getRecordset() {
    const lineList = isTmp.value ? getTmpDataCrud() : getDataCrud();
    lineList.forEach((item: Recordable) => {
      item.saveTransient = false;
    });

    return lineList;
  }

  // 获取变更时数据
  function getDataCrud() {
    const {
      insertRecords = [],
      updateRecords = [],
      pendingRecords = [],
    } = tableRef.value?.getRecordset() || {};
    const { fullData } = tableRef.value?.getTableData() || {};
    const modifiedIds: string[] = [];
    insertRecords &&
      insertRecords.forEach((item) => {
        // 新插入
        item.crudType = CuryTypeEnum.SAVE; // 数据对于草稿表为新增
        item.tmpCrudType = CuryTypeEnum.SAVE; // 数据对于bom表为新增
        modifiedIds.push(item._X_ROW_KEY);
      });
    updateRecords &&
      updateRecords.forEach((item) => {
        // 编辑中
        item.crudType = CuryTypeEnum.SAVE; // 数据对于草稿表为新增
        item.tmpCrudType = CuryTypeEnum.UPDATE; // 数据对于bom表为修改
        modifiedIds.push(item._X_ROW_KEY);
      });
    pendingRecords &&
      pendingRecords.forEach((item) => {
        // 已删除
        item.crudType = CuryTypeEnum.SAVE; // 数据对于草稿表为新增
        item.tmpCrudType = CuryTypeEnum.DELETE; // 数据对于bom表为删除
        modifiedIds.push(item._X_ROW_KEY);
      });
    const noneRecords: Recordable[] = [];
    fullData?.forEach((item) => {
      // 处理第一次变更时，未修改数据
      !modifiedIds.includes(item._X_ROW_KEY) &&
        noneRecords.push({
          ...item,
          crudType: CuryTypeEnum.SAVE, // 数据对于草稿表为新增
          tmpCrudType: CuryTypeEnum.NONE, // 数据对于bom表为未修改
        });
    });
    return [
      ...(insertRecords as Recordable[]),
      ...(updateRecords as Recordable[]),
      ...(pendingRecords as Recordable[]),
      ...noneRecords,
    ];
  }

  function getTmpDataCrud() {
    const {
      insertRecords = [],
      updateRecords = [],
      pendingRecords = [],
      removeRecords = [],
    } = tableRef.value?.getRecordset() || {};
    const { fullData } = tableRef.value?.getTableData() || {};
    const modifiedIds: string[] = [];
    insertRecords &&
      insertRecords.forEach((item) => {
        // 新插入
        item.crudType = CuryTypeEnum.SAVE; // 数据对于草稿表为新增
        item.tmpCrudType = CuryTypeEnum.SAVE; // 数据对于bom表为新增
        modifiedIds.push(item.id);
      });
    updateRecords &&
      updateRecords.forEach((item) => {
        // 编辑中
        item.crudType = CuryTypeEnum.UPDATE; // 数据对于草稿表为修改
        item.tmpCrudType =
          item.tmpCrudType === CuryTypeEnum.DELETE || item.tmpCrudType === CuryTypeEnum.NONE
            ? CuryTypeEnum.UPDATE
            : item.tmpCrudType; // 处理撤下删除后又有修改
        modifiedIds.push(item.id);
      });
    pendingRecords &&
      pendingRecords.forEach((item) => {
        // 已删除
        item.crudType = CuryTypeEnum.UPDATE; // 数据对于草稿表为修改
        item.tmpCrudType = CuryTypeEnum.DELETE; // 数据对于bom表为删除
        modifiedIds.push(item.id);
      });
    removeRecords &&
      removeRecords.forEach((item) => {
        // 已删除
        item.crudType = CuryTypeEnum.DELETE; // 数据对于草稿表为删除
        item.tmpCrudType = CuryTypeEnum.DELETE; // 数据对于bom表为删除
        modifiedIds.push(item.id);
      });
    const cancelPendingRecords: Recordable = [];
    fullData?.forEach((item) => {
      // 处理撤下删除，table脏数据未变化
      if (!modifiedIds.includes(item.id) && item.tmpCrudType === CuryTypeEnum.DELETE) {
        cancelPendingRecords.push({
          ...item,
          crudType: CuryTypeEnum.UPDATE, // 数据对于草稿表为新增
          tmpCrudType: CuryTypeEnum.UPDATE, // 数据对于bom表为未修改
        });
      }
    });
    return [
      ...(insertRecords as Recordable[]),
      ...(updateRecords as Recordable[]),
      ...(pendingRecords as Recordable[]),
      ...removeRecords,
      ...(cancelPendingRecords as Recordable[]),
    ];
  }

  function validatorOrder({ cellValue, row }) {
    if (row.tmpCrudType === CuryTypeEnum.DELETE) {
      return undefined;
    }
    const { fullData } = tableRef.value?.getTableData() || {};
    const list =
      fullData?.filter((item) => {
        return (
          item.tmpCrudType !== CuryTypeEnum.DELETE &&
          item._X_ROW_KEY !== row._X_ROW_KEY &&
          Number(item.processOrder) === Number(cellValue)
        );
      }) || [];
    if (list.length > 0) {
      return new Error('工艺序号不能重复');
    }
    // 验证通过时显式返回 undefined
    return undefined;
  }

  function getInsertData() {
    const maxNum = getMaxNum();
    return {
      processOrder: maxNum ? getProcessOrder(maxNum) : 5,
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

  function getProcessOrder(processOrder) {
    // 获取需要插入的序号
    const times = processOrder / 5;
    const order = Math.ceil(times >= 1 ? times + 1 : times) * 5;
    return Math.floor(order);
  }

  async function handleSuccess(groupCode, row) {
    const res = await procGroupLineList(groupCode);
    const insetData = getInsertData();
    const insertList: Recordable[] = [];
    res.forEach((item, i) => {
      if (i > 0) {
        insetData.processOrder = insetData.processOrder + 5;
      }
      insertList.push(
        Object.assign(
          {},
          {
            processCode: item.processCode,
            procGroupCode: item.procGroupCode,
            procGroupName: row.procGroupName,
            processInfo: { ...item.processInfo },
          },
          insetData,
        ),
      );
    });
    tableRef.value?.insert(insertList);
    sortTable();
  }

  async function handleInfoSuccess(codes, selectRows) {
    if (selectRows && selectRows.length > 0) {
      const insetData = getInsertData();
      const insertList: Recordable[] = [];
      selectRows.forEach((item, i) => {
        if (i > 0) {
          insetData.processOrder = insetData.processOrder + 5;
        }
        insertList.push(
          Object.assign(
            {},
            {
              processCode: item.processCode,
              processInfo: { ...item },
            },
            insetData,
          ),
        );
      });
      tableRef.value?.insert(insertList);
      sortTable();
    }
  }

  async function handleStandardSuccess(codes, row) {
    const res = await procStandroutLineList(codes);
    const insetData = getInsertData();
    const insertList: Recordable[] = [];
    res.forEach((item, i) => {
      if (i > 0) {
        insetData.processOrder = insetData.processOrder + 5;
      }
      insertList.push(
        Object.assign(
          {},
          {
            processCode: item.processCode,
            procGroupCode: item.procGroupCode,
            procGroupName: row.procGroupName,
            processInfo: { ...item.processInfo },
          },
          insetData,
        ),
      );
    });
    tableRef.value?.insert(insertList);
    sortTable();
  }

  async function handelUp() {
    const current = tableRef.value?.getCurrentRecord();
    if (current) {
      const { tableData = [] } = tableRef.value?.getTableData() || {};
      const currentIndex = tableData.findIndex(
        (item: Recordable) => item.processOrder === current.processOrder,
      );
      if (currentIndex > 0) {
        // 交换 processOrder,先临时存储当前 processOrder
        const processOrder = tableData[currentIndex].processOrder;
        XEUtils.set(
          tableData[currentIndex],
          'processOrder',
          tableData[currentIndex - 1].processOrder,
        );
        XEUtils.set(tableData[currentIndex - 1], 'processOrder', processOrder);
        sortTable();
      } else {
        createMessage.warning('已到顶部');
      }
    }
  }

  async function handelDown() {
    const current = tableRef.value?.getCurrentRecord();
    if (current) {
      const { tableData = [] } = tableRef.value?.getTableData() || {};
      const currentIndex = tableData.findIndex(
        (item: Recordable) => item.processOrder === current.processOrder,
      );
      if (currentIndex < tableData.length - 1) {
        // 交换 processOrder,先临时存储当前 processOrder
        const processOrder = tableData[currentIndex].processOrder;
        XEUtils.set(
          tableData[currentIndex],
          'processOrder',
          tableData[currentIndex + 1].processOrder,
        );
        XEUtils.set(tableData[currentIndex + 1], 'processOrder', processOrder);

        sortTable();
      } else {
        createMessage.warning('已到底部');
      }
    }
  }

  function sortTable() {
    tableRef.value?.sort({ field: 'processOrder', order: 'asc' });
  }

  defineExpose({
    setData,
    getRecordset,
    validate,
  });
</script>
<style lang="less">
  .vxe-body--row {
    .row--add {
      background-color: #f1e3c9;
    }
  }

  .vxe-table--render-default .vxe-body--row.row--stripe > .row--add {
    background-color: #f1e3c9;
  }

  .proc-detail-table .vxe-table--render-default .vxe-body--row.row--current > .vxe-body--column {
    background-color: var(--vxe-ui-table-row-current-background-color);
  }
</style>
