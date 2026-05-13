import { FormItemContentRenderParams, VxeGlobalRendererHandles } from 'vxe-pc-ui';
import { createDefaultRender, createEditRender, createFormItemRender } from './AButton';

function createEditButtonRender() {
  return function (
    renderOpts: VxeGlobalRendererHandles.RenderEditOptions,
    params: VxeGlobalRendererHandles.RenderEditParams,
  ) {
    const buttonEditRender = createEditRender();
    const { children } = renderOpts;
    if (children) {
      return children.map(
        (childRenderOpts: VxeGlobalRendererHandles.RenderEditOptions) =>
          buttonEditRender(childRenderOpts, params)[0],
      );
    }
    return [];
  };
}

function createDefaultButtonRender() {
  return function (
    renderOpts: VxeGlobalRendererHandles.RenderDefaultOptions,
    params: VxeGlobalRendererHandles.RenderDefaultParams,
  ) {
    const buttonDefaultRender = createDefaultRender();
    const { children } = renderOpts;
    if (children) {
      return children.map(
        (childRenderOpts: VxeGlobalRendererHandles.RenderDefaultOptions) =>
          buttonDefaultRender(childRenderOpts, params)[0],
      );
    }
    return [];
  };
}

function createButtonItemRender() {
  return function (
    renderOpts: VxeGlobalRendererHandles.RenderFormItemTitleOptions,
    params: FormItemContentRenderParams,
  ) {
    const buttonItemRender = createFormItemRender();
    const { children } = renderOpts;
    if (children) {
      return children.map(
        (childRenderOpts: VxeGlobalRendererHandles.RenderFormItemTitleOptions) =>
          buttonItemRender(childRenderOpts, params)[0],
      );
    }
    return [];
  };
}

export default {
  renderTableEdit: createEditButtonRender(),
  renderTableDefault: createDefaultButtonRender(),
  renderFormItemContent: createButtonItemRender(),
};
