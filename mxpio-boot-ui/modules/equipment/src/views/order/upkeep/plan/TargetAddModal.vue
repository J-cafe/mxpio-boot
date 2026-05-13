<template>
  <BasicModal
    width="1200px"
    title="新增点检设备"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions">
      <template #targetId="{ row }">
        <EqpBasicsSelect
          :value="row.targetId"
          @change="(value, items) => selectTarget(value, items, row)"
        />
      </template>
      <template #targetIdText="{ row }">
        {{ row.targetId }}
      </template>
    </VxeBasicTable>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable, BasicModal, useModalInner } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance, VxeGridPropTypes } from '@mxpio/components';
  import { setDataCrud } from '@mxpio/utils/src/common';
  import { addEqpUpkeepPlanTargetApi, EqpBasicsSelect } from '@mxpio/bizcommon';
  import XEUtils from 'xe-utils';

  defineOptions({ name: 'TargetAddModal' });
  const props = defineProps({
    planId: {
      type: String,
      default: () => '',
    },
  });
  const emit = defineEmits(['success', 'register']);

  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const formData = ref<Recordable>({});

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    formData.value = data.record;
  });

  async function handleSubmit() {
    try {
      setModalProps({ confirmLoading: true });
      let errMap = await tableRef.value?.validate(true);
      if (errMap) {
        return;
      }
      const saveData = classifyIntoFormData() || [];
      await addEqpUpkeepPlanTargetApi(saveData);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  const detailColumns: VxeGridPropTypes.Columns = [
    { type: 'checkbox', width: 40 },
    {
      title: '序号',
      type: 'seq',
      width: 50,
      align: 'center',
    },
    {
      title: '资产编码',
      field: 'targetId',
      editRender: {},
      slots: { default: 'targetIdText', edit: 'targetId' },
    },
    { title: '资产名称', field: 'targetName' },
    { title: '规格型号', field: 'targetSpec' },
    { title: '父级资产编码', field: 'parentTargetId' },
    { title: '父级资产名称', field: 'parentTargetName' },
    {
      title: '上次保养日期',
      field: 'lastUpkeepDate',
      editRender: {
        name: 'ADatePicker',
        props: {
          valueFormat: 'YYYY-MM-dd',
        },
      },
    },
  ];

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'TargetTable',
      keepSource: true,
      height: 300,
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
        itemCode: [{ required: true, message: '请选择物料', trigger: 'change' }],
        lastUpkeepDate: [{ required: true, message: '请选择上次保养日期', trigger: 'change' }],
      },
      pagerConfig: {
        enabled: false,
      },
    };
  });

  function classifyIntoFormData() {
    const data: Recordable | undefined = tableRef.value?.getRecordset();
    if (!data) {
      return false;
    }
    const bomData = setDataCrud(data, true);
    return bomData;
  }

  function selectTarget(value: any, items: any, row) {
    const insertData: Recordable[] = [];
    items.forEach((item: any, i: number) => {
      if (i === 0) {
        XEUtils.merge(row, {
          planId: props.planId,
          targetId: item.basicsCode,
          targetName: item.eqpName,
          targetCode: item.eqpCode,
          targetSpec: item.specType,
          checkPersonGroupCode: item.checkPersonGroupCode,
          lastCheckDate: item.lastCheckDate,
          processCode: item.processCode,
          parentTargetId: item.parentCode,
          parentTargetName: item.parentName,
          lastUpkeepDate: item.lastUpkeepDate,
        });
      } else {
        insertData.push({
          planId: props.planId,
          targetId: item.basicsCode,
          targetName: item.eqpName,
          targetCode: item.eqpCode,
          targetSpec: item.specType,
          checkPersonGroupCode: item.checkPersonGroupCode,
          lastCheckDate: item.lastCheckDate,
          processCode: item.processCode,
          parentTargetId: item.parentCode,
          parentTargetName: item.parentName,
          lastUpkeepDate: item.lastUpkeepDate,
        });
      }
    });
    tableRef.value?.insert(insertData);
  }
</script>
