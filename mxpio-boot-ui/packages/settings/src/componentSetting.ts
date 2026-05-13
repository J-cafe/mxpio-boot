import type { ComponentSettingOptions } from './types';

export const componentSettingDef: ComponentSettingOptions = {
  // basic-table setting
  table: {
    // Form interface request general configuration
    // support xxx.xxx.xxx
    fetchSetting: {
      // The field name of the current page passed to the background
      pageField: 'page',
      // The number field name of each page displayed in the background
      sizeField: 'pageSize',
      // Field name of the form data returned by the interface
      listField: 'items',
      // Total number of tables returned by the interface field name
      totalField: 'total',
    },
    // Number of pages that can be selected
    pageSizeOptions: ['10', '50', '80', '100'],
    // Default display quantity on one page
    defaultPageSize: 10,
    // Default Size
    defaultSize: 'middle',
    // Custom general sort function
    defaultSortFn: (sortInfo: any) => {
      const { field, order } = sortInfo;
      if (field && order) {
        return {
          // The sort field passed to the backend you
          field,
          // Sorting method passed to the background asc/desc
          order,
        };
      } else {
        return {};
      }
    },
    // Custom general filter function
    defaultFilterFn: (data: any) => {
      return data;
    },
  },
  vxeTable: {
    table: {
      border: true,
      stripe: true,
      showHeaderOverflow: true,
      columnConfig: {
        resizable: true,
      },
      rowConfig: {
        isCurrent: true,
        isHover: true,
      },
      emptyRender: {
        name: 'AEmpty',
      },
      printConfig: {},
      exportConfig: {},
      sortConfig: {
        remote: true,
        trigger: 'cell',
      },
      importConfig: {
        remote: true,
        types: ['xls', 'xlsx'],
      },
      customConfig: {
        storage: true,
      },
      showOverflow: true,
    },
    grid: {
      toolbarConfig: {
        perfect: true,
        enabled: true,
        export: false,
        import: false,
        zoom: true,
        refresh: true,
        custom: true,
        circle: true,
      },
      pagerConfig: {
        pageSizes: [10, 20, 100, 200],
        pageSize: 10,
        autoHidden: false,
        size: 'small',
      },
      proxyConfig: {
        form: true,
        sort: true, // 启用排序代理
        response: {
          result: 'content',
          total: 'totalElements',
          // list: 'result',
        },
      },
      zoomConfig: {},
    },
  },
  // scrollbar setting
  scrollbar: {
    // Whether to use native scroll bar
    // After opening, the menu, modal, drawer will change the pop-up scroll bar to native
    native: false,
  },
};
