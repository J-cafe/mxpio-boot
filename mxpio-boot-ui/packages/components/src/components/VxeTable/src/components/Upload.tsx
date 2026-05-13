import XEUtils from 'xe-utils';
import { createEditRender } from './common';

export default {
  renderTableEdit: createEditRender(
    {
      class: '!w-full',
    },
    (_, params) => {
      return {
        params: XEUtils.get(params, 'row'),
      };
    },
  ),
  renderTableCell: createEditRender(
    {
      class: '!w-full',
    },
    (renderOpts, params) => {
      const { $grid } = params;
      const editConfig = $grid?.props.editConfig;
      let disabled = false;
      // 修复表格禁用编辑时，上传组件未禁用问题
      if (!editConfig || !editConfig.enabled) {
        disabled = true;
      }
      return {
        params: XEUtils.get(params, 'row'),
        disabled: disabled,
      };
    },
  ),
};
