<template>
  <BasicModal
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
  import { addAuth, editAuth, apiList } from '@mxpio/api';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';

  defineOptions({ name: 'AuthModal' });

  const emit = defineEmits(['success', 'register']);

  const props = defineProps({
    /**
     * Whether to refresh the interface when changing
     */
    urlId: { type: String },
  });

  const authFormSchema: FormSchema[] = [
    {
      field: 'title',
      label: '名称',
      component: 'Input',
      required: true,
    },
    {
      field: 'path',
      label: '路径',
      component: 'ApiSelect',
      required: true,
      componentProps: {
        showSearch: true,
        onChange: (e, data) => {
          if (data) {
            setFieldsValue({
              hasCriteria: data.hasCriteria,
            });
          }
        },
      },
    },
    {
      field: 'hasCriteria',
      label: '数据权限',
      component: 'Switch',
      componentProps: { disabled: true },
    },
    {
      field: 'elementId',
      label: '组件标识',
      component: 'Input',
      helpMessage: ['项目简称:组件名:功能标识,例如: system:MenuList:add"'],
    },
    {
      field: 'description',
      label: '描述',
      component: 'Input',
    },
  ];

  const isUpdate = ref(true);
  let menuData = reactive({});
  const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: authFormSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    console.log(data);
    if (unref(isUpdate)) {
      setFieldsValue({
        ...data.record,
      });
      Object.assign(menuData, { ...data.record });
    }
    getApiList();
  });

  // 获取路径列表
  async function getApiList() {
    const resultData = await apiList();
    const paths: string[] = [];
    const treeData: { label: string; value: string; hasCriteria: boolean }[] = [];
    resultData.forEach((item) => {
      const path = `${item.httpUrls[0]}:${item.hasCriteria}`;
      if (!paths.includes(path)) {
        // 去除重复接口
        paths.push(path);
        treeData.push({
          label: item.httpUrls[0],
          value: item.httpUrls[0],
          hasCriteria: item.hasCriteria,
        });
      }
    });
    updateSchema([
      {
        field: 'path',
        componentProps: { options: treeData },
      },
    ]);
  }

  const getTitle = computed(() => (!unref(isUpdate) ? '新增权限' : '编辑权限'));

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      // TODO custom api
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addAuth(Object.assign(menuData, values, { parentId: props.urlId }));
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editAuth(Object.assign(menuData, values, { parentId: props.urlId }));
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
