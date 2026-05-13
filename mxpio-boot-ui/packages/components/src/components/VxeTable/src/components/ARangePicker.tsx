import { VxeColumnPropTypes, VxeGlobalRendererHandles } from 'vxe-table';
import XEUtils from 'xe-utils';
import {
  createCellRender,
  createEditRender,
  createExportMethod,
  getComponent,
  createProps,
  createEvents,
  createFilterRender,
  createFloatingFilterRender,
} from './common';
import dayjs from 'dayjs';
import type { FormItemContentRenderParams } from 'vxe-pc-ui';
import { h } from 'vue';
import { getPopupContainer } from '@mxpio/utils';

// 处理日期范围格式化（附加时分秒）
function createFormItemRender(
  defaultProps?: { [key: string]: any },
  callBack?: (
    renderOpts: VxeGlobalRendererHandles.RenderFormItemTitleOptions,
    params: FormItemContentRenderParams,
  ) => Record<string, any>,
) {
  return function (
    renderOpts: VxeGlobalRendererHandles.RenderFormItemTitleOptions,
    params: FormItemContentRenderParams,
  ) {
    const args = (callBack && callBack(renderOpts, params)) ?? {};
    const { data, field, $form } = params;
    const { name } = renderOpts;
    const { attrs } = renderOpts;
    const itemValue = XEUtils.get(data, field);

    const Component = getComponent(name);
    return [
      h(Component, {
        ...attrs,
        ...createProps(renderOpts, itemValue, defaultProps, params),
        ...args,
        ...createEvents(
          renderOpts,
          params,
          (value: any) => {
            const { props = {} } = renderOpts;
            // 处理 model 值双向绑定
            if (props?.autoFormat && value && value.length === 2) {
              const [start, end] = value;
              // 开始时间附加 00:00:00
              const formattedStart = start ? dayjs(start).format('YYYY-MM-DD 00:00:00') : null;
              // 结束时间附加 23:59:59
              const formattedEnd = end ? dayjs(end).format('YYYY-MM-DD 23:59:59') : null;
              const { data } = params;
              XEUtils.set(data, field, [formattedStart, formattedEnd]);
            } else {
              XEUtils.set(data, field, value);
            }
          },
          () => {
            // 处理 change 事件相关逻辑
            $form.updateStatus({
              ...params,
              field: field,
            });
          },
        ),
      }),
    ];
  };
}
function getRangePickerCellValue(
  renderOpts: VxeColumnPropTypes.EditRender,
  params: VxeGlobalRendererHandles.RenderCellParams | VxeGlobalRendererHandles.ExportMethodParams,
) {
  const { props = {} } = renderOpts;
  const { row, column } = params;
  let cellValue = XEUtils.get(row, column.field as string);
  if (cellValue) {
    cellValue = XEUtils.map(cellValue, (date: any) =>
      // date.format(props.format || 'YYYY-MM-DD'),
      dayjs(date).format(props.format || 'YYYY-MM-DD'),
    ).join(' ~ ');
  }
  return cellValue;
}

export default {
  renderTableEdit: createEditRender(),
  renderTableCell: createCellRender(getRangePickerCellValue),
  renderFormItemContent: createFormItemRender(),
  tableExportMethod: createExportMethod(getRangePickerCellValue),
  renderTableFilter: createFilterRender({
    getPopupContainer: getPopupContainer,
  }),
  renderTableFloatingFilter: createFloatingFilterRender(),
};
