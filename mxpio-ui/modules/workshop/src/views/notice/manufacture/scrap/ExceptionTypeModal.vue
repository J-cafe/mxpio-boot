<template>
  <BasicModal
    width="1200px"
    title="不良明细"
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
  import { positiveNumberPattern } from '@mxpio/bizcommon';

  defineOptions({ name: 'ExceptionTypeModal' });

  const emit = defineEmits(['success', 'register']);
  const isDisabled = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const formData: Recordable = {};

  const detailColumns: VxeGridPropTypes.Columns = [
    { type: 'checkbox', width: 40 },
    {
      title: '序号',
      type: 'seq',
      width: 50,
      align: 'center',
    },
    {
      title: '异常数量',
      field: 'exceptionQuantity',
      editRender: {
        name: 'AInputNumber',
      },
      width: 120,
    },
    {
      title: '异常类型',
      field: 'exceptionType',
      editRender: {
        name: 'DictSelect',
        props: { dictCode: 'ERP_QUAL_UNQUALIFIED_TYPE', mode: 'multiple', arrayToString: true },
      },
      width: 250,
    },
  ];

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'ExceptionTypeTable',
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
        exceptionType: [{ required: true, message: '请选择异常类型', trigger: 'change' }],
        exceptionQuantity: [
          { required: true, message: '请输入异常数量', trigger: 'change' },
          {
            type: 'number',
            pattern: positiveNumberPattern,
            message: '数量不能小于等于0',
            trigger: 'change',
          },
        ],
      },
      pagerConfig: {
        enabled: false,
      },
    };
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    isDisabled.value = !!data?.disabled;
    const record = data.record || {};
    formData.value = record;
    dataSource.value = record.exceptionList || [];
    tableRef.value?.loadData(dataSource.value);
  });

  function getData() {
    try {
      const { fullData = [] } = tableRef.value?.getTableData() || {};
      fullData.forEach((item: Recordable) => {
        delete item.id;
      });
      return fullData;
    } catch (err) {
      console.error(err);
    }
  }

  async function handleSubmit() {
    let errMap = await tableRef.value?.validate(true);
    if (!errMap) {
      const data = getData();
      emit('success', data);
      closeModal();
    }
  }
</script>
