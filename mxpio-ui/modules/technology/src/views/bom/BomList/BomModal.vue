<template>
  <BasicModal
    width="80%"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm">
      <template #parentCode="{ model, field, disabled }">
        <MaterialCopySelect
          v-model:value="model[field]"
          :disabled="disabled"
          :multiple="false"
          @change="handleParentCodeChange"
        />
      </template>
    </BasicForm>
    <a-tabs>
      <a-tab-pane key="detailTable" tab="BOM明细" forceRender>
        <BomDetailTable ref="detailTable" />
      </a-tab-pane>
    </a-tabs>
    <template #footer>
      <a-button @click="closeModal">关闭</a-button>
      <a-button type="dashed" @click="handleSave" v-if="!isDisabled">保存草稿</a-button>
      <a-button type="primary" @click="handleSubmit" v-if="!isDisabled">提交</a-button>
    </template>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { saveBom, saveAndSubmitBom, getBom, getTmpBom } from '@mxpio/bizcommon';
  import { CuryTypeEnum } from '@mxpio/enums';
  import BomDetailTable from './BomDetailTable.vue';
  import MaterialCopySelect from './MaterialSelect/index.vue';
  import { useUserStore } from '@mxpio/stores';
  import { dateUtil } from '@mxpio/utils';
  import { useMessage } from '@mxpio/hooks';

  defineOptions({ name: 'BomModal' });

  const emit = defineEmits(['success', 'register']);
  const { createMessage } = useMessage();
  const isUpdate = ref(true);
  const isDisabled = ref(false);
  const isTmp = ref(false);
  const isCopy = ref(false);
  const detailTable = ref<InstanceType<typeof BomDetailTable>>();
  let formData: Recordable = {};

  const formSchema: FormSchema[] = [
    {
      field: 'changeCode',
      label: '变更单编号',
      component: 'Input',
      colProps: {
        span: 8,
      },
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'parentCode',
      label: '父项物料编码',
      slot: 'parentCode',
      itemProps: { autoLink: false },
      // component: 'MaterialSelect',
      // componentProps: {
      //   disabled: !isCopy.value,
      //   multiple: false,
      // },
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'parentName',
      label: '父项物料名称',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'parentSpec',
      label: '父项规格型号',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'parentDrawingNo',
      label: '父项物料图号',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'parentUnit',
      label: '父项物料单位',
      component: 'DictSelect',
      componentProps: () => {
        return {
          dictCode: 'ERP_TECH_UNIT_CODE',
          disabled: true,
        };
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'parentGroup',
      label: '物料组',
      component: 'ItemGroupSelect',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'bomType',
      label: 'BOM类型',
      component: 'DictSelect',
      componentProps: () => {
        return {
          dictCode: 'ERP_TECH_BOM_TYPE',
          disabled: true,
        };
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'useType',
      label: '使用状态',
      component: 'DictSelect',
      componentProps: () => {
        return {
          dictCode: 'ERP_TECH_USE_TYPE',
          disabled: true,
        };
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'version',
      label: '版本号',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'beginTime',
      label: '生效日期',
      component: 'DatePicker',
      componentProps: () => {
        return {
          valueFormat: 'YYYY-MM-DD HH:mm:ss',
          class: 'w-full',
          disabledDate: (current) => {
            const beginTime = formData.beginTime; // 获取当前行生效日期
            if (beginTime) {
              return current && dateUtil(current).isSameOrBefore(dateUtil(beginTime), 'day');
            } else {
              return current && dateUtil(current).isBefore(dateUtil(), 'day');
            }
          },
        };
      },
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'changeMan',
      label: '变更人',
      component: 'UserByDeptSelect',
      colProps: {
        span: 8,
      },
    },
    {
      field: 'changeDate',
      label: '变更日期',
      component: 'DatePicker',
      componentProps: {
        valueFormat: 'YYYY-MM-DD',
        class: 'w-full',
      },
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'changeMemo',
      label: '变更说明',
      component: 'InputTextArea',
      colProps: {
        span: 8,
      },
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, validate, getFieldsValue, updateSchema }] =
    useForm({
      labelWidth: 100,
      baseColProps: { span: 24 },
      schemas: formSchema,
      showActionButtonGroup: false,
    });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    isDisabled.value = !!data?.disabled;
    isTmp.value = !!data?.isTmp;
    isCopy.value = !!data?.isCopy;
    formData = data.record;
    if (unref(isUpdate)) {
      const res = await getBomData(data.record);
      if (!unref(isCopy)) {
        //正常变更、草稿编辑
        formData = res;
        setFieldsValue({
          ...res,
          beginTime: isTmp.value || isDisabled.value ? res.beginTime : null,
          useType: isTmp.value || isDisabled.value ? res.useType : '01',
        });
        formData.bomId = isTmp.value || isDisabled.value ? res.bomId : null;
      } else {
        //复制
        const initData = getInitBomData();
        const lineList = getCopyBomDetailData(res.bomInfoLineList);
        setFieldsValue({
          ...initData,
          bomType: '01',
        });
        formData = {
          ...initData,
          bomInfoLineList: lineList,
        };
      }
    } else {
      // 首次维护
      const initData = getInitBomData();
      setFieldsValue({
        ...initData,
        bomType: '01',
        ...data.record,
      });
    }
    detailTable.value?.setData({
      record: formData,
      isUpdate: unref(isUpdate),
      disabled: unref(isDisabled),
      isTmp: unref(isTmp),
      isCopy: unref(isCopy),
    });
    if (unref(isCopy)) {
      updateSchema([
        {
          field: 'parentCode',
          componentProps: { disabled: false },
        },
      ]);
    }
  });

  async function getBomData(data: Recordable) {
    if (isTmp.value) {
      return getTmpBom(data.parentCode);
    } else {
      return getBom(data.parentCode, {
        date: dateUtil(data.beginTime).format('YYYY-MM-DD HH:mm:ss'),
      });
    }
  }

  function getInitBomData() {
    const userStore = useUserStore();
    const { username } = userStore.getUserInfo || {};
    return {
      useType: '01',
      changeMan: username,
      changeDate: dateUtil().format('YYYY-MM-DD HH:mm:ss'),
      beginTime: null,
    };
  }

  function getCopyBomDetailData(lineList: Recordable[] = []) {
    lineList.forEach((item) => {
      item.bomId = null;
      item.bomCode = null;
      item.bomName = null;
      item.tmpCrudType = CuryTypeEnum.SAVE;
    });
    return lineList;
  }

  const getTitle = computed(() =>
    !unref(isDisabled) ? (unref(isTmp) ? '编辑BOM草稿' : '编辑BOM') : 'BOM详情',
  );

  // 提交BOM审核
  async function handleSubmit() {
    try {
      await validate();
      await detailTable.value?.validate();
      setModalProps({ confirmLoading: true });
      const values = getSubmitData();
      await saveAndSubmitBom(values);
      closeModal();
      createMessage.success('提交成功');
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  // 保存BOM草稿
  async function handleSave() {
    try {
      await validate();
      await detailTable.value?.validate();
      setModalProps({ confirmLoading: true });
      const values = getSubmitData();
      await saveBom(values);
      createMessage.success('保存成功');
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  function getSubmitData() {
    const data = getFieldsValue();
    const bomInfoLineList = detailTable.value?.getRecordset() || [];
    return {
      ...Object.assign(formData, data),
      beginTime: dateUtil(formData.beginTime).format('YYYY-MM-DD HH:mm:ss'),
      changeDate: dateUtil(formData.changeDate).format('YYYY-MM-DD') + ' 00:00:00',
      crudType: formData.bomId ? CuryTypeEnum.UPDATE : CuryTypeEnum.SAVE,
      bomInfoLineList: bomInfoLineList,
    };
  }

  function handleParentCodeChange(code: string, row: Recordable) {
    setFieldsValue({
      parentName: row?.parentName,
      parentSpec: row?.parentSpec,
      parentUnit: row?.parentUnit,
      parentDrawingNo: row?.parentDrawingNo,
      parentGroup: row?.parentGroup,
    });
  }
</script>
