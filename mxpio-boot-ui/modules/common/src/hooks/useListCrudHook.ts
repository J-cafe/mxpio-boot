import type {
  BasicVxeTableProps,
  VxeGridInstance,
  ModalMethods,
  VxeFormItemProps,
  VxeGridPropTypes,
} from '@mxpio/components';
import { useModal } from '@mxpio/components';
import { useMessage, usePermission, useProfile } from '@mxpio/hooks';
import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
import { onMounted, reactive, ref, Ref, computed, ComputedRef, isRef } from 'vue';
// 新增必要的类型导入
import type { Recordable, VxeToolbarPropTypes, VxeToolbarProps } from '@mxpio/types';
// 定义返回类型接口
interface UseListCrudReturn {
  auth: {
    add: string;
    edit: string;
    delete: string;
    import: string;
  };
  tableRef: Ref<VxeGridInstance | undefined>;
  gridOptions: ComputedRef<BasicVxeTableProps>;
  registerModal: (instance: ModalMethods, uuid: number) => void;
  openModal: <T = any>(props?: boolean, data?: T, openOnSet?: boolean) => void;
  handleAdd: () => void;
  handleEdit: (record: Recordable) => void;
  handleDetail: (record: Recordable) => void;
  handleDelete: (record: Recordable) => void;
  handleBatchDel: () => void;
  handleSuccess: () => void;
  checkboxChange: () => void;
  filterChangeEvent: () => void;
  configData: Ref<Recordable>;
}

// 定义参数类型接口
interface UseListCrudOptions {
  componentName: string;
  module?: string; // 模块字段
  columns: VxeGridPropTypes.Columns; // 列配置字段,放在外部方便定义
  searchFormSchema?: VxeFormItemProps[]; // 搜索表单字段,放在外部方便定义
  pageApi?: (params: Recordable) => Promise<any>;
  deleteApi?: (ids: string) => Promise<any>;
  configApi?: () => Promise<any>;
  authConfig?: {
    add?: string;
    edit?: string;
    delete?: string;
    import?: string;
    export?: string;
  };
  vxeGridOptions?: Ref<BasicVxeTableProps> | BasicVxeTableProps;
  filters?: Recordable | (() => Recordable); // 过滤器字段
  importCode?: Ref<string> | string; // 导入模版编码字段
  addBefore?: () => Recordable | Promise<Recordable>;
  editBefore?: (params: Recordable) => Recordable | Promise<any>;
  deleteBefore?: (params: Recordable | Recordable[]) => Recordable | Promise<any>;
  importBefore?: (params: Recordable) => Recordable | Promise<any>;
  exportBefore?: (params: Recordable) => Recordable | Promise<any>;
  deleteAfter?: (row: any, res: any) => void;
  loadBefore?: (params: {
    page?: VxeTableQueryParams['page'];
    form?: VxeTableQueryParams['form'];
    sorts?: VxeTableQueryParams['sorts'];
    filters?: Recordable;
    tableFilters?: VxeTableQueryParams['tableFilters'];
    other?: VxeTableQueryParams['other'];
    callback?: VxeTableQueryParams['callback'];
  }) => Recordable;
  loadAfter?: (params: Recordable) => void;
  // 新增：追加自定义按钮配置
  appendButtons?: VxeToolbarPropTypes.Buttons;
}

export function useListCrudHook(options: UseListCrudOptions): UseListCrudReturn {
  const {
    componentName,
    columns,
    searchFormSchema,
    pageApi,
    deleteApi,
    configApi,
    authConfig,
    vxeGridOptions = ref<BasicVxeTableProps>({}),
    module = 'sys',
    filters = {},
    addBefore,
    editBefore,
    deleteBefore,
    deleteAfter,
    importCode,
    loadBefore,
    loadAfter,
    appendButtons = [],
  } = options;

  // 权限控制
  const auth = reactive({
    add: authConfig?.add || `${module}:${componentName}:add`,
    edit: authConfig?.edit || `${module}:${componentName}:edit`,
    delete: authConfig?.delete || `${module}:${componentName}:delete`,
    import: authConfig?.import || `${module}:${componentName}:import`,
    export: authConfig?.export || `${module}:${componentName}:export`,
  });

  // 状态管理
  const selectedRowCount = ref(0);
  const importCodeDefault = ref('');
  const tableRef = ref<VxeGridInstance>();
  const configData = ref({});
  // 模态框
  const [registerModal, { openModal }] = useModal();

  // 工具函数
  const { restoreStore, updateStore } = useProfile();
  const { hasPermission } = usePermission();
  const { createMessage, createConfirm } = useMessage();

  const rowConfigDefault = {
    keyField: 'id',
  };

  const addBtn = {
    content: '新增',
    buttonRender: {
      name: 'AButton',
      props: {
        type: 'primary',
        preIcon: 'mdi:page-next-outline',
      },
      attrs: {
        class: 'ml-2',
      },
      events: {
        click: handleAdd,
      },
    },
    visible: hasPermission(auth.add),
  };

  const delBtn = {
    content: '删除',
    code: 'delete',
    buttonRender: {
      name: 'AButton',
      props: {
        type: 'primary',
        danger: true,
        preIcon: 'mdi:delete-forever',
      },
      attrs: {
        class: 'ml-2',
      },
      events: {
        click: handleBatchDel,
      },
    },
    visible: hasPermission(auth.delete) && selectedRowCount.value > 0,
  };

  const exportTool = {
    toolRender: {
      name: 'ExportButton',
      attrs: {
        class: 'mr-2',
      },
      props: {
        export: auth.export,
      },
    },
  };

  const importTool = computed(() => {
    const importCode_ = isRef(importCode) ? importCode.value : importCode;
    return {
      toolRender: {
        name: 'ImportButton',
        props: { importCode: importCode_ || importCodeDefault.value },
        // props: () => {
        //   return { importCode: importCode_ || importCodeDefault.value };
        // },
      },
      visible: hasPermission(auth.import),
    };
  });

  const toolbarConfigDefault: VxeToolbarProps = {
    buttons: [],
    tools: [],
    // refreshOptions: {
    //   queryMethod: () => {
    //     tableRef.value?.commitProxy('query');
    //   },
    // },
  };

  const formConfigDefault = {
    enabled: searchFormSchema ? true : false,
    items: searchFormSchema,
  };

  const customConfigDefault = {
    storage: {
      visible: true,
      resizable: true,
      sort: true,
      fixed: true,
    },
    restoreStore: restoreStore,
    updateStore: updateStore,
  };

  const proxyConfigDefault = {
    ajax: {
      query: async ({ page, form, sorts, filters }) => {
        return loadData({ page, form, sorts, tableFilters: filters });
      },
    },
  };

  const pagerConfigDefault: BasicVxeTableProps['pagerConfig'] = {
    pageSizes: [10, 20, 100, 200],
    pageSize: 10,
    autoHidden: false,
    size: 'small',
  };

  const sortConfigDefault: BasicVxeTableProps['sortConfig'] = {
    trigger: 'cell',
    remote: true,
    defaultSort: {
      field: 'createTime',
      order: 'desc',
    },
  };

  const filterConfigDefault: BasicVxeTableProps['filterConfig'] = {
    remote: true,
  };

  const floatingFilterConfigDefault: BasicVxeTableProps['floatingFilterConfig'] = {
    enabled: false,
  };

  const rawToolbarConfig = computed<VxeToolbarProps>(() => {
    const { toolbarConfig = {} } = isRef(vxeGridOptions) ? vxeGridOptions.value : vxeGridOptions;
    const tools: VxeToolbarPropTypes.Tools = [];
    if (!toolbarConfig || toolbarConfig?.export !== false) {
      tools.push(exportTool);
    }
    if (!toolbarConfig || toolbarConfig?.import !== false) {
      tools.push(importTool.value);
    }
    delBtn.visible = hasPermission(auth.delete) && selectedRowCount.value > 0;
    return {
      ...toolbarConfigDefault,
      tools: tools,
      buttons: toolbarConfig?.buttons ? toolbarConfig?.buttons : [addBtn, ...appendButtons, delBtn],
      ...toolbarConfig,
    };
  });

  const rawRowConfig = computed<BasicVxeTableProps['rowConfig']>(() => {
    const { rowConfig = {} } = isRef(vxeGridOptions) ? vxeGridOptions.value : vxeGridOptions;
    return {
      ...rowConfigDefault,
      ...rowConfig,
    };
  });

  const rawFormConfig = computed<BasicVxeTableProps['formConfig']>(() => {
    const { formConfig = {} } = isRef(vxeGridOptions) ? vxeGridOptions.value : vxeGridOptions;
    return {
      ...formConfigDefault,
      ...formConfig,
    };
  });

  const rawCustomConfig = computed<BasicVxeTableProps['customConfig']>(() => {
    const { customConfig = {} } = isRef(vxeGridOptions) ? vxeGridOptions.value : vxeGridOptions;
    return {
      ...customConfigDefault,
      ...customConfig,
    };
  });

  const rawProxyConfig = computed<BasicVxeTableProps['proxyConfig']>(() => {
    const { proxyConfig = {} } = isRef(vxeGridOptions) ? vxeGridOptions.value : vxeGridOptions;
    return {
      ...proxyConfigDefault,
      ...proxyConfig,
    };
  });

  const rawPagerConfig = computed<BasicVxeTableProps['pagerConfig']>(() => {
    const { pagerConfig = {} } = isRef(vxeGridOptions) ? vxeGridOptions.value : vxeGridOptions;
    return {
      ...pagerConfigDefault,
      ...pagerConfig,
    };
  });

  const rawSortConfig = computed<BasicVxeTableProps['sortConfig']>(() => {
    const { sortConfig = {} } = isRef(vxeGridOptions) ? vxeGridOptions.value : vxeGridOptions;
    return {
      ...sortConfigDefault,
      ...sortConfig,
    };
  });

  const rawFilterConfig = computed<BasicVxeTableProps['filterConfig']>(() => {
    const { filterConfig = {} } = isRef(vxeGridOptions) ? vxeGridOptions.value : vxeGridOptions;
    return {
      ...filterConfigDefault,
      ...filterConfig,
    };
  });

  const rawFloatingFilterConfig = computed<BasicVxeTableProps['floatingFilterConfig']>(() => {
    const { floatingFilterConfig = {} } = isRef(vxeGridOptions)
      ? vxeGridOptions.value
      : vxeGridOptions;
    return {
      ...floatingFilterConfigDefault,
      ...floatingFilterConfig,
    };
  });

  const gridOptions = computed<BasicVxeTableProps>(() => {
    const vxeGridOptions_ = isRef(vxeGridOptions) ? vxeGridOptions.value : vxeGridOptions;
    return {
      id: `${componentName}Table`,
      height: 720,
      ...vxeGridOptions_,
      columns: columns,
      rowConfig: rawRowConfig.value,
      toolbarConfig: rawToolbarConfig.value,
      formConfig: rawFormConfig.value,
      customConfig: rawCustomConfig.value,
      proxyConfig: rawProxyConfig.value,
      pagerConfig: rawPagerConfig.value,
      sortConfig: rawSortConfig.value,
      ref: tableRef,
      filterConfig: rawFilterConfig.value,
      floatingFilterConfig: rawFloatingFilterConfig.value,
    };
  });

  // 加载数据
  async function loadData({ page, form, sorts, tableFilters }: VxeTableQueryParams) {
    // 当无排序参数时，使用默认排序
    const _sorts: VxeGridPropTypes.ProxyAjaxQuerySortCheckedParams[] | undefined =
      sorts?.length === 0
        ? ([
            ...(Array.isArray(rawSortConfig?.value?.defaultSort)
              ? rawSortConfig.value.defaultSort
              : [rawSortConfig.value?.defaultSort]),
          ] as VxeGridPropTypes.ProxyAjaxQuerySortCheckedParams[])
        : sorts;
    // 解析 filters：如果是函数则执行，否则直接使用对象
    const resolvedFilters = typeof filters === 'function' ? filters() : filters;
    const params = loadBefore
      ? loadBefore({
          page,
          form,
          sorts: _sorts,
          filters: resolvedFilters,
          tableFilters,
        })
      : getVxeTableQueryParams({
          page,
          form,
          sorts: _sorts,
          filters: resolvedFilters,
          tableFilters,
        });
    const res = await pageApi?.(params);
    if (!res.first && res.last && res.content.length === 0) {
      setTimeout(() => {
        tableRef.value?.commitProxy('query');
      }, 1000);
    }
    if (loadAfter && typeof loadAfter === 'function') {
      loadAfter(res);
    }
    return res;
  }

  // 打开创建模态框
  async function handleAdd() {
    try {
      let extendData;
      if (addBefore && typeof addBefore === 'function') {
        extendData = await addBefore();
      }
      openModal(true, {
        isUpdate: false,
        extendData: extendData,
      });
    } catch (error) {
      console.error('Failed to fetch config:', error);
    }
  }

  // 打开编辑模态框
  async function handleEdit(record: Recordable) {
    try {
      let extendData;
      if (editBefore && typeof editBefore === 'function') {
        extendData = await editBefore(record);
      }
      openModal(true, {
        record,
        isUpdate: true,
        extendData,
      });
    } catch (error) {
      console.error('Failed to fetch config:', error);
    }
  }

  async function handleDetail(record: Recordable) {
    try {
      openModal(true, {
        record,
        isUpdate: true,
        disabled: true,
      });
    } catch (error) {
      console.error('Failed to fetch config:', error);
    }
  }

  // 单行删除
  async function handleDelete(record: Recordable) {
    try {
      if (deleteBefore && typeof deleteBefore === 'function') {
        await deleteBefore(record);
      }
      const keyField = gridOptions.value.rowConfig?.keyField || rowConfigDefault.keyField;
      const id = record[keyField];
      const res = await deleteApi?.(id).then(() => {
        tableRef.value?.commitProxy('query');
      });
      // createMessage.success('删除成功');
      if (deleteAfter && typeof deleteAfter === 'function') {
        deleteAfter(record, res);
      }
      selectedRowCount.value = 0;
    } catch (error) {
      console.error('Failed to fetch config:', error);
    }
  }

  // 批量删除
  async function handleBatchDel() {
    try {
      const selectedRows: Recordable[] = tableRef.value?.getCheckboxRecords() || [];
      if (selectedRows.length === 0) {
        createMessage.error('请选择要删除的数据');
        return;
      }
      if (deleteBefore && typeof deleteBefore === 'function') {
        await deleteBefore(selectedRows);
      }
      createConfirm({
        title: '是否确认删除',
        iconType: 'warning',
        centered: false,
        onOk: async () => {
          const keyField = gridOptions.value.rowConfig?.keyField || rowConfigDefault.keyField;
          const selectedRowKeys = selectedRows.map((item) => item[keyField]);
          const res = await deleteApi?.(selectedRowKeys.join()).then(() => {
            tableRef.value?.commitProxy('query');
          });
          // createMessage.success('删除成功');
          if (deleteAfter && typeof deleteAfter === 'function') {
            deleteAfter(selectedRows, res);
          }
          selectedRowCount.value = 0;
        },
      });
    } catch (error) {
      console.error('Failed to fetch config:', error);
    }
  }

  // 复选框变化事件
  function checkboxChange() {
    const selectedRows: Recordable[] = tableRef.value?.getCheckboxRecords() || [];
    selectedRowCount.value = selectedRows.length;
  }

  // 操作成功后的回调
  async function handleSuccess() {
    tableRef.value?.commitProxy('query');
  }

  async function filterChangeEvent() {
    try {
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  // watch(selectedRowCount, () => {
  //   const deleteButton = gridOptions.value?.toolbarConfig?.buttons?.find(
  //     (btn) => btn.code === 'delete',
  //   );
  //   if (deleteButton) {
  //     deleteButton.visible = hasPermission(auth.delete) && selectedRowCount.value > 0;
  //   }
  // });

  // 初始化加载配置
  onMounted(async () => {
    if (configApi) {
      try {
        const res = await configApi();
        importCodeDefault.value = res?.importTemplate;
        configData.value = res;
      } catch (error) {
        console.error('Failed to fetch config:', error);
      }
    }
  });

  return {
    auth,
    configData,
    tableRef,
    gridOptions,
    registerModal,
    handleAdd,
    handleEdit,
    handleDetail,
    handleDelete,
    handleBatchDel,
    handleSuccess,
    checkboxChange,
    filterChangeEvent,
    openModal,
  };
}
