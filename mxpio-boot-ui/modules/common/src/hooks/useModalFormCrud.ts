import { ref, computed, unref, Ref, ComputedRef, nextTick } from 'vue';
import { useModalInner, useForm } from '@mxpio/components';
import type { FormActionType, ModalMethods, FormProps, ModalProps } from '@mxpio/components';
import { CuryTypeEnum } from '@mxpio/enums';
// import { useMessage } from '@mxpio/hooks';

// 定义返回类型接口
export interface UseModalFormReturn {
  isUpdate: Ref<boolean>;
  isDisabled: Ref<boolean>;
  registerForm: (formInstance: FormActionType) => void;
  resetFields: () => Promise<void>;
  setFieldsValue: (values: Recordable) => Promise<void>;
  validate: () => Promise<any>;
  registerModal: (modalInstance: ModalMethods, uuid: number) => void;
  handleSubmit: () => Promise<any>;
  getTitle: ComputedRef<string>;
  getFieldsValue: () => Recordable;
  formDataRef: Recordable;
  closeModal: () => void;
  setModalProps: (props: Partial<ModalProps>) => void;
}

// 定义明细表配置接口
export interface SubTableConfig {
  initSubData: (params: any) => void;
  validate?: () => Promise<any>;
  getSubData?: () => Recordable | Promise<Recordable>;
}

// 定义接口类型接口
export interface UseModalFormOptions {
  formSchema: any[];
  saveApi?: (params: any) => Promise<any>;
  editApi?: (params: any) => Promise<any>;
  addApi?: (params: any) => Promise<any>;
  title?: string | Ref<string>;
  formProps?: FormProps;
  defaultValues?: Recordable;
  openBefore?: (params?: any) => Recordable | Promise<any>;
  openAfter?: (params?: any) => void | Promise<any>;
  submitBefore?: (params?: any) => Recordable | Promise<any>;
  submitAfter?: (params?: any) => void | Promise<any>;
  classifyIntoFormData?: (params?: any) => Recordable | Promise<Recordable>;
  subTables?: SubTableConfig[];
}

export function useModalFormCrud(options: UseModalFormOptions): UseModalFormReturn {
  const {
    formSchema,
    saveApi,
    editApi,
    addApi,
    title,
    formProps = {},
    defaultValues = {},
    openBefore,
    openAfter,
    submitBefore,
    submitAfter,
    classifyIntoFormData,
    subTables = [],
  } = options;

  // 状态管理
  const isUpdate = ref(true);
  const isDisabled = ref(false);
  const formData = ref<Recordable>({});

  // 消息提示
  // const { createMessage } = useMessage();

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

  // 模态框配置
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    // 打开前处理
    if (openBefore && typeof openBefore === 'function') {
      await openBefore(data);
    }
    // 初始化模式状态
    isUpdate.value = !!data?.isUpdate;
    isDisabled.value = !!data?.disabled;
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
        });
      });
    }
    // 打开后处理
    if (openAfter && typeof openAfter === 'function') {
      openAfter(data);
    }
  });

  // 标题计算
  const getTitle = computed(() => {
    const baseTitle = unref(title) || '';
    if (unref(isDisabled)) return `${baseTitle}查看`;
    return unref(isUpdate) ? `${baseTitle}编辑` : `新增${baseTitle}`;
  });

  // 提交表单
  async function handleSubmit() {
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

      // 提交前处理
      if (submitBefore && typeof submitBefore === 'function') {
        await submitBefore();
      }

      // 提交数据
      setModalProps({ confirmLoading: true });
      const res = await save(values);
      closeModal();
      // createMessage.success(`操作成功`);
      submitAfter?.(res); // 调用成功回调替代emit
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  async function save(values: Recordable) {
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
    if (saveApi) {
      return await saveApi(submitData);
    }
    if (unref(isUpdate)) {
      return await editApi?.(submitData);
    } else {
      return await addApi?.(submitData);
    }
  }

  return {
    registerForm,
    resetFields,
    setFieldsValue,
    validate,
    registerModal,
    handleSubmit,
    isUpdate,
    isDisabled,
    getTitle,
    getFieldsValue,
    formDataRef,
    closeModal,
    setModalProps,
  };
}
