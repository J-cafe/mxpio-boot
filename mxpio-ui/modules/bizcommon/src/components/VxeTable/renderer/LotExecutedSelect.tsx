import { createEditRender, createDefaultRender } from '@mxpio/components';
import type { VxeGlobalRendererOptions } from '@mxpio/types';
import XEUtils from 'xe-utils';

export default {
  renderTableDefault: createDefaultRender(),
  renderTableEdit: createEditRender({}, (_, params) => {
    return {
      params: XEUtils.get(params, 'row'),
    };
  }),
} as VxeGlobalRendererOptions;
