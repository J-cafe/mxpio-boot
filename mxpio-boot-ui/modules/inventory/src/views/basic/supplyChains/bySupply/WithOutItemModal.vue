<template>
  <BasicModal
    width="1200px"
    title="未关联物料"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, reactive } from 'vue';
  import { BasicModal, useModalInner, VxeBasicTable } from '@mxpio/components';
  import { supplyOutColumns } from './bySupply.data';
  import type { VxeGridPropTypes, VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';
  import { supplychainItemWithout, supplychainSave } from '@mxpio/bizcommon';
  import { useMessage } from '@mxpio/hooks';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import { OperatorEnum } from '@mxpio/enums';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';

  defineOptions({ name: 'WithOutItemModal' });

  const emit = defineEmits(['success', 'register']);

  let pnCode: string = '';
  const tableRef = ref<VxeGridInstance>();
  const { createMessage } = useMessage();
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'WithOutItemTable',
    keepSource: true,
    minHeight: '200px',
    tableClass: '!px-0 !py-0',
    columns: supplyOutColumns,
    proxyConfig: {
      ajax: {
        query: async ({ page, form, sorts }: VxeGridPropTypes.ProxyAjaxQueryParams<any>) => {
          return loadData({ page, form, sorts });
        },
      },
      autoLoad: false,
    },
    editConfig: {
      trigger: 'click',
      mode: 'row',
      showStatus: true,
      autoClear: false,
    },
    editRules: {
      itemPrice: [
        { required: true, message: '请输入单价', trigger: 'change' },
        {
          type: 'number',
          min: 0,
          message: '单价不能小于等于0',
        },
      ],
    },
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    pnCode = data.pnCode;
    tableRef.value?.commitProxy('query');
  });

  async function loadData({ page, form, sorts }: VxeTableQueryParams) {
    const params = getVxeTableQueryParams({
      page,
      form,
      sorts,
      callback: (params) => {
        params
          .or()
          .addCriterion('outsourceAble', OperatorEnum.EQ, '1')
          .addCriterion('itemSource', OperatorEnum.EQ, '2')
          .end();
        return params;
      },
    });
    const res = await supplychainItemWithout(pnCode, params);
    return res;
  }

  async function handleSubmit() {
    try {
      setModalProps({ confirmLoading: true });
      const selectionRows = tableRef.value?.getCheckboxRecords() || [];
      if (selectionRows.length <= 0) {
        createMessage.error('请选择要关联的物料');
        return;
      }
      let errMap = await tableRef.value?.validate(selectionRows);
      if (errMap) {
        return;
      }
      const executeData = classifyIntoFormData(selectionRows);
      await supplychainSave(executeData);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  function classifyIntoFormData(selectionRows: Recordable[]) {
    const data: Recordable[] = [];
    selectionRows.forEach((item) => {
      data.push({
        pnCode: pnCode,
        itemCode: item.itemCode,
        defaultSupply: '0',
        inQualityControl: '1',
        hold: '0',
        itemPrice: item.itemPrice,
      });
    });
    return data;
  }
</script>
