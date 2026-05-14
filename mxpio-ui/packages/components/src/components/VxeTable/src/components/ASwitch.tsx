import { h } from 'vue';
import XEUtils from 'xe-utils';
import {
  createCellRender,
  createEditRender,
  createProps,
  createEvents,
  createDefaultFilterRender,
  createFormItemRender,
  getComponent,
} from './common';
import type { VxeGlobalRendererHandles } from 'vxe-pc-ui';

export default {
  renderTableEdit: createEditRender(),
  renderTableCell: createCellRender(
    (
      renderOpts: VxeGlobalRendererHandles.RenderOptions,
      params:
        | VxeGlobalRendererHandles.RenderTableCellParams
        | VxeGlobalRendererHandles.ExportMethodParams,
    ) => {
      const { row, column } = params;
      const { props = {} } = renderOpts;
      const { checkedChildren = '开', unCheckedChildren = '关' } = props;
      return row && row[`${column.field}`] ? checkedChildren : unCheckedChildren;
    },
  ),
  renderTableFilter(renderOpts, params) {
    const { column } = params;
    const { name, attrs } = renderOpts;
    const Component = getComponent(name);

    return [
      h(
        'div',
        {
          class: 'vxe-table--filter-antd-wrapper',
        },
        column.filters.map((option, oIndex) => {
          const optionValue = option.data;
          return h(Component, {
            key: oIndex,
            ...attrs,
            ...createProps(renderOpts, optionValue, {}, params),
            ...createEvents(
              renderOpts,
              params,
              (value: any) => {
                // 处理 model 值双向绑定
                option.data = value;
              },
              () => {
                // 处理 change 事件相关逻辑
                const { $panel } = params;
                $panel.changeOption(null, XEUtils.isBoolean(option.data), option);
              },
            ),
          });
        }),
      ),
    ];
  },
  tableFilterDefaultMethod: createDefaultFilterRender(),
  renderFormItemContent: createFormItemRender(),
};
