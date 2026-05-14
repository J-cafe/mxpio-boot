// import type { GlobConfig } from '@mxpio/types';
import { isEqual } from 'lodash-es';
import type { DeepReadonly, Ref, UnwrapRef, WritableComputedRef } from 'vue';
import {
  computed,
  getCurrentInstance,
  nextTick,
  reactive,
  readonly,
  toRaw,
  unref,
  watchEffect,
} from 'vue';

export function useRuleFormItem<T extends Recordable, K extends keyof T, V = UnwrapRef<T[K]>>(
  props: T,
  key?: K,
  changeEvent?,
  emitData?: Ref<any[]>,
): [WritableComputedRef<V>, (val: V) => void, DeepReadonly<V>];

export function useRuleFormItem<T extends Recordable>(
  props: T,
  key: keyof T = 'value',
  changeEvent = 'change',
  emitData?: Ref<any[]>,
) {
  const instance = getCurrentInstance();
  const emit = instance?.emit;

  const innerState = reactive({
    value: props[key],
  });

  const defaultState = readonly(innerState);

  const setState = (val: UnwrapRef<T[keyof T]>): void => {
    innerState.value = val as T[keyof T];
  };

  watchEffect(() => {
    innerState.value = props[key];
  });

  const state: any = computed({
    get() {
      const { arrayToString, numberToString } = props;
      if (arrayToString && innerState.value?.indexOf(',') > -1) {
        return innerState.value.split(',');
      }
      return innerState.value === null || innerState.value === undefined
        ? undefined
        : numberToString
          ? `${innerState.value}`
          : innerState.value;
    },
    set(value) {
      if (isEqual(value, defaultState.value)) return;

      innerState.value = value as T[keyof T];
      nextTick(() => {
        const { arrayToString } = props;
        emit?.(changeEvent, arrayToString ? `${value}` : value, ...(toRaw(unref(emitData)) || []));
        // 触发 update:value 事件,修复自定义组件在vxe-table中使用时, 不能更新 value 的问题
        //   changeEvent !== 'update:value' && emit?.(
        //   `update:value`,
        //   arrayToString ? `${value}` : value,
        //   ...(toRaw(unref(emitData)) || []),
        // );
      });
    },
  });

  return [state, setState, defaultState];
}
