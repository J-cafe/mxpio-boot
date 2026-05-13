<template>
  <BasicModal
    width="800px"
    destroyOnClose
    v-bind="$attrs"
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref, reactive } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { addExport, editExport, apiList } from '@mxpio/api';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';
  import { duplicateCheck } from '@mxpio/api/src/common/common';

  defineOptions({ name: 'ExportModal' });

  const emit = defineEmits(['success', 'register']);

  const formSchema: FormSchema[] = [
    {
      field: 'code',
      label: '方案编码',
      component: 'Input',
      colProps: {
        span: 12,
      },
      componentProps: () => {
        return {
          disabled: isUpdate.value,
        };
      },
      rules: [
        {
          // @ts-ignore
          validator: async (rule, value) => {
            return new Promise((resolve, reject) => {
              if (!value || unref(isUpdate)) return resolve();
              duplicateCheck({
                tableName: 'mb_excel_export_solution',
                column: 'code_',
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
      required: true,
    },
    {
      field: 'fileName',
      label: '文件名称',
      component: 'Input',
      colProps: {
        span: 12,
      },
      required: true,
    },
    {
      field: 'api',
      label: '接口',
      component: 'ApiSelect',
      componentProps: {
        showSearch: true,
      },
      colProps: {
        span: 12,
      },
      required: true,
    },
    {
      field: 'elementId',
      label: '组件标识',
      component: 'Input',
      colProps: {
        span: 12,
      },
      helpMessage: ['项目名:组件名:功能标识', '例如: system:MenuList:export'],
    },
    {
      field: 'showTitle',
      label: '显示标题',
      component: 'Switch',
      componentProps: {
        checkedChildren: '显示',
        unCheckedChildren: '隐藏',
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'title',
      label: '标题',
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'bgColor',
      label: '标题背景颜色',
      component: 'Input',
      colProps: {
        span: 12,
      },
      required: true,
    },
    {
      field: 'fontColor',
      label: '标题字体颜色',
      component: 'Input',
      colProps: {
        span: 12,
      },
      required: true,
    },
    {
      field: 'fontSize',
      label: '标题字体大小',
      component: 'InputNumber',
      colProps: {
        span: 12,
      },
      rules: [
        {
          type: 'number',
          min: 1,
          message: '请输入大于等于1的数字',
        },
      ],
      defaultValue: 16,
      required: true,
    },
    {
      field: 'columnBgColor',
      label: '列头背景颜色',
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'columnFontColor',
      label: '列头字体颜色',
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'columnFontSize',
      label: '列头字体大小',
      component: 'InputNumber',
      colProps: {
        span: 12,
      },
      rules: [
        {
          type: 'number',
          min: 1,
          message: '请输入大于等于1的数字',
        },
      ],
      defaultValue: 11,
    },
    {
      field: 'columnAlign',
      label: '列头字体对齐',
      component: 'RadioButtonGroup',
      componentProps: {
        options: [
          {
            label: '左对齐',
            value: 0,
          },
          {
            label: '居中',
            value: 1,
          },
          {
            label: '右对齐',
            value: 2,
          },
        ],
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'dataFontSize',
      label: '正文字体大小',
      component: 'InputNumber',
      colProps: {
        span: 12,
      },
      rules: [
        {
          type: 'number',
          min: 1,
          message: '请输入大于等于1的数字',
        },
      ],
      required: true,
    },
    {
      field: 'dataBgColor',
      label: '正文背景颜色',
      component: 'Input',
      colProps: {
        span: 12,
      },
      required: true,
    },
    {
      field: 'dataFontColor',
      label: '正文字体颜色',
      colProps: {
        span: 12,
      },
      component: 'Input',
    },
    {
      field: 'interceptorName',
      label: '拦截器',
      component: 'Input',
      colProps: {
        span: 12,
      },
    },
    {
      field: 'repeatHeader',
      label: '重复表头',
      component: 'Switch',
      componentProps: {
        checkedChildren: '显示',
        unCheckedChildren: '隐藏',
      },
      colProps: {
        span: 12,
      },
    },
    {
      field: 'showBorder',
      label: '显示边框',
      component: 'Switch',
      componentProps: {
        checkedChildren: '显示',
        unCheckedChildren: '隐藏',
      },
      colProps: {
        span: 12,
      },
    },
  ];

  const isUpdate = ref(true);
  let deptData = reactive({});
  const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    if (unref(isUpdate)) {
      setFieldsValue({
        ...data.record,
      });
      Object.assign(deptData, { ...data.record });
    } else {
      setFieldsValue({
        dataBgColor: '#ffffff',
        dataFontColor: '#000000',
        bgColor: '#ffffff',
        fontColor: '#000000',
        showTitle: true,
        columnAlign: 1,
        repeatHeader: false,
        showBorder: true,
      });
      deptData = reactive(Object.assign({}, {}));
    }
    getApiList();
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增导出模板' : '编辑导出模板'));

  // 获取路径列表
  async function getApiList() {
    const resultData = await apiList();
    const paths: string[] = [];
    const oriDataList: { label: any; value: any; hasCriteria: any }[] = [];
    resultData.forEach((item) => {
      const path = `${item.httpUrls[0]}:${item.hasCriteria}`;
      if (!paths.includes(path)) {
        // 去除重复接口
        paths.push(path);
        oriDataList.push({
          label: item.httpUrls[0],
          value: item.httpUrls[0],
          hasCriteria: item.hasCriteria,
        });
      }
    });

    updateSchema([
      {
        field: 'api',
        componentProps: { options: oriDataList },
      },
    ]);
  }

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      // TODO custom api
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addExport(Object.assign(deptData, values));
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editExport(Object.assign(deptData, values));
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
