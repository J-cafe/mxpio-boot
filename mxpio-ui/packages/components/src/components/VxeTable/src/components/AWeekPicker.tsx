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
    return ['YYYY-WW周'];
  }),
  renderFormItemContent: createFormItemRender(),
  tableExportMethod: createExportMethod(getDatePickerCellValue, () => {
    return ['YYYY-WW周'];
  }),
  renderTableFilter: createFilterRender({
    getPopupContainer: getPopupContainer,
  }),
  renderTableFloatingFilter: createFloatingFilterRender(),
};
