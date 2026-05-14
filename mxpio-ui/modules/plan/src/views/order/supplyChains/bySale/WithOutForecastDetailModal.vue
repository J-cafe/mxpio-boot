<template>
  <BasicModal
    width="1200px"
    title="未冲销明细"
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
  import { forecastDetailOutColumns } from './bySale.data';
  import type { VxeGridPropTypes, VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';
  import { spofPickageApi, spofSaveApi } from '@mxpio/bizcommon';
  import { useMessage } from '@mxpio/hooks';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';

  defineOptions({ name: 'WithOutForecastDetailModal' });

  const emit = defineEmits(['success', 'register']);

  const filters = ref<Recordable>({});
  const tableRef = ref<VxeGridInstance>();
  const saleRow = ref<Recordable>({});
  const { createMessage } = useMessage();
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'WithOutForecastDetail',
    keepSource: true,
    minHeight: 300,
    tableClass: '!px-0 !py-0',
    columns: forecastDetailOutColumns,
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
      executeQuantity: [
        { required: true, message: '请输入冲销数量', trigger: 'change' },
        {
          validator: ({ cellValue, row }) => {
            if (cellValue <= 0) {
              return new Error('冲销数量不能小于等于0');
            }
            if (cellValue > row.restQuantity) {
              return new Error('不能大于未冲销数量');
            }
            if (cellValue > saleRow.value.restQuantity) {
              return new Error('不能大于未冲销销售单数量');
            }
          },
          trigger: 'change',
        },
      ],
    },
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    filters.value = {
      'itemCode@EQ': data.itemCode,
      'createTime@LE': data.createTime,
    };
    saleRow.value = data;
    tableRef.value?.commitProxy('query');
  });

  async function loadData({ page, form, sorts }: VxeTableQueryParams) {
    const params = getVxeTableQueryParams({ page, form, sorts, filters: filters.value });
    const res = await spofPickageApi(params);
    return res;
  }

  async function handleSubmit() {
    try {
      setModalProps({ confirmLoading: true });
      const selectionRows = tableRef.value?.getCheckboxRecords() || [];
      if (selectionRows.length <= 0) {
        createMessage.error('请选择要冲销的明细');
        return;
      }
      let errMap = await tableRef.value?.validate(selectionRows);
      if (errMap) {
        return;
      }
      const executeData = classifyIntoFormData(selectionRows);
      await spofSaveApi(executeData);
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
        quantity: item.executeQuantity,
        soBizNo: saleRow.value.bizNo,
        soBizLineNo: saleRow.value.lineNo,
        spBizNo: item.bizNo,
        spBizLineNo: item.lineNo,
        itemCode: item.itemCode,
      });
    });
    return data;
  }
</script>
