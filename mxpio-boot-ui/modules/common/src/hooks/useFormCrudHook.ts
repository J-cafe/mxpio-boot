import { ref, unref, Ref, nextTick } from 'vue';
import { useForm } from '@mxpio/components';
import type { FormActionType, FormProps } from '@mxpio/components';
import { CuryTypeEnum } from '@mxpio/enums';

// 定义返回类型接口
interface UseFormCrudReturn {
  isUpdate: Ref<boolean>;
  isDisabled: Ref<boolean>;
  registerForm: (formInstance: FormActionType) => void;
  resetFields: () => Promise<void>;
  setFieldsValue: (values: Recordable) => Promise<void>;
  validate: () => Promise<any>;
  getFieldsValue: () => Recordable;
  formDataRef: Recordable;
  setFormData: (data: Recordable) => Promise<void>;
  submitForm: () => Promise<any>;
  getFormData: (values: Recordable) => Recordable | Promise<Recordable>;
}

// 定义明细表配置接口
interface SubTableConfig {
  initSubData: (params: any) => void;
  validate?: () => Promise<any>;
  getSubData?: () => Recordable | Promise<Recordable>;
}

// 定义接口类型接口
export interface UseFormCrudOptions {
  formSchema: any[];
  formProps?: FormProps;
  defaultValues?: Recordable;
  classifyIntoFormData?: (params?: any) => Recordable | Promise<Recordable>;
  subTables?: SubTableConfig[];
  submitBefore?: (params?: any) => Recordable | Promise<any>;
  submitAfter?: (params?: any) => void | Promise<any>;
  saveApi?: (params: any) => Promise<any>;
  editApi?: (params: any) => Promise<any>;
  addApi?: (params: any) => Promise<any>;
}

export function useFormCrudHook(options: UseFormCrudOptions): UseFormCrudReturn {
  const {
    formSchema,
    formProps = {},
    defaultValues = {},
    subTables = [],
    submitBefore,
    submitAfter,
    saveApi,
    editApi,
    addApi,
    classifyIntoFormData,
  } = options;

  // 状态管理
  const isUpdate = ref(true);
  const isDisabled = ref(false);
  const isBpmn = ref(false);
  const formData = ref<Recordable>({});

  const formPropsDefault = {
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: formSchema,
    showActionButtonGroup: false,
  };

  const formPropsAll = Object.assign(formPropsDefault, formProps);
  // 主表表单配置
  const [registerForm, { resetFields, setFieldsValue, validate, getFieldsValue }, formDataRef] =
    useForm(formPropsAll);

  async function setFormData(data) {
    // 初始化模式状态
    isUpdate.value = !!data?.isUpdate;
    isDisabled.value = !!data?.disabled;
    isBpmn.value = !!data?.isBpmn;
    if (unref(isUpdate)) {
      // 编辑/查看模式 - 加载数据
      formData.value = { ...data.record };
      await setFieldsValue(formData.value);
    } else {
      // 修复默认值未在表单项，导致新增模式无法显示的问题
      formData.value = { ...defaultValues };
      // 新增模式 - 设置默认值
      await setFieldsValue(defaultValues);
    }
    // 关键修改：等待DOM更新后再初始化子表
    await nextTick();
    // 加载从表数据
    if (subTables && subTables.length > 0) {
      subTables.forEach((subTableRef) => {
        const subTable = unref(subTableRef); // 获取ref实际值
        subTable?.initSubData({
          record: data.record || {},
          isUpdate: unref(isUpdate),
          disabled: unref(isDisabled),
          isBpmn: unref(isBpmn),
        });
      });
    }
  }

  async function submitForm() {
    try {
      const values = await validateAll();
      // 提交前处理
      if (submitBefore && typeof submitBefore === 'function') {
        await submitBefore();
      }
      const res = await save(values);
      submitAfter?.(res);
    } catch (error) {
      console.error('提交表单失败:', error);
      throw error;
    }
  }

  async function getFormData(values: Recordable) {
    try {
      let submitData = {};
      // 处理主表数据
      if (classifyIntoFormData && typeof classifyIntoFormData === 'function') {
        submitData = await classifyIntoFormData(values);
      } else {
        let subTableDatas = {};
        // 1. 将forEach改为map，收集所有异步操作的Promise
        const subTablePromises = subTables.map(async (subTableRef) => {
          const subTable = unref(subTableRef); // 获取ref实际值
          const subTableData = await subTable?.getSubData?.();
          return subTableData; // 返回获取到的子表数据
        });
        // 2. 等待所有子表数据获取完成
        const subTableResults = await Promise.all(subTablePromises);
        // 3. 处理所有子表数据（此时已确保所有异步操作完成）
        subTableResults.forEach((subTableData) => {
          if (subTableData) {
            subTableDatas = {
              ...subTableDatas,
              ...subTableData,
            };
          }
        });
        submitData = {
          ...formData.value,
          ...values,
          ...subTableDatas,
          crudType: unref(isUpdate) ? CuryTypeEnum.UPDATE : CuryTypeEnum.SAVE,
        };
      }
      return submitData;
    } catch (error) {
      console.log(error);
      throw error;
    }
  }

  async function save(values: Recordable) {
    // const submitData = await getFormData(values);
    if (saveApi) {
      return await saveApi(values);
    }
    if (unref(isUpdate)) {
      return await editApi?.(values);
    } else {
      return await addApi?.(values);
    }
  }

  async function validateAll() {
    try {
      // 主表验证
      const values = await validate();
      // 加载从表数据
      if (subTables && subTables.length > 0) {
        // Promise.all 等待所有验证完成
        await Promise.all(
          subTables.map((subTableRef) => {
            const subTable = unref(subTableRef); // 获取ref实际值
            // 调用子表的验证方法
            if (subTable?.validate) {
              return subTable.validate();
            }
          }),
        );
      }
      const submitData = await getFormData(values);
      return submitData;
    } catch (error) {
      console.error('表单验证失败:', error);
      throw error;
    }
  }

  return {
    registerForm,
    resetFields,
    setFieldsValue,
    validate: validateAll,
    isUpdate,
    isDisabled,
    getFieldsValue,
    formDataRef,
    setFormData,
    submitForm,
    getFormData,
  };
}
