<template>
  <BasicModal
    width="1200px"
    title="请填写协助人员信息"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable, BasicModal, useModalInner } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { positiveNumberPattern, assistSaveEqpRepairApi } from '@mxpio/bizcommon';
  import { setDataCrud, dateUtil } from '@mxpio/utils';
  import XEUtils from 'xe-utils';

  defineOptions({ name: 'EqpUpkeepAssistModal' });
  const emit = defineEmits(['success', 'register']);

  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const bizNo = ref('');
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    bizNo.value = data.record.bizNo;
  });

  async function handleSubmit() {
    try {
      setModalProps({ confirmLoading: true });
      let errMap = await tableRef.value?.validate(true);
      if (errMap) {
        return;
      }
      const submitData: Recordable[] = classifyIntoFormData() || [];
      await assistSaveEqpRepairApi(submitData);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  function classifyIntoFormData() {
    const data: Recordable | undefined = tableRef.value?.getRecordset();
    if (!data) {
      return false;
    }
    const classifyData = setDataCrud(data, true);
    classifyData.forEach((item: Recordable) => {
      item.bizNo = bizNo.value;
    });
    return classifyData;
  }

  const detailColumns: VxeGridPropTypes.Columns = [
    { type: 'checkbox', width: 40 },
    { type: 'seq', width: 60 },
    {
      title: '工号',
      field: 'personNo',
      editRender: {
        name: 'UserByDeptSelect',
        placeholder: '请点击输入',
        events: {
          change: (params: any, value, items) => {
            const { row, $grid } = params;
            const insertData: Recordable[] = [];
            items.forEach((item: any, i) => {
              if (i === 0) {
                XEUtils.set(row, 'personNo', item.username);
                XEUtils.set(row, 'personName', item.nickname);
              } else {
                insertData.push({
                  personNo: item.username,
                  personName: item.nickname,
                });
              }
            });
            $grid.insert(insertData);
          },
        },
      },
      width: 140,
    },
    {
      title: '姓名',
      field: 'personName',
      width: 100,
      align: 'center',
    },
    {
      title: '开始时间',
      field: 'startTime',
      editRender: {
        name: 'ADatePicker',
        props: {
          showTime: true,
          valueFormat: 'YYYY-MM-DD HH:mm:ss',
        },
        events: {
          change: (params: any, value) => {
            const { row } = params;
            const manHour = row.manHour;
            if (value && manHour) {
              XEUtils.set(row, 'endTime', dateUtil(value).add(manHour, 'h'));
            }
          },
        },
      },
      width: 200,
    },
    {
      title: '工时',
      field: 'manHour',
      editRender: {
        name: 'AInputNumber',
        events: {
          change: (params: any, value) => {
            const { row } = params;
            const startTime = row.startTime;
            if (value && startTime) {
              XEUtils.set(
                row,
                'endTime',
                dateUtil(startTime).add(value, 'h').format('YYYY-MM-DD HH:mm:ss'),
              );
            }
          },
        },
      },
      width: 140,
    },
    {
      title: '结束时间',
      field: 'endTime',
      editRender: {
        name: 'ADatePicker',
        props: {
          showTime: true,
          valueFormat: 'YYYY-MM-DD HH:mm:ss',
        },
        events: {
          change: (params: any, value) => {
            const { row } = params;
            const startTime = row.startTime;
            if (value && startTime) {
              XEUtils.set(row, 'manHour', dateUtil(value).diff(startTime, 'h'));
            }
          },
        },
      },
      width: 200,
    },
    {
      title: '协助内容',
      field: 'content',
      editRender: {
        name: 'AInput',
      },
      width: 140,
    },
  ];

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'EqpUpkeepAssistTable',
      keepSource: true,
      height: 400,
      tableClass: '!px-0 !py-0',
      columns: detailColumns,
      data: dataSource.value,
      editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true,
        autoClear: false,
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
              },
              attrs: {
                class: 'ml-2',
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
        manHour: [
          { required: true, message: '请输入工时', trigger: 'change' },
          {
            type: 'number',
            pattern: positiveNumberPattern,
            message: '数量不能小于等于0',
            trigger: 'change',
          },
        ],
        startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
        endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
        personNo: [{ required: true, message: '请选择工号', trigger: 'change' }],
        content: [{ required: true, message: '请输入协助内容', trigger: 'change' }],
      },
      pagerConfig: {
        enabled: false,
      },
    };
  });
</script>
