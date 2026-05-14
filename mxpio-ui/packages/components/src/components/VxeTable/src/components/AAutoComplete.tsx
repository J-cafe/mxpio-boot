import {
  createEditRender,
  createDefaultRender,
  createFilterRender,
  createDefaultFilterRender,
  createFormItemRender,
} from './common';

export default {
  autofocus: 'input.ant-input',
  renderTableDefault: createDefaultRender(),
  renderTableEdit: createEditRender({ class: '!w-full' }),
  renderTableFilter: createFilterRender(),
  tableFilterDefaultMethod: createDefaultFilterRender(),
  renderFormItemContent: createFormItemRender(),
};
