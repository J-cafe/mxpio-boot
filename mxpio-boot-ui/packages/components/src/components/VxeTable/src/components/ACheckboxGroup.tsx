import { createFormItemRender, getComponent, createEvents, getModelKey } from './common';
import { h, isRef } from 'vue';
import { VxeGlobalRendererHandles, FormItemContentRenderParams } from 'vxe-pc-ui';
import XEUtils from 'xe-utils';

function createProps(
  renderOpts: VxeGlobalRendererHandles.RenderOptions,
  value: any,
  defaultProps?: { [prop: string]: any },
  params?: VxeGlobalRendererHandles.RenderParams,
) {
  // 新增：处理函数类型的 props
  let renderProps = renderOpts.props;
  if (typeof renderProps === 'function' && params) {
    renderProps = renderProps(params);
  }

  // 支持 value 传入 ref 对象，自动解包
  const modelKey = getModelKey(renderOpts);
  const rawValue = renderProps?.[modelKey];
  const actualValue = isRef(rawValue) ? rawValue.value : rawValue || value;

  return XEUtils.assign({}, defaultProps, renderProps, {
    [modelKey]: actualValue,
  });
}

function createToolbarButtonRender(
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
    const { name } = renderOpts;
    const { attrs } = renderOpts;

    // 获取原始 props（可能是函数返回的结果）
    let rawProps = renderOpts.props;
    if (typeof rawProps === 'function' && params) {
      rawProps = rawProps(params);
    }

    // 检测 value 是否为 ref，用于自动双向绑定
    const modelKey = getModelKey(renderOpts);
    const rawValue = rawProps?.[modelKey];
    const valueRef = isRef(rawValue) ? rawValue : null;

    const props = createProps(renderOpts, undefined, {}, params);
    const Component = getComponent(name);

    return [
      h(Component, {
        ...attrs,
        ...props,
        ...args,
        ...createEvents(renderOpts, params, (value) => {
          // 处理 model 值双向绑定
          // 如果传入的是 ref，自动更新
          if (valueRef) {
            valueRef.value = value;
          }
        }),
      }),
    ];
  };
}

export default {
  renderFormItemContent: createFormItemRender(),
  renderToolbarButton: createToolbarButtonRender(),
};
