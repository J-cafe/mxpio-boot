<template>
  <BasicModal
    width="1200px"
    title="生产不良退料确认"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <VxeBasicTable ref="tableRef" v-bind="gridOptions">
      <template #exceptionList="{ row }">
        <a-button @click="handleException(row)">添加明细</a-button>
      </template>
    </VxeBasicTable>
    <ExceptionTypeModal @register="registerExceptionTypeModal" @success="selectExceptionType" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { BasicModal, useModalInner, VxeBasicTable, useModal } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance } from '@mxpio/components';
  import { executeColumns } from './scrap.data';
  import {
    manufactureScrapExecuteApi,
    dpLineListApi,
    useExecuteClassifyData,
  } from '@mxpio/bizcommon';
  import ExceptionTypeModal from './ExceptionTypeModal.vue';

  defineOptions({ name: 'ManufactureScrapExecuteModal' });

  const emit = defineEmits(['success', 'register']);
  let notice: Recordable = {};
  const dataSource = ref([]);
  const currentData = ref<Recordable>({});
  const tableRef = ref<VxeGridInstance>();
  const [registerExceptionTypeModal, { openModal }] = useModal();
  const { classifyNoticeFormData } = useExecuteClassifyData();
  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'ManufactureScrapExecuteTable',
      keepSource: true,
      minHeight: '200px',
      tableClass: '!px-0 !py-0',
      columns: executeColumns,
      data: dataSource.value,
      proxyConfig: { enabled: false },
      pagerConfig: {
        enabled: false,
      },
      editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true,
        autoClear: false,
      },
      editRules: {
        executeQuantity: [
          { required: true, message: '请输入确认数量', trigger: 'change' },
          {
            validator: ({ cellValue, row }) => {
              if (cellValue <= 0) {
                return new Error('发货数量不能小于等于0');
              }
              if (cellValue > row.quantity) {
                return new Error('不能大于数量' + row.quantity);
              }
            },
            trigger: 'change',
          },
        ],
        defectiveReason: [{ required: true, message: '请输入不良原因', trigger: 'change' }],
        responseUnit: [{ required: true, message: '请输入责任单位', trigger: 'change' }],
      },
    };
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    notice = data.record;
    const res = await dpLineListApi(notice.noticeNo);
    dataSource.value = res.map((item) => {
      return {
        ...item,
        executeQuantity: item.quantity,
      };
    });
    tableRef.value?.loadData(dataSource.value);
  });

  async function handleSubmit() {
    try {
      setModalProps({ confirmLoading: true });
      let errMap = await tableRef.value?.validate(true);
      if (errMap) {
        return;
      }
      const executeData = classifyIntoFormData();
      await manufactureScrapExecuteApi(notice.noticeNo, executeData);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  function classifyIntoFormData() {
    const { fullData = [] } = tableRef.value?.getTableData() || {};
    const executeData = classifyNoticeFormData(fullData, notice, (item: Recordable) => {
      return {
        supplementFlag: item.supplementFlag,
        reporter: item.reporter,
        reportProc: item.reportProc,
        exceptionList: item.exceptionList,
        defectiveReason: item.defectiveReason,
        defectiveDesc: item.defectiveDesc,
        responseUnit: item.responseUnit,
        bomWhCode: item.bomWhCode,
      };
    });
    return {
      lines: executeData,
    };
  }

  function handleException(row) {
    currentData.value = row;
    openModal(true, { record: row });
  }

  function selectExceptionType(data) {
    console.log(data);
    currentData.value.exceptionList = data;
  }
</script>
