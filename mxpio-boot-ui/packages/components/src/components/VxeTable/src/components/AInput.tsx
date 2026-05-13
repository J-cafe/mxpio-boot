import {
  createEditRender,
  createDefaultRender,
  createFilterRender,
  createDefaultFilterRender,
  createFormItemRender,
  createFloatingFilterRender,
} from './common';

export default {
  autofocus: 'input.ant-input',
  renderTableDefault: createDefaultRender(),
  renderTableEdit: createEditRender(),
  renderTableFilter: createFilterRender(),
  tableFilterDefaultMethod: createDefaultFilterRender(),
  renderFormItemContent: createFormItemRender(),
  renderTableFloatingFilter: createFloatingFilterRender(),
};
