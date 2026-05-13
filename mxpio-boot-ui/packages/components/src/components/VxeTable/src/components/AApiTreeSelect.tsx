import {
  createTreeSelectEditRender,
  createFormItemRender,
  createSelectCellRender,
  createFilterRender,
  createFloatingFilterRender,
} from './common';

export default {
  renderTableEdit: createTreeSelectEditRender(
    {
      class: '!w-full',
    },
    (_, params) => {
      return {
        params: params, // 传递参数 为后续filter方法使用
      };
    },
  ),
  renderTableCell: createSelectCellRender(),
  renderFormItemContent: createFormItemRender({}, (_, params) => {
    return {
      params: params, // 传递参数 为后续filter方法使用
    };
  }),
  renderTableFilter: createFilterRender(),
  renderTableFloatingFilter: createFloatingFilterRender(),
};

// import TreeSelectRendererOptions from './TreeSelectRendererOptions';

// export default TreeSelectRendererOptions;
// import XEUtils from 'xe-utils';
// import { createDefaultRender, createEditRender, createFormItemRender } from './common';

// export default {
//   renderTableDefault: createDefaultRender({}, (_, params) => {
//     return {
//       params: XEUtils.get(params, 'row'),
//     };
//   }),
//   renderTableEdit: createEditRender({}, (_, params) => {
//     return {
//       params: XEUtils.get(params, 'row'),
//     };
//   }),
//   renderFormItemContent: createFormItemRender({}, (_, params) => {
//     return {
//       params: XEUtils.get(params, 'row'),
//     };
//   }),
// };
