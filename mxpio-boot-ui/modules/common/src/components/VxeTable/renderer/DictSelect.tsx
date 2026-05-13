import {
  createSelectEditRender,
  createFormItemRender,
  createCellRender,
  createFilterRender,
  createFloatingFilterRender,
} from '@mxpio/components';
import { dictCache } from '../../../utils/dictCache';
import type { VxeGlobalRendererOptions } from '@mxpio/types';
import XEUtils from 'xe-utils';

const dictSelectRendererOptions: VxeGlobalRendererOptions = {
  renderTableEdit: createSelectEditRender(
    {
      class: '!w-full',
      modelEvent: 'change',
    },
    (renderOpts) => {
      const dictCode = (renderOpts as any).dictCode;
      // 如果有 dictCode，预加载字典
      if (dictCode) {
        dictCache.get(dictCode).catch(console.warn);
      }
      return {};
    },
  ),
  renderFormItemContent: createFormItemRender(
    {
      modelEvent: 'change', // 修复自定义组件在vxe-table中使用时, 不能更新 value 的问题
    },
    (renderOpts) => {
      const dictCode = (renderOpts as any).props?.dictCode;
      // 如果有 dictCode，预加载字典
      if (dictCode) {
        dictCache.get(dictCode).catch(console.warn);
      }
      return {};
    },
  ),
  renderTableCell: createCellRender(
    (renderOpts, params) => {
      const { row, column } = params;
      const cellValue = row[column.field];
      const { props = {} } = renderOpts;

      const dictCode = props?.dictCode;
      const mode = props?.mode;
      // 1. 优先使用 textMap 中的字典文本（向后兼容）
      const textMap = XEUtils.get(row, 'textMap');
      if (textMap && textMap[`${column.field}$DICT_TEXT_`]) {
        return textMap[`${column.field}$DICT_TEXT_`];
      }
      if (dictCode) {
        const label =
          mode === 'multiple'
            ? dictCache.getLabelsSync(dictCode, cellValue ? cellValue.split(',') : [])
            : dictCache.getLabelSync(dictCode, cellValue);
        return label;
      }
    },
    (renderOpts) => {
      const dictCode = (renderOpts as any).props?.dictCode;
      // 如果有 dictCode，预加载字典
      if (dictCode) {
        dictCache.get(dictCode).catch(console.warn);
      }
      return [];
    },
  ),
  renderTableFilter: createFilterRender({
    modelEvent: 'change',
  }),
  renderTableFloatingFilter: createFloatingFilterRender({
    modelEvent: 'change',
  }),
  // 传入 dictCache 实例，支持自动加载和缓存
  // renderTableCell: createSelectCellRender(dictCache),
};

export default dictSelectRendererOptions;
