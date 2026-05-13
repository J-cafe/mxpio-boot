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
  import { addMenu, editMenu, menuList } from '@mxpio/api';
  import { CuryTypeEnum } from '@mxpio/enums';

  defineOptions({ name: 'MenuModal' });
  const formSchema: FormSchema[] = [
    {
      field: 'parentId',
      label: '上级菜单',
      component: 'TreeSelect',
      componentProps: {
        fieldNames: {
          label: 'title',
          // key: 'id',
          value: 'id',
        },
        getPopupContainer: () => document.body,
      },
    },
    {
      field: 'urlScope',
      label: '菜单范畴',
      component: 'RadioButtonGroup',
      required: true,
      componentProps: {
        options: [
          {
            label: '平台',
            value: 'platform',
          },
          {
            label: '模块',
            value: 'modules',
          },
          {
            label: '菜单',
            value: 'menu',
          },
        ],
      },
    },
    {
      field: 'urlType',
      label: '菜单类型',
      component: 'RadioButtonGroup',
      required: true,
      componentProps: {
        options: [
          {
            label: '目录',
            value: 'M',
          },
          {
            label: '菜单',
            value: 'C',
          },
        ],
        onChange: (e) => {
          if (e?.nativeEvent?.constructor === Event && e?.target?.value === 'M') {
            setFieldsValue({
              component: 'blank',
              name: '',
            });
          }
        },
      },
      defaultValue: 'M',
    },
    {
      field: 'title',
      label: '菜单标题',
      component: 'Input',
      required: true,
    },
    {
      field: 'path',
      label: '路由地址',
      component: 'Input',
      required: true,
      helpMessage: ['访问的路由地址，如：`/user/list`'],
    },
    {
      field: 'component',
      label: '组件路径',
      component: 'Input',
      required: true,
      componentProps: ({ formModel }) => {
        return {
          disabled: formModel.urlType === 'M',
        };
      },
      defaultValue: 'blank',
      helpMessage: [
        '访问的组件路径',
        '系统内地址默认在`pages`目录下：如`system/user/index`',
        '外网地址需内链访问则以`http(s)://`开头',
      ],
    },
    {
      field: 'name',
      label: '组件名称',
      component: 'Input',
      componentProps: ({ formModel }) => {
        return {
          disabled: formModel.urlType === 'M',
        };
      },
      helpMessage: ['路由name页面缓存需要', '组件内定义的name', '如`UserList`'],
    },
    {
      field: 'icon',
      label: '图标',
      component: 'IconPicker',
      colProps: { span: 24 },
    },
    {
      field: 'order',
      label: '排序',
      component: 'InputNumber',
      colProps: { span: 24 },
      required: true,
    },
    {
      field: 'navigable',
      label: '是否显示',
      component: 'RadioButtonGroup',
      colProps: { span: 12 },
      componentProps: {
        options: [
          {
            label: '是',
            value: '1',
          },
          {
            label: '否',
            value: '0',
          },
        ],
      },
      defaultValue: '1',
      helpMessage: ['选择隐藏则路由将不会出现在侧边栏，但仍然可以访问'],
    },
    {
      field: 'outside',
      label: '外部打开',
      component: 'RadioButtonGroup',
      colProps: { span: 12 },
      componentProps: {
        options: [
          {
            label: '是',
            value: '1',
          },
          {
            label: '否',
            value: '0',
          },
        ],
      },
      defaultValue: '0',
      show: (formData) => {
        return formData.values?.urlType === 'C';
      },
      helpMessage: ['选择是否打开一个新窗口'],
    },
    {
      field: 'keepAlive',
      label: '是否缓存',
      component: 'RadioButtonGroup',
      colProps: { span: 12 },
      componentProps: {
        options: [
          {
            label: '是',
            value: '1',
          },
          {
            label: '否',
            value: '0',
          },
        ],
      },
      defaultValue: '1',
      show: (formData) => {
        return formData.values?.urlType === 'C';
      },
      helpMessage: [
        '选择是则会被`keep-alive`缓存',
        '需要匹配组件的`name`和地址保持一致，仅在多页签下有效。',
      ],
    },
  ];

  const emit = defineEmits(['success', 'register']);

  const isUpdate = ref(true);
  let menuData = reactive({});
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
        keepAlive: data.record.keepAlive ? '1' : '0',
        outside: data.record.outside ? '1' : '0',
        navigable: data.record.navigable ? '1' : '0',
      });
      Object.assign(menuData, { ...data.record });
    } else {
      const { record } = data;
      // if (data.record) {
      setFieldsValue({
        parentId: record?.id,
        urlScope: !record?.id ? 'platform' : record.urlScope === 'platform' ? 'modules' : 'menu',
        urlType: record?.id ? 'C' : 'M',
        component: !record?.id ? 'blank' : '',
        order: 1,
      });
      // }
      menuData = reactive(Object.assign({}, {}));
    }
    const treeData = await menuList();
    updateSchema([
      {
        field: 'parentId',
        componentProps: { treeData },
      },
    ]);
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '新增菜单' : '编辑菜单'));

  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      // TODO custom api
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addMenu(
          Object.assign(menuData, values, {
            keepAlive: values.keepAlive === '1',
            outside: values.outside === '1',
            navigable: values.navigable === '1',
          }),
        );
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editMenu(
          Object.assign(menuData, values, {
            keepAlive: values.keepAlive === '1',
            outside: values.outside === '1',
            navigable: values.navigable === '1',
          }),
        );
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
