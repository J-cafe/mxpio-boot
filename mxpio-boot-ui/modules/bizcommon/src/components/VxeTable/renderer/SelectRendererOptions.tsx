import {
  createSelectEditRender,
  createFormItemRender,
  createSelectCellRender,
  createFilterRender,
  createFloatingFilterRender,
} from '@mxpio/components';
import type { VxeGlobalRendererOptions } from '@mxpio/types';

const selectRendererOptions: VxeGlobalRendererOptions = {
  renderTableEdit: createSelectEditRender(
    {
      class: '!w-full',
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
    modelEvent: 'change', // 修复自定义组件在vxe-table中使用时, 不能更新 value 的问题
  }),
  renderTableFloatingFilter: createFloatingFilterRender({
    modelEvent: 'change', // 修复自定义组件在vxe-table中使用时, 不能更新 value 的问题
  }),
};

export default selectRendererOptions;
