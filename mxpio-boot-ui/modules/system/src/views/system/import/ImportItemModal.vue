<template>
  <BasicModal
    width="700px"
    destroyOnClose
    v-bind="$attrs"
    :title="getTitle"
    @register="registerModal"
    @ok="handleSubmit"
  >
    <a-tabs>
      <a-tab-pane key="1" tab="基本信息">
        <BasicForm @register="registerForm" />
      </a-tab-pane>
      <a-tab-pane key="2" tab="数据映射" forceRender>
        <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref, reactive, defineProps, nextTick } from 'vue';
  import { Tabs as ATabs } from 'ant-design-vue';
  import { VxeBasicTable, BasicModal, useModalInner, BasicForm, useForm } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance } from '@mxpio/components';
  import { preList, parserPostList, addRule, editRule } from '@mxpio/api';
  import { itemFormSchema, vxeTableColumns } from './import.data';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';
  import { setDataCrud } from '@mxpio/utils/src/common';

  defineOptions({ name: 'ImportItemModal' });

  const emit = defineEmits(['success', 'register']);
  const ATabPane = ATabs.TabPane;
  const tableRef = ref<VxeGridInstance>();

  const props = defineProps({
    importId: { type: String }, //导入模板id
  });

  const isUpdate = ref(true);

  let formData: Recordable = reactive({});
  let entries = ref([]);

  const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: itemFormSchema,
    showActionButtonGroup: false,
  });

  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'VxeTable',
    keepSource: true,
    minHeight: '200px',
    editConfig: { trigger: 'click', mode: 'cell', showStatus: true },
    columns: vxeTableColumns,
    toolbarConfig: {
      buttons: [
        {
          content: '新增',
          buttonRender: {
            name: 'AButton',
            attrs: {
              class: 'ml-2',
            },
            props: {
              type: 'primary',
              preIcon: 'mdi:page-next-outline',
            },
            events: {
              click: () => {
                tableRef.value?.insert({});
              },
            },
          },
        },
        {
          content: '删除',
          buttonRender: {
            name: 'AButton',
            props: {
              type: 'primary',
              danger: true,
              preIcon: 'mdi:delete-forever',
            },
            events: {
              click: () => {
                tableRef.value?.removeCheckboxRow();
              },
            },
          },
        },
      ],
      import: false,
      print: false,
      export: false,
    },
    data: entries.value,
    editRules: {
      key: [{ required: true, message: '请输入关键字', trigger: 'change' }],
      value: [{ required: true, message: '请输入值', trigger: 'change' }],
    },
    proxyConfig: { enabled: false },
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(
    async ({ isUpdate: isUpdate_, record }) => {
      resetFields();
      setModalProps({ confirmLoading: false });
      isUpdate.value = !!isUpdate_;
      if (unref(isUpdate)) {
        setFieldsValue({
          ...record,
        });
        Object.assign(formData, { ...record });
        entries.value = record.entries;
        nextTick(() => {
          tableRef.value?.loadData(entries.value);
        });
      } else {
        formData = reactive(Object.assign({}, {}));
      }
      getParserPostList();
      getParserPreList();
    },
  );

  const getTitle = computed(() => (!unref(isUpdate) ? '新增导入规则' : '编辑导入规则'));

  async function handleSubmit() {
    try {
      let values = await validate();
      const error = await tableRef.value?.validate();
      if (error) {
        return;
      }
      const data: Recordable | undefined = tableRef.value?.getRecordset();
      if (!data) {
        closeModal();
        return false;
      }

      // 获取待保存的脏数据
      const entries: Recordable[] = setDataCrud(data, isUpdate.value);
      entries.forEach((item) => {
        item.mappingRuleId = formData.id;
      });

      setModalProps({ confirmLoading: true });
      // TODO custom api
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        values.importerSolutionId = props.importId;
        await addRule(Object.assign(formData, values, { entries }));
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editRule(Object.assign(formData, values, { entries }));
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  // 请求前置解析器
  async function getParserPostList() {
    const list = await preList();
    list.forEach((item) => {
      item.label = item.parserName;
      item.value = item.beanId;
    });
    updateSchema([
      {
        field: 'cellPreParserBean',
        componentProps: { options: list },
      },
    ]);
  }

  // 请求后置解析器
  async function getParserPreList() {
    const list = await parserPostList();
    list.forEach((item) => {
      item.label = item.parserName;
      item.value = item.beanId;
    });
    updateSchema([
      {
        field: 'cellPostParserBean',
        componentProps: { options: list },
      },
    ]);
  }
</script>
