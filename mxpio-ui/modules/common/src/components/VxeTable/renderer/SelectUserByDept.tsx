import XEUtils from 'xe-utils';
import { h } from 'vue';
import {
  getComponent,
  createProps,
  createEvents,
  createFormItemRender,
  createCellRender,
  createFilterRender,
  createFloatingFilterRender,
} from '@mxpio/components';
import type { VxeGlobalRendererHandles, VxeGlobalRendererOptions } from '@mxpio/types';

function createEditRender(
  defaultProps?: { [key: string]: any },
  callBack?: (
    renderOpts: VxeGlobalRendererHandles.RenderTableEditOptions,
    params: VxeGlobalRendererHandles.RenderTableEditParams,
  ) => Record<string, any>,
) {
  return function (
    renderOpts: VxeGlobalRendererHandles.RenderTableEditOptions,
    params: VxeGlobalRendererHandles.RenderTableEditParams,
  ) {
    const { row, column } = params;
    const { name, attrs } = renderOpts;
    const cellValue = XEUtils.get(row, column.field as string);
    const args = (callBack && callBack(renderOpts, params)) ?? {};
    const Component = getComponent(name);
    return [
      h(Component, {
        ...attrs,
        ...createProps(renderOpts, cellValue, defaultProps, params),
        ...args,
        ...createEvents(
          renderOpts,
          params,
          (value: any) => {
            XEUtils.set(row, column.field as string, value);
          },
          // (value: any, users: any) => {
          //   const nicknames = users?.length > 0 ? users.map((user: any) => user.nickname) : [];
          //   const textMap = XEUtils.get(row, 'textMap');
          //   XEUtils.set(row, 'textMap', {
          //     ...textMap,
          //     [`${column.field as string}$DICT_TEXT_`]: nicknames.join(', '),
          //   });
          //   $table.updateStatus(params);
          // },
        ),
      }),
    ];
  };
}

const selectUserByDeptRendererOptions: VxeGlobalRendererOptions = {
  renderTableEdit: createEditRender(
    {
      class: '!w-full',
    },
    (_, params) => {
      return {
        params: XEUtils.get(params, 'row'),
      };
    },
  ),
  renderTableCell: createCellRender(
    (
      renderOpts: VxeGlobalRendererHandles.RenderOptions,
      params:
        | VxeGlobalRendererHandles.RenderTableCellParams
        | VxeGlobalRendererHandles.ExportMethodParams,
    ) => {
      const { row, column } = params;
      const textMap = XEUtils.get(row, 'textMap');
      return textMap && textMap[`${column.field}$DICT_TEXT_`]
        ? textMap[`${column.field}$DICT_TEXT_`]
        : row[column.field];
    },
  ),
  renderFormItemContent: createFormItemRender({}, (_, params) => {
    return {
      params: params,
    };
  }),
  renderTableFilter: createFilterRender(),
  renderTableFloatingFilter: createFloatingFilterRender(),
};

export default selectUserByDeptRendererOptions;
