<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
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
</template>
<script lang="ts" setup>
  import { ref, computed, nextTick } from 'vue';
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { procGroupLineList, procStandroutLineList } from '@mxpio/bizcommon';
  import ProcGroupModal from '@mxpio/bizcommon/src/components/Form/ProcGroupSelect/ProcGroupModal.vue';
  import ProcInfoModal from '@mxpio/bizcommon/src/components/Form/ProcInfoSelect/ProcInfoModal.vue';
  import { setDataCrud } from '@mxpio/utils/src/common';
  import { useMessage } from '@mxpio/hooks';
  import XEUtils from 'xe-utils';

  defineOptions({ name: 'ProcGroupDetailTable' });

  const isDisabled = ref(false);
  const isUpdate = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const { createMessage } = useMessage();
  const [registerGroupModal, { openModal: openGroupModal }] = useModal();
  const [registerInfoModal, { openModal: openInfoModal }] = useModal();

  const procInfoColumns: VxeGridPropTypes.Columns = [
    { type: 'checkbox', width: 40 },
    {
      title: '工艺序号',
      field: 'processOrder',
      width: 120,
      align: 'center',
      sortable: true,
      editRender: {
        name: 'AInputNumber',
        placeholder: '请点击输入',
      },
    },
    { title: '工序段编码', field: 'procGroupCode', width: 120 },
    { title: '工序段名称', field: 'procGroupName', width: 120 },
    { title: '工序编码', field: 'processCode', width: 100 },
    { title: '工序名称', field: 'processInfo.processName', width: 100 },
    { title: '工序描述', field: 'processInfo.craftDesc', width: 100 },
    { title: '工序分类', field: 'processInfo.textMap.processClass$DICT_TEXT_', width: 100 },
    { title: '工序类型', field: 'processInfo.textMap.processType$DICT_TEXT_', width: 100 },
    { title: '报工方式', field: 'processInfo.textMap.reportMode$DICT_TEXT_', width: 100 },
    { title: '是否分批报工', field: 'processInfo.textMap.batchReportAble$DICT_TEXT_', width: 100 },
    { title: '是否自制', field: 'processInfo.textMap.manufactureAble$DICT_TEXT_', width: 100 },
    { title: '是否委外', field: 'processInfo.textMap.outsourceAble$DICT_TEXT_', width: 100 },
    { title: '误差范围（%）', field: 'processInfo.toleranceRange', width: 120 },
    { title: '是否自动报工', field: 'processInfo.textMap.autoReportAble$DICT_TEXT_', width: 120 },
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
        // trigger: 'cell',
        remote: false,
        defaultSort: { field: 'processOrder', order: 'asc' },
      },
    };
  });

  async function setData(data: Recordable) {
    isDisabled.value = !!data?.disabled;
    isUpdate.value = data?.isUpdate || false;
    const record = data.record || {};
    const res = await procStandroutLineList(record.routCode);
    dataSource.value = res.sort((pre, next) => {
      return pre.processOrder - next.processOrder;
    });
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
    }
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
        tableRef.value?.sort({ field: 'processOrder', order: 'asc' });
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

        tableRef.value?.sort({ field: 'processOrder', order: 'asc' });
      } else {
        createMessage.warning('已到底部');
      }
    }
  }

  defineExpose({
    setData,
    validate,
    getData,
  });
</script>
