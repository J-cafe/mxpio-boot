import { VxeGlobalRendererHandles } from 'vxe-table';
import XEUtils from 'xe-utils';
import {
  createEditRender,
  createCellRender,
  isEmptyValue,
  createFormItemRender,
  createExportMethod,
  createFloatingFilterRender,
  createFilterRender,
} from './common';
import { getPopupContainer } from '@mxpio/utils';

function getTreeSelectCellValue(
  renderOpts: VxeGlobalRendererHandles.RenderOptions,
  params: VxeGlobalRendererHandles.RenderCellParams | VxeGlobalRendererHandles.ExportMethodParams,
) {
  const { props = {} } = renderOpts;
  const { treeData, treeCheckable } = props;
  const { row, column } = params;
  const cellValue = XEUtils.get(row, column.field as string);
  if (!isEmptyValue(cellValue)) {
    return XEUtils.map(treeCheckable ? cellValue : [cellValue], (value) => {
      const matchObj = XEUtils.findTree(treeData, (item: any) => item.value === value, {
        children: 'children',
      });
      return matchObj ? matchObj.item.label : value;
    }).join(', ');
  }
  return cellValue;
}

export default {
  renderTableEdit: createEditRender({
    class: '!w-full',
  }),
  renderTableCell: createCellRender(getTreeSelectCellValue),
  renderFormItemContent: createFormItemRender(),
  tableExportMethod: createExportMethod(getTreeSelectCellValue),
  renderTableFilter: createFilterRender({
    getPopupContainer: getPopupContainer,
  }),
  renderTableFloatingFilter: createFloatingFilterRender(),
};
