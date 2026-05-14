import {
  createSelectEditRender,
  createFormItemRender,
  createSelectCellRender,
  createFilterRender,
  createFloatingFilterRender,
} from './common';

export default {
  renderTableEdit: createSelectEditRender(
    {
      class: '!w-full',
      modelEvent: 'change',
    },
    (_, params) => {
      return {
        params: params,
      };
    },
  ),
  renderFormItemContent: createFormItemRender(
    {
      modelEvent: 'change', // 修复自定义组件在vxe-table中使用时, 不能更新 value 的问题
    },
    (_, params) => {
      return {
        params: params,
      };
    },
  ),
  renderTableCell: createSelectCellRender(),
  renderTableFilter: createFilterRender({
    modelEvent: 'change',
  }),
  renderTableFloatingFilter: createFloatingFilterRender({
    modelEvent: 'change',
  }),
};
