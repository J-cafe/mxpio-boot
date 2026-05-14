import type { ComponentType } from '../componentType';
import { tryOnUnmounted } from '@vueuse/core';
import { add, del } from '../componentMap';
import type { Component } from 'vue';
import { isPascalCase } from '@mxpio/utils/src/is';
import VxeUITable from 'vxe-table';

export function useVxeComponentRegister<T extends string, R extends Component>(
  compName: ComponentType | T,
  comp: R,
  render: VxeUITable.VxeGlobalRendererOptions,
) {
  if (!isPascalCase(compName)) {
    throw new Error('compName must be in PascalCase');
  }

  add(compName, comp);
  const { renderer } = VxeUITable;
  renderer.add(compName, render);
  tryOnUnmounted(() => {
    del(compName);
    renderer.delete(compName);
  });
}
