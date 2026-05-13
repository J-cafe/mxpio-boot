import {
  createEditRender,
  createFilterRender,
  createFormItemRender,
  createDefaultFilterRender,
  createDefaultRender,
} from './common';

export default {
  autofocus: 'input.ant-input-number-input',
  renderTableDefault: createDefaultRender(),
  renderTableEdit: createEditRender(),
  renderTableFilter: createFilterRender(),
  tableFilterDefaultMethod: createDefaultFilterRender(),
  renderFormItemContent: createFormItemRender(),
};
