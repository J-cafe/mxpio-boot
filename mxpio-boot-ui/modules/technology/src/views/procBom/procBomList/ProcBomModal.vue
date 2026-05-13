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
    <BasicForm :disabled="isDisabled" @register="registerForm" />
    <a-tabs>
      <a-tab-pane key="detailTable" tab="BOM明细" forceRender>
        <DetailTable ref="detailTable" />
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
  import { saveProcBom, saveAndSubmitProcBom, getProcBom, getTmpProcBom } from '@mxpio/bizcommon';
  import { CuryTypeEnum } from '@mxpio/enums';
  import DetailTable from './DetailTable.vue';
  import { useUserStore } from '@mxpio/stores';
  import { dateUtil } from '@mxpio/utils';

  defineOptions({ name: 'ProcBomModal' });

  const emit = defineEmits(['success', 'register']);
  const isUpdate = ref(true);
  const isDisabled = ref(false);
  const isTmp = ref(false);
  const isCopy = ref(false);
  const detailTable = ref<InstanceType<typeof DetailTable>>();
  let formData: Recordable = {};

  const formSchema: FormSchema[] = [
    {
      field: 'productItemCode',
      label: '物料编码',
      component: 'MaterialSelect',
      componentProps: ({ formActionType }) => {
        return {
          multiple: false,
          onSelect: (value, item) => {
            const { setFieldsValue } = formActionType;
            setFieldsValue({
              productItemCode: item.itemCode,
              productItemName: item.itemName || '',
              itemSpec: item.itemSpec || '',
              productDrawingNo: item.drawingNo || '',
            });
          },
          filters: {
            'itemSource@EQ': '1',
          },
          disabled: (isDisabled.value || isTmp.value || isUpdate.value) && !isCopy.value,
        };
      },
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'productItemName',
      label: '物料名称',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'itemSpec',
      label: '规格型号',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'productDrawingNo',
      label: '图号',
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
            const beginTime = formData?.beginTime; // 获取当前行生效日期
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
      ifShow: !isUpdate.value,
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
      ifShow: !isUpdate.value,
    },
    {
      field: 'changeMemo',
      label: '变更说明',
      component: 'InputTextArea',
      colProps: {
        span: 8,
      },
      ifShow: !isUpdate.value,
    },
    {
      field: 'changeAttachFile',
      label: '变更附件',
      component: 'Upload',
      colProps: {
        span: 8,
      },
      ifShow: !isUpdate.value,
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, validate, getFieldsValue }] = useForm({
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
    formData = data.record || {};
    if (unref(isUpdate)) {
      const res = await getBomData(data.record);
      if (!unref(isCopy)) {
        //变更、草稿编辑、详情,变更时需要清空开始时间、使用状态、工艺路线id、生效时间、行数据id等
        formData = res;
        formData.routId = isTmp.value || isDisabled.value ? res.routId : null;
        formData.useType = isTmp.value || isDisabled.value ? res.useType : '01';
        setFieldsValue({
          ...res,
          beginTime: isTmp.value || isDisabled.value ? res.beginTime : null,
        });
        const lineList =
          !isTmp.value && !isDisabled.value ? getEditBomDetailData(res.lineList) : res.lineList;
        formData = {
          ...formData,
          lineList: lineList,
        };
      } else {
        //复制
        const initData = getInitBomData();
        const lineList = getCopyBomDetailData(res.lineList);
        setFieldsValue({
          ...initData,
          productItemCode: '',
        });
        formData = {
          ...initData,
          productItemCode: '',
          lineList: lineList,
        };
      }
    } else {
      // 新增
      const initData = getInitBomData();
      setFieldsValue({
        ...initData,
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
  });

  async function getBomData(data: Recordable) {
    if (isTmp.value) {
      return getTmpProcBom(data.productItemCode);
    } else {
      return getProcBom(data.productItemCode, {
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
      item.routProcId = item.id;
      item.id = null;
      item.tmpCrudType = CuryTypeEnum.SAVE;
    });
    return lineList;
  }

  function getEditBomDetailData(lineList: Recordable[] = []) {
    lineList.forEach((item) => {
      item.routProcId = item.id;
      item.id = null;
      item.tmpCrudType = CuryTypeEnum.NONE;
    });
    return lineList;
  }

  const getTitle = computed(() =>
    !unref(isDisabled)
      ? unref(isTmp)
        ? '编辑产品工艺路线'
        : '编辑产品工艺路线'
      : '产品工艺路线详情',
  );

  // 提交BOM审核
  async function handleSubmit() {
    try {
      await validate();
      await detailTable.value?.validate();
      setModalProps({ confirmLoading: true });
      const values = getSubmitData();
      await saveAndSubmitProcBom(values);
      closeModal();
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
      await saveProcBom(values);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  function getSubmitData() {
    const data = getFieldsValue();
    const lineList = detailTable.value?.getRecordset() || [];
    return {
      ...Object.assign(formData, data),
      beginTime: dateUtil(formData.beginTime).format('YYYY-MM-DD HH:mm:ss'),
      changeDate: dateUtil(formData.changeDate).format('YYYY-MM-DD') + ' 00:00:00',
      crudType: formData.routId ? CuryTypeEnum.UPDATE : CuryTypeEnum.SAVE,
      lineList: lineList,
    };
  }
</script>
