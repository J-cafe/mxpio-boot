import { getDatePickerCellValue } from './ADatePicker';
import {
  createEditRender,
  createCellRender,
  createFormItemRender,
  createExportMethod,
  createFilterRender,
  createFloatingFilterRender,
} from './common';
import { getPopupContainer } from '@mxpio/utils';

export default {
  renderTableEdit: createEditRender(),
  renderTableCell: createCellRender(getDatePickerCellValue, () => {
    return ['YYYY'];
  }),
  renderFormItemContent: createFormItemRender(),
  tableExportMethod: createExportMethod(getDatePickerCellValue, () => {
    return ['YYYY'];
  }),
  renderTableFilter: createFilterRender({
    getPopupContainer: getPopupContainer,
  }),
  renderTableFloatingFilter: createFloatingFilterRender(),
};
