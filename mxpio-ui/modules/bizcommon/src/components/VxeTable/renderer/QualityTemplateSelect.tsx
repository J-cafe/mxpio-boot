import { createEditRender, createDefaultRender, createFormItemRender } from '@mxpio/components';
import type { VxeGlobalRendererOptions } from '@mxpio/types';

export default {
  autofocus: 'input.ant-input',
  renderTableDefault: createDefaultRender(),
  renderTableEdit: createEditRender(),
  renderFormItemContent: createFormItemRender(),
} as VxeGlobalRendererOptions;
