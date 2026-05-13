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
      <a-tab-pane key="leadForm" tab="提前期属性" forceRender>
        <LeadForm ref="leadFormRef" />
      </a-tab-pane>
      <a-tab-pane key="planForm" tab="计划属性" forceRender>
        <PlanForm ref="planFormRef" />
      </a-tab-pane>
      <a-tab-pane key="purchaseForm" tab="采购属性" forceRender>
        <PurchaseForm ref="purchaseFormRef" />
      </a-tab-pane>
      <a-tab-pane key="storeForm" tab="仓储属性" forceRender>
        <StoreForm ref="storeFormRef" />
      </a-tab-pane>
      <a-tab-pane key="rawForm" tab="材料属性" forceRender>
        <MaterialForm ref="materialFormRef" />
      </a-tab-pane>
      <a-tab-pane key="drawingTable" tab="设计图纸" forceRender>
        <DesignfileTable ref="designfileTableRef" />
      </a-tab-pane>
      <a-tab-pane key="qualityForm" tab="质量属性" forceRender>
        <QualityForm ref="qualityFormRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { duplicateCheck } from '@mxpio/api';
  import { addItem, editItem } from '@mxpio/bizcommon';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';
  import { useMessage } from '@mxpio/hooks';
  import LeadForm from './LeadForm.vue';
  import PlanForm from './PlanForm.vue';
  import PurchaseForm from './PurchaseForm.vue';
  import StoreForm from './StoreForm.vue';
  import MaterialForm from './MaterialForm.vue';
  import QualityForm from './QualityForm.vue';
  import DesignfileTable from './DesignfileTable.vue';

  defineOptions({ name: 'MaterialModal' });

  const emit = defineEmits(['success', 'register']);
  const isUpdate = ref(true);
  const isDisabled = ref(false);
  const leadFormRef = ref<InstanceType<typeof LeadForm>>();
  const planFormRef = ref<InstanceType<typeof PlanForm>>();
  const purchaseFormRef = ref<InstanceType<typeof PurchaseForm>>();
  const storeFormRef = ref<InstanceType<typeof StoreForm>>();
  const materialFormRef = ref<InstanceType<typeof MaterialForm>>();
  const qualityFormRef = ref<InstanceType<typeof QualityForm>>();
  const designfileTableRef = ref<InstanceType<typeof DesignfileTable>>();
  let formData: Recordable = {};
  const { createMessage } = useMessage();
  const formSchema: FormSchema[] = [
    {
      field: 'itemCode',
      label: '物料编码',
      component: 'Input',
      required: true,
      colProps: {
        span: 8,
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
                tableName: 'mb_erp_inventory_item',
                column: 'item_code_',
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
      field: 'itemName',
      label: '物料名称',
      component: 'Input',
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'itemSpec',
      label: '规格型号',
      component: 'Input',
      colProps: {
        span: 8,
      },
    },
    {
      field: 'itemGroupCode',
      label: '物料组',
      component: 'ItemGroupSelect',
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'unitCode',
      label: '单位',
      component: 'DictSelect',
      required: true,
      componentProps: () => {
        return {
          dictCode: 'ERP_TECH_UNIT_CODE',
        };
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'drawingNo',
      label: '图号',
      component: 'Input',
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'drawingFileNo',
      label: '图档号',
      component: 'Input',
      colProps: {
        span: 8,
      },
    },
    {
      field: 'scale',
      label: '数量精度',
      component: 'Input',
      colProps: {
        span: 8,
      },
    },
    {
      field: 'itemSource',
      label: '物料来源',
      component: 'DictSelect',
      required: true,
      componentProps: ({ formModel }) => {
        return {
          dictCode: 'ERP_TECH_ITEM_SOURCE',
          onChange: (value) => {
            if (value === '1') {
              formModel.manufactureAble = '1';
              formModel.outsourceAble = '0';
            } else {
              formModel.manufactureAble = '0';
              formModel.outsourceAble = '0';
            }
          },
        };
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'defaultWhCode',
      label: '默认仓库',
      component: 'WareHouseSelect',
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'mainWorkshop',
      label: '主制车间',
      component: 'WorkShopSelect',
      required: true,
      ifShow: (formData) => {
        return formData.values?.manufactureAble === '1' && formData.values?.itemSource === '1';
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'workCenterCode',
      label: '工作中心',
      component: 'WorkCenterSelect',
      componentProps: ({ formModel }) => {
        return {
          disabled: !formModel.mainWorkshop,
          filters: {
            'workShopCode@EQ': formModel.mainWorkshop,
          },
          alwaysLoad: true,
        };
      },
      required: true,
      ifShow: (formData) => {
        return formData.values?.manufactureAble === '1' && formData.values?.itemSource === '1';
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'itemPrice',
      label: '单价',
      component: 'InputNumber',
      componentProps: {
        precision: 2,
      },
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'materialBrand',
      label: '材料牌号',
      component: 'Input',
      colProps: {
        span: 8,
      },
      rules: [
        {
          // @ts-ignore
          validator: async (rule, value) => {
            return new Promise((resolve, reject) => {
              const { material } = getFieldsValue();
              if (!value && material === '1') {
                return reject('请填写材料牌号');
              } else {
                return resolve();
              }
            });
          },
          trigger: 'blur',
        },
      ],
    },
    {
      field: 'manufactureAble',
      label: '可自制',
      component: 'RadioButtonGroup',
      componentProps: () => {
        return {
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
        };
      },
      ifShow: (formData) => {
        return formData.values?.itemSource === '1';
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'outsourceAble',
      label: '可委外',
      component: 'RadioButtonGroup',
      componentProps: () => {
        return {
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
        };
      },
      ifShow: (formData) => {
        return formData.values?.itemSource === '1';
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'virtualPart',
      label: '是否虚拟件',
      component: 'RadioButtonGroup',
      componentProps: () => {
        return {
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
        };
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'material',
      label: '是否材料',
      component: 'RadioButtonGroup',
      componentProps: () => {
        return {
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
        };
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'saleAble',
      label: '可销售',
      component: 'RadioButtonGroup',
      componentProps: () => {
        return {
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
        };
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'lendPermit',
      label: '可借用',
      component: 'RadioButtonGroup',
      componentProps: () => {
        return {
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
        };
      },
      colProps: {
        span: 8,
      },
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
    if (unref(isUpdate)) {
      setFieldsValue({
        ...data.record,
      });
      formData = { ...data.record };
    } else {
      setFieldsValue({
        virtualPart: '0',
        material: '0',
        scale: 0,
        mainWorkshop: null,
        saleAble: '1',
        lendPermit: 0,
        fixedLeadTime: 0,
        varLeadTime: 0,
        leadLotSize: 1,
        outsourcing: 0,
        supplyManager: 1,
        safeStock: 0,
        batchControl: 1,
        locationControl: 0,
        incrementLotSize: 1,
      });
    }
    updateForm(data);
  });

  function updateForm(data) {
    leadFormRef.value?.updateForm(data);
    planFormRef.value?.updateForm(data);
    purchaseFormRef.value?.updateForm(data);
    storeFormRef.value?.updateForm(data);
    materialFormRef.value?.updateForm(data);
    qualityFormRef.value?.updateForm(data);
    designfileTableRef.value?.updateForm(data);
  }

  const getTitle = computed(() => (!unref(isUpdate) ? '新增物料' : '编辑物料'));

  async function handleSubmit() {
    try {
      await allValidate();
      setModalProps({ confirmLoading: true });
      const res = await submitForm();
      if (!res) {
        return;
      }
      let flag = true;
      res.forEach((item) => {
        !item && (flag = false);
      });
      if (flag) {
        closeModal();
        emit('success');
      } else {
        createMessage.warning('提交失败');
      }
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  async function allValidate() {
    try {
      return Promise.all([
        validate(),
        leadFormRef.value?.validate(),
        planFormRef.value?.validate(),
        purchaseFormRef.value?.validate(),
        storeFormRef.value?.validate(),
        materialFormRef.value?.validate(),
        qualityFormRef.value?.validate(),
        designfileTableRef.value?.validate(),
      ]);
    } catch (error) {
      console.error('表单提交失败', error);
    }
  }

  async function submitForm() {
    try {
      const values = getFieldsValue();
      if (!unref(isUpdate)) {
        values.crudType = CuryTypeEnum.SAVE;
        await addItem(values);
      } else {
        values.crudType = CuryTypeEnum.UPDATE;
        await editItem(Object.assign(formData, values));
      }
      return Promise.all([
        leadFormRef.value?.submitForm(values.itemCode),
        planFormRef.value?.submitForm(values.itemCode),
        purchaseFormRef.value?.submitForm(values.itemCode),
        storeFormRef.value?.submitForm(values.itemCode),
        materialFormRef.value?.submitForm(values.itemCode),
        qualityFormRef.value?.submitForm(values.itemCode),
        designfileTableRef.value?.submitForm(values.itemCode),
      ]);
    } catch (error) {
      console.error('表单提交失败', error);
    }
  }
</script>
