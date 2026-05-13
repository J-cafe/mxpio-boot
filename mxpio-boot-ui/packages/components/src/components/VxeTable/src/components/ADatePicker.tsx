import { VxeGlobalRendererHandles } from 'vxe-table';
import XEUtils from 'xe-utils';
import dayjs from 'dayjs';
import isoWeek from 'dayjs/plugin/isoWeek';
import {
  createCellRender,
  createEditRender,
  createExportMethod,
  createFormItemRender,
  createFilterRender,
  createFloatingFilterRender,
} from './common';
import { getPopupContainer } from '@mxpio/utils';

dayjs.extend(isoWeek);

export function getDatePickerCellValue(
  renderOpts: VxeGlobalRendererHandles.RenderOptions,
  params: VxeGlobalRendererHandles.RenderCellParams | VxeGlobalRendererHandles.ExportMethodParams,
  defaultFormat: string,
) {
  const { props = {} } = renderOpts;
  const { row, column } = params;
  let cellValue = XEUtils.get(row, column.field as string);
  if (cellValue) {
    // cellValue = cellValue.format(props.format || defaultFormat);
    cellValue = dayjs(cellValue).format(props.format || defaultFormat);
  }
  return cellValue;
}

export default {
  renderTableEdit: createEditRender(),
  renderTableCell: createCellRender(getDatePickerCellValue, () => {
    return ['YYYY-MM-DD'];
  }),
  renderFormItemContent: createFormItemRender(),
  tableExportMethod: createExportMethod(getDatePickerCellValue, () => {
    return ['YYYY-MM-DD'];
  }),
  renderTableFilter: createFilterRender({
    getPopupContainer: getPopupContainer,
  }),
  renderTableFloatingFilter: createFloatingFilterRender(),
};
