<template>
  <BasicModal
    width="80%"
    v-bind="$attrs"
    :destroyOnClose="true"
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm class="invplan_warp" :disabled="isDisabled" @register="registerForm" />
    <a-tabs>
      <a-tab-pane key="detailTable" tab="盘点范围" forceRender>
        <SuperQuery
          ref="rangeRef"
          :disabled="isDisabled"
          :criteria="criteria"
          :fieldList="fieldList"
        />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm } from '@mxpio/components';
  import { addInvplanApi, editInvplanApi } from '@mxpio/bizcommon';
  import { useModalFormCrud, SuperQuery } from '@mxpio/common';
  import { formSchema } from './inventoryPlan.data';
  import { dateUtil } from '@mxpio/utils';
  import { ref } from 'vue';

  defineOptions({ name: 'InventoryPlanModal' });

  const emit = defineEmits(['success', 'register']);
  const rangeRef = ref<InstanceType<typeof SuperQuery>>();
  const criteria = ref({});
  const fieldList = ref([
    { component: 'Input', field: 'itemCode', label: '物料编码' },
    { component: 'ItemGroupSelect', field: 'itemGroupCode', label: '物料组' },
  ]);
  const { registerForm, setFieldsValue, registerModal, getTitle, isDisabled, handleSubmit } =
    useModalFormCrud({
      title: '盘点计划',
      formSchema,
      addApi: addInvplanApi,
      editApi: editInvplanApi,
      submitAfter: () => {
        criteria.value = {};
        emit('success'); // 在这里使用组件的emit
      },
      defaultValues: {
        startTime: dateUtil().format('YYYY-MM-DD'),
        isEnable: '0',
        orderStatus: '1',
        isExcludeZero: '0',
        isAppend: '0',
      },
      subTables: [
        {
          initSubData: () => {
            // rangeRef.value?.setData(data);
          },
          validate: () => {
            return rangeRef.value?.validate() || Promise.resolve();
          },
          getSubData: () => {
            const criteria = rangeRef.value?.getData();
            return criteria ? { criteria: encodeURIComponent(JSON.stringify(criteria)) } : {};
          },
        },
      ],
      openAfter: (data) => {
        const { record } = data;
        setFieldsValue({
          isExcludeZero: record?.isExcludeZero === 1 ? '1' : '0',
          isAppend: record?.isAppend === 1 ? '1' : '0',
          orderStatus: record?.orderStatus === 1 ? '1' : '0',
        });
        criteria.value =
          !!record?.criteria && typeof record?.criteria === 'string'
            ? JSON.parse(decodeURIComponent(record?.criteria))
            : undefined;
      },
    });
</script>
<style lang="less">
  .invplan_warp {
    .ant-divider {
      margin-top: 0;
    }
  }
</style>
