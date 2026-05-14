<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
    <a-tabs>
      <a-tab-pane key="1" tab="班组成员">
        <DetailTable ref="tableRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, unref } from 'vue';
  import { BasicModal, BasicForm } from '@mxpio/components';
  import type { FormSchema } from '@mxpio/components';
  import { addEqpGroup } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { useCommon } from '@mxpio/hooks';
  import { Tabs as ATabs } from 'ant-design-vue';
  import DetailTable from './DetailTable.vue';

  defineOptions({ name: 'EqpGroupModal' });

  const { duplicateCheck } = useCommon();
  const emit = defineEmits(['success', 'register']);
  const formSchema: FormSchema[] = [
    {
      field: 'groupName',
      label: '群组名称',
      component: 'Input',
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'groupCode',
      label: '群组编码',
      component: 'Input',
      rules: [
        {
          // @ts-ignore
          validator: async (rule, value) => {
            if (!value || unref(isUpdate)) return true;
            return duplicateCheck({
              tableName: 'mb_erp_equipment_ps_group',
              column: 'group_code_',
              key: value,
              exclude: unref(isUpdate) ? value : '',
            });
          },
          trigger: 'blur',
        },
      ],
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'groupCategory',
      label: '群组类别',
      component: 'DictSelect',
      required: true,
      componentProps: () => {
        return {
          dictCode: 'ERP_EQUIPMENT_GROUP_CATEGORY',
        };
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'directorId',
      label: '主管',
      component: 'UserByDeptSelect',
      componentProps: () => {
        return {
          multiple: false,
        };
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'groupTel',
      label: '联系电话',
      component: 'Input',
      colProps: {
        span: 8,
      },
    },
    {
      field: 'remarks',
      label: '备注',
      component: 'InputTextArea',
      colProps: {
        span: 8,
      },
    },
    {
      field: 'isEnable',
      label: '是否启用',
      component: 'RadioButtonGroup',
      componentProps: () => {
        return {
          options: [
            {
              label: '是',
              value: 1,
            },
            {
              label: '否',
              value: 0,
            },
          ],
        };
      },
      colProps: {
        span: 8,
      },
    },
  ];
  const tableRef = ref<InstanceType<typeof DetailTable>>();
  const { registerForm, registerModal, getTitle, isDisabled, isUpdate, handleSubmit } =
    useModalFormCrud({
      title: '班组',
      formSchema,
      addApi: addEqpGroup,
      editApi: addEqpGroup, // 编辑时也调用添加接口，后台已处理
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
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
      defaultValues: {
        isEnable: 1,
      },
    });
</script>
