<!--
 * @Description: 渲染组件，无法使用Vben的组件
-->
<template>
  <Modal
    title="预览(支持布局)"
    :open="state.visible"
    @ok="handleGetData"
    @cancel="handleCancel"
    okText="获取数据"
    cancelText="关闭"
    style="top: 20px"
    :destroyOnClose="true"
    :width="900"
  >
    <VFormCreate
      ref="vFormCreate"
      :form-config="state.formConfig"
      :bpmnTask="{ taskDefinitionKey: '12345' }"
      v-model:fApi="state.fApi"
      v-model:formModel="state.formData"
      @submit="onSubmit"
    >
      <template #slotName="{ formModel, field }">
        <a-input v-model:value="formModel[field]" placeholder="我是插槽传递的输入框" />
      </template>
    </VFormCreate>
    <JsonModal ref="jsonModal" />
  </Modal>
</template>
<script lang="ts" setup>
  import { reactive, ref } from 'vue';
  import { IFormConfig } from '../../typings/v-form-component';
  import { IAnyObject } from '../../typings/base-type';
  import VFormCreate from '../VFormCreate/index.vue';
  import { formatRules } from '../../utils';
  import { IVFormMethods } from '../../hooks/useVFormMethods';
  import JsonModal from '../VFormDesign/components/JsonModal.vue';
  import { IToolbarMethods } from '../../typings/form-type';
  import { Modal } from 'ant-design-vue';

  const jsonModal = ref<IToolbarMethods | null>(null);
  const vFormCreate = ref();
  const state = reactive<{
    formData: IAnyObject;
    visible: boolean;
    formConfig: IFormConfig;
    fApi: IVFormMethods;
  }>({
    formData: {
      deptCode: '1',
      deptName: '1',
      deptLevel: '01',
      deptType: '1',
    },
    formConfig: {} as IFormConfig,
    visible: false,
    fApi: {} as IVFormMethods,
  });

  // const formData: IAnyObject = ref({ _input_count_down_1: '1' });

  // const formConfig: IFormConfig = reactive({ formType: 'design' } as IFormConfig);

  /**
   * 显示Json数据弹框
   * @param jsonData
   */
  const showModal = (jsonData: IFormConfig) => {
    formatRules(jsonData.schemas);
    state.formConfig = jsonData as any;
    state.visible = true;
  };

  /**
   * 获取表单数据
   * @return {Promise<void>}
   */
  const handleCancel = () => {
    state.visible = false;
    state.formData = {
      '12345__z_select_dept_1': 'bm001',
      '12345__api_dict_select_2': '1',
    };
  };

  const handleGetData = async () => {
    // const _data = await state.fApi.validate?.();
    const _data = await vFormCreate?.value.validate?.();
    jsonModal.value?.showModal?.(_data);
  };

  const onSubmit = (_data: IAnyObject) => {
    //
  };

  const onCancel = () => {
    state.formData = {
      '12345__z_select_dept_1': 'bm001',
      '12345__api_dict_select_2': '1',
    };
  };
  defineExpose({
    showModal,
    onCancel,
  });
</script>
