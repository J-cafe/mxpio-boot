import {
  createTreeSelectEditRender,
  createFormItemRender,
  createCellRender,
  createFilterRender,
  createFloatingFilterRender,
} from '@mxpio/components';
import type { VxeGlobalRendererHandles, VxeGlobalRendererOptions } from '@mxpio/types';
import XEUtils from 'xe-utils';

const selectDeptRendererOptions: VxeGlobalRendererOptions = {
  renderTableEdit: createTreeSelectEditRender(),
  renderTableCell: createCellRender(
    (
      renderOpts: VxeGlobalRendererHandles.RenderOptions,
      params:
        | VxeGlobalRendererHandles.RenderTableCellParams
        | VxeGlobalRendererHandles.ExportMethodParams,
    ) => {
      const { row, column } = params;
      const textMap = XEUtils.get(row, 'textMap');
      return textMap ? textMap[`${column.field}$DICT_TEXT_`] : row[column.field];
    },
  ),
  renderFormItemContent: createFormItemRender({}, (_, params: Recordable) => {
    return {
      params: params,
    };
  }),
  renderTableFilter: createFilterRender(),
  renderTableFloatingFilter: createFloatingFilterRender(),
};

export default selectDeptRendererOptions;
