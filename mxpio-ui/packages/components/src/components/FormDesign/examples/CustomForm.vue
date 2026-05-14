<template>
  <BasicForm @register="registerForm" :disabled="disabled" />
</template>
<script lang="ts" setup>
  import { ref, unref } from 'vue';
  import { BasicForm, useForm, FormSchema } from '../../Form';
  import { duplicateCheck } from '@mxpio/api/src/common/common';

  defineOptions({ name: 'CustomForm' });

  const disabled = ref(false);
  const isBpmn = ref(false);
  const isUpdate = ref(true);
  const formSchema: FormSchema[] = [
    {
      field: 'deptCode',
      label: '部门编码1',
      component: 'Input',
      required: true,
      rules: [
        {
          // @ts-ignore
          validator: async (rule, value) => {
            return new Promise((resolve, reject) => {
              if (!value || unref(isUpdate)) return resolve();
              duplicateCheck({
                tableName: 'mb_dept',
                column: 'dept_code_',
                key: value,
                exclude: unref(isUpdate) ? value : '',
              })
                .then((res) => {
                  if (res === 1) {
                    return reject('编码已存在');
                  }
                  return resolve();
                })
                .catch((err) => {
                  reject(err.message || '验证失败');
                });
            });
          },
          trigger: 'blur',
        },
      ],
    },
    {
      field: 'deptName',
      label: '部门名称1',
      component: 'Input',
      required: true,
    },
    {
      field: 'deptDesc',
      label: '描述',
      component: 'InputTextArea',
    },
  ];

  const [registerForm, { setFieldsValue, getFieldsValue, validate }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  // 设置自定义表单数据
  function setFormData(data: Recordable) {
    disabled.value = data.disabled;
    isUpdate.value = data.isUpdate || false;
    isBpmn.value = data.isBpmn || false;
    setFieldsValue(data.record || {});
  }

  // 作为节点表单时，外部获取自定义表单数据
  async function getFormData() {
    try {
      await validate();
      return getFieldsValue();
    } catch (error) {
      console.log(error);
    }
  }

  // 暴露自定义表单bpmnSubmitAfter方法，更新自定义表单数据
  const bpmnSubmitAfter = async ({ state }) => {
    try {
      console.log(state);
      if (state === 'COMPLETED') {
        // 审批完成回写业务单状态为已审核
        // const res = await getAction(this.url.save + this.bpmnData.businessKey + '/03') // 保存更新后的行数据
        // return res
      } else if (state === 'INTERNALLY_TERMINATED') {
        // 回写业务单状态为已终止
        // const res = await getAction(this.url.save + this.bpmnData.businessKey + '/99') // 保存更新后的行数据
        // return res
      }
      return {};
    } catch (error) {
      return error;
    }
  };

  defineExpose({
    validate,
    bpmnSubmitAfter,
    setFormData,
    getFormData,
  });
</script>
