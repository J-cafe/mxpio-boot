<!-- 审核发起单据示例 -->
<template>
  <BasicForm :disabled="isDisabled" @register="registerForm" />
  <a-tabs>
    <a-tab-pane key="detailTable" tab="销售明细" forceRender>
      <DetailTable ref="tableRef" />
    </a-tab-pane>
  </a-tabs>
</template>
<script lang="ts" setup>
  import { BasicForm } from '@mxpio/components';
  import { saveSalesOrder } from '@mxpio/bizcommon';
  import { useFormCrudHook } from '@mxpio/common';
  import { formSchema } from './salesOrder.data';
  import { dateUtil } from '@mxpio/utils';
  import DetailTable from './DetailTable.vue';
  import { ref } from 'vue';

  defineOptions({ name: 'SalesOrderForm' });

  const emit = defineEmits(['success', 'register']);
  const tableRef = ref<InstanceType<typeof DetailTable>>();

  const { registerForm, isDisabled, setFormData, validate, getFormData, submitForm } =
    useFormCrudHook({
      formSchema,
      saveApi: saveSalesOrder,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      defaultValues: {
        orderDate: dateUtil().format('YYYY-MM-DD'),
        orderType: '01',
        bpmnStatus: '01',
        orderStatus: '10',
        closeStatus: 'open',
      },
      subTables: [
        {
          initSubData: (data: Recordable) => {
            tableRef.value?.setData(data);
          },
          validate: () => {
            return tableRef.value?.validate() || Promise.resolve();
          },
          getSubData: () => {
            return tableRef.value?.getData() || {};
          },
        },
      ],
    });

  // 示例：节点审批完成后，回写业务单逻辑
  const bpmnSubmitAfter = async ({ state, data }) => {
    try {
      if (state === 'NODE_COMPLETED') {
        // 作为节点表单使用时,节点审批完成,处理相关业务逻辑,例如更新订单数据
        console.log(data);
      }
      return {};
    } catch (error) {
      return error;
    }
  };

  // 作为节点表单使用时，使用validate校验并返回业务数据,提供给流程自定义节点单据使用
  const validateAndGetData = async function () {
    /**
     * 1、返回子表数据时，需要自行处理，目前明细子表返回的数据只包含了脏数据，在流程中存储时可能需要包含全部数据
     **/
    const data = await validate();
    return data;
  };

  defineExpose({
    setFormData,
    submitForm,
    getFormData,
    validate: validateAndGetData, // 验证表单并返回业务数据,提供给流程自定义节点单据使用
    bpmnSubmitAfter,
  });
</script>
