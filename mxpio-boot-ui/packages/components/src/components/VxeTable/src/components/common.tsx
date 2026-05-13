import { ComponentOptions, h } from 'vue';
import type { FormItemContentRenderParams, VxeGlobalRendererHandles } from 'vxe-pc-ui';
import XEUtils from 'xe-utils';
import { componentMap } from '../componentMap';
import { ComponentType } from '../componentType';
import { createPlaceholderMessage } from '../helper';
import { isArray } from '@mxpio/utils';
/**
 * @description: 获取组件
 */
export function getComponent(componentName) {
  const Component = componentMap.get(componentName as ComponentType);
  if (!Component) throw `您还没注册此组件 ${componentName}`;
  return Component as ComponentOptions;
}

export function isEmptyValue(cellValue: any) {
  return cellValue === null || cellValue === undefined || cellValue === '';
}

export function formatText(cellValue: any) {
  return '' + (isEmptyValue(cellValue) ? '' : cellValue);
}

export function cellText(cellValue: any): string[] {
  return [formatText(cellValue)];
}

/**
 * @description: 方法名转换
 */
export function getOnName(type: string) {
  return 'on' + type.substring(0, 1).toLocaleUpperCase() + type.substring(1);
}

/**
 * @description: 获取组件传值所接受的属性
 */
export function getModelKey(renderOpts: VxeGlobalRendererHandles.RenderOptions) {
  let prop = 'value';
  switch (renderOpts.name) {
    case 'ASwitch':
      prop = 'checked';
      break;
  }
  return prop;
}

/**
 * @description: 回去双向更新的方法
 */
function getModelEvent(renderOpts: VxeGlobalRendererHandles.RenderOptions) {
  let type = 'update:value';
  switch (renderOpts.name) {
    case 'ASwitch':
      type = 'update:checked';
      break;
  }
  return renderOpts?.modelEvent || type; // 修复自定义组件在表单中使用时, 不能更新 value 的问题
}

/**
 * @description: chang值改变方法
 * @param {}
 * @return {*}
 * @author: *
 */
function getChangeEvent() {
  return 'change';
}

function getClickEvent() {
  return 'click';
}
/**
 * @description: 获取方法
 * @param {}
 * @return {*}
 * @author: *
 */
export function createEvents(
  renderOpts: VxeGlobalRendererHandles.RenderOptions,
  params: VxeGlobalRendererHandles.RenderParams,
  inputFunc?: Function,
  changeFunc?: Function,
  clickFunc?: Function,
) {
  const { events } = renderOpts;
  const modelEvent = getModelEvent(renderOpts);
  const changeEvent = getChangeEvent();
  const clickEvent = getClickEvent();
  const isSameEvent = changeEvent === modelEvent;
  const ons: { [type: string]: Function } = {};

  XEUtils.objectEach(events, (func: Function, key: string) => {
    ons[getOnName(key)] = function (...args: any[]) {
      func(params, ...args);
    };
  });
  if (inputFunc) {
    ons[getOnName(modelEvent)] = function (targetEvnt, ...args: any[]) {
      inputFunc(targetEvnt);
      if (events && events[modelEvent]) {
        events[modelEvent](params, targetEvnt);
      }
      if (isSameEvent && changeFunc) {
        changeFunc(targetEvnt, ...args);
      }
    };
  }
  if (!isSameEvent && changeFunc) {
    ons[getOnName(changeEvent)] = function (...args: any[]) {
      changeFunc(...args);
      if (events && events[changeEvent]) {
        events[changeEvent](params, ...args);
      }
    };
  }
  if (clickFunc) {
    ons[getOnName(clickEvent)] = function (...args: any[]) {
      clickFunc(...args);
      if (events && events[clickEvent]) {
        events[clickEvent](params, ...args);
      }
    };
  }
  return ons;
}

/**
 * @description: 获取属性
 */
export function createProps(
  renderOpts: VxeGlobalRendererHandles.RenderOptions,
  value: any,
  defaultProps?: { [prop: string]: any },
  params?: VxeGlobalRendererHandles.RenderParams,
) {
  const name = renderOpts.name as ComponentType;
  // 新增：处理函数类型的 props
  let renderProps = renderOpts.props;
  if (typeof renderProps === 'function' && params) {
    renderProps = renderProps(params);
  }
  return XEUtils.assign(
    {
      placeholder: createPlaceholderMessage(name),
      allowClear: true,
    },
    defaultProps,
    renderProps,
    {
      [getModelKey(renderOpts)]: value,
    },
  );
}

/**
 * @description: 创建单元格默认显示内容
 */
export function createDefaultRender(
  defaultProps?: { [key: string]: any },
  callBack?: (
    renderOpts: VxeGlobalRendererHandles.RenderDefaultOptions,
    params: VxeGlobalRendererHandles.RenderTableDefaultParams,
  ) => Record<string, any>,
) {
  return function (
    renderOpts: VxeGlobalRendererHandles.RenderDefaultOptions,
    params: VxeGlobalRendererHandles.RenderTableDefaultParams,
  ) {
    const { row, column, $table } = params;
    const { name, attrs } = renderOpts;
    const cellValue = XEUtils.get(row, column.field as string);
    const args = (callBack && callBack(renderOpts, params)) ?? {};
    const Component = getComponent(name);
    return [
      h(Component, {
        ...attrs,
        ...createProps(renderOpts, cellValue, defaultProps),
        ...args,
        ...createEvents(
          renderOpts,
          params,
          (value: any) => XEUtils.set(row, column.field as string, value),
          () => $table.updateStatus(params),
        ),
      }),
    ];
  };
}

/**
 * @description: 创建编辑单元格
 */
export function createEditRender(
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
    const { row, column, $table } = params;
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
          () => $table.updateStatus(params),
        ),
      }),
    ];
  };
}

/**
 *  xys 20250926
 * @description: 创建需要翻译类型的编辑单元格
 */
export function createSelectEditRender(
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
    const { row, column, $table } = params;
    const { name, attrs } = renderOpts;
    const cellValue = XEUtils.get(row, column.field as string);
    const args = (callBack && callBack(renderOpts, params)) ?? {};
    const opts = createProps(renderOpts, cellValue, defaultProps, params);
    const Component = getComponent(name);
    return [
      h(Component, {
        ...attrs,
        ...opts,
        ...args,
        ...createEvents(
          opts as VxeGlobalRendererHandles.RenderFormItemTitleOptions,
          params,
          (value: any) => {
            XEUtils.set(row, column.field as string, value);
          },
          (value: any, option: any) => {
            // 获取字典文本
            let dictLable = '';
            if (value) {
              if (isArray(option)) {
                dictLable = option.map((item) => item.label).join(',');
              } else {
                dictLable = option?.label;
              }
            }
            const textMap = XEUtils.get(row, 'textMap');
            XEUtils.set(row, 'textMap', {
              ...textMap,
              [`${column.field as string}$DICT_TEXT_`]: dictLable,
            });
            $table.updateStatus(params);
          },
        ),
      }),
    ];
  };
}

export function createTreeSelectEditRender(
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
    const { row, column, $table } = params;
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
          (value: any, label: any) => {
            const textMap = XEUtils.get(row, 'textMap');
            XEUtils.set(row, 'textMap', {
              ...textMap,
              [`${column.field as string}$DICT_TEXT_`]: label.join(','),
            });
            $table.updateStatus(params);
          },
        ),
      }),
    ];
  };
}

/**
 * @description: 创建筛选渲染内容
 */
export function createFilterRender(
  defaultProps?: { [key: string]: any },
  callBack?: (
    renderOpts: VxeGlobalRendererHandles.RenderTableFilterOptions,
    params: VxeGlobalRendererHandles.RenderTableFilterParams,
  ) => Record<string, any>,
) {
  return function (
    renderOpts: VxeGlobalRendererHandles.RenderTableFilterOptions,
    params: VxeGlobalRendererHandles.RenderTableFilterParams,
  ) {
    const { column } = params;
    const { name, attrs } = renderOpts;
    const args = (callBack && callBack(renderOpts, params)) ?? {};

    const Component = getComponent(name);
    return [
      h(
        'div',
        {
          class: 'vxe-table--filter-antd-wrapper',
        },
        column.filters.map((option, oIndex) => {
          const optionValue = option.data;

          const opts = createProps(renderOpts, optionValue, defaultProps, params);
          // 使用 option 对象本身缓存防抖函数，避免跨表格冲突
          const cacheKey = '_debounceFn_' + oIndex;
          if (!option[cacheKey]) {
            option[cacheKey] = XEUtils.debounce(() => {
              const { $panel } = params;
              // 需要判断空数组的情况
              $panel &&
                $panel.changeOption(
                  null,
                  Array.isArray(option.data) ? option.data?.length > 0 : !!option.data,
                  option,
                );
            }, 300);
          }
          return h(Component, {
            key: oIndex,
            ...attrs,
            // ...createProps(renderOpts, optionValue, defaultProps, params),
            ...opts,
            ...args,
            ...createEvents(
              opts as VxeGlobalRendererHandles.RenderFormItemTitleOptions,
              params,
              (value: any) => {
                console.log('value', value);
                // 处理 model 值双向绑定
                option.data = value;
              },
              option[cacheKey],
            ),
          });
        }),
      ),
    ];
  };
}

/**
 * @description: 创建筛选渲染内容
 */
export function createFloatingFilterRender(
  defaultProps?: { [key: string]: any },
  callBack?: (
    renderOpts: VxeGlobalRendererHandles.RenderTableFloatingFilterOptions,
    params: VxeGlobalRendererHandles.RenderTableFloatingFilterParams,
  ) => Record<string, any>,
) {
  return function (
    renderOpts: VxeGlobalRendererHandles.RenderTableFloatingFilterOptions,
    params: VxeGlobalRendererHandles.RenderTableFloatingFilterParams,
  ) {
    const { column, $table } = params;
    const { name, attrs } = renderOpts;
    const args = (callBack && callBack(renderOpts, params)) ?? {};

    const Component = getComponent(name);

    return [
      h(
        'div',
        {
          class: 'vxe-table-floating-filter-antd-wrapper',
        },
        column.filters.map((option, oIndex) => {
          const optionValue = option.data;
          const opt = createProps(renderOpts, optionValue, defaultProps, params);

          // 使用 option 对象本身缓存防抖函数，避免跨表格冲突
          const cacheKey = '_debounceFn_' + oIndex;
          if (!option[cacheKey]) {
            option[cacheKey] = XEUtils.debounce(() => {
              $table &&
                $table.updateFilterOptionStatus(
                  option,
                  Array.isArray(option.data) ? option.data?.length > 0 : !!option.data,
                );
              $table && $table.saveFilterByEvent(new Event('keydown'), column);
            }, 300);
          }

          return h(Component, {
            key: oIndex,
            ...attrs,
            ...opt,
            ...args,
            ...createEvents(
              opt as VxeGlobalRendererHandles.RenderFormItemTitleOptions,
              params,
              (value: any) => {
                // 处理 model 值双向绑定
                option.data = value;
              },
              option[cacheKey],
            ),
          });
        }),
      ),
    ];
  };
}

/**
 * @description: 默认过滤
 * @param {}
 * @return {*}
 * @author: *
 */

export function createDefaultFilterRender() {
  return function (params: VxeGlobalRendererHandles.TableFilterMethodParams) {
    const { option, row, column } = params;
    const { data } = option;
    const cellValue = XEUtils.get(row, column.field as string);
    return cellValue === data;
  };
}

/**
 * @description: 创建 form表单渲染
 */
export function createFormItemRender(
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
    const opts = createProps(renderOpts, itemValue, defaultProps, params);
    return [
      h(Component, {
        ...attrs,
        ...opts,
        ...args,
        ...createEvents(
          opts as VxeGlobalRendererHandles.RenderFormItemTitleOptions,
          params,
          (value: any) => {
            // 处理 model 值双向绑定
            XEUtils.set(data, field, value);
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

/**
 * @description: cell渲染
 */
export function createCellRender(
  getSelectCellValue: Function,
  callBack?: (
    renderOpts: VxeGlobalRendererHandles.RenderTableCellOptions,
    params: VxeGlobalRendererHandles.RenderTableCellParams,
  ) => Array<any>,
) {
  return function (
    renderOpts: VxeGlobalRendererHandles.RenderTableCellOptions,
    params: VxeGlobalRendererHandles.RenderTableCellParams,
  ) {
    const args = (callBack && callBack(renderOpts, params)) ?? [];
    const cellLabel = getSelectCellValue && getSelectCellValue(renderOpts, params, ...args);
    const { placeholder } = renderOpts;
    return [
      h(
        'span',
        {
          class: 'vxe-cell--label',
        },
        placeholder && isEmptyValue(cellLabel)
          ? [
              h(
                'span',
                {
                  class: 'vxe-cell--placeholder',
                },
                formatText(placeholder),
              ),
            ]
          : formatText(cellLabel),
      ),
    ];
  };
}

/**
 *
 * @description: 创建 SELECT类型单元格渲染
 * 优先使用 textMap 中的字典文本
 * @returns
 */
export function createSelectCellRender() {
  return createCellRender(
    (
      renderOpts: VxeGlobalRendererHandles.RenderTableCellOptions,
      params: VxeGlobalRendererHandles.RenderTableCellParams,
    ) => {
      const { row, column } = params;
      const textMap = XEUtils.get(row, 'textMap');
      return textMap && textMap[`${column.field}$DICT_TEXT_`]
        ? textMap[`${column.field}$DICT_TEXT_`]
        : row[column.field];
    },
  );
}

/**
 * @description: 创建 导出渲染
 * @param {}
 * @return {*}
 * @author: *
 */
export function createExportMethod(
  getExportCellValue: Function,
  callBack?: (params: VxeGlobalRendererHandles.ExportMethodParams) => Array<any>,
) {
  return function (params: VxeGlobalRendererHandles.ExportMethodParams) {
    const { row, column, options } = params;
    const args = (callBack && callBack(params)) ?? [];
    return options && options.original
      ? XEUtils.get(row, column.field as string)
      : getExportCellValue(column.editRender || column.cellRender, params, ...args);
  };
}

/**
 * @description: 创建单元格默认显示内容
 */
export function createToolbarToolRender(
  defaultProps?: { [key: string]: any },
  callBack?: (
    renderOpts: VxeGlobalRendererHandles.RenderToolOptions,
    params: VxeGlobalRendererHandles.RenderToolParams,
  ) => Record<string, any>,
) {
  return function (
    renderOpts: VxeGlobalRendererHandles.RenderToolOptions,
    params: VxeGlobalRendererHandles.RenderToolParams,
  ) {
    const { name, attrs } = renderOpts;
    const args = (callBack && callBack(renderOpts, params)) ?? {};

    const Component = getComponent(name);
    return [
      h(Component, {
        ...attrs,
        ...createProps(renderOpts, null, defaultProps, params),
        params: params,
        events: createEvents(renderOpts, params),
        ...args,
        ...createEvents(renderOpts, params),
      }),
    ];
  };
}
