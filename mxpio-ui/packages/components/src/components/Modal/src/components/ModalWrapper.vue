<template>
  <ScrollContainer ref="wrapperRef" :scrollHeight="realHeight">
    <div ref="spinRef" :style="spinStyle" v-loading="loading" :loading-tip="loadingTip">
      <slot></slot>
    </div>
  </ScrollContainer>
</template>
<script lang="ts" setup>
  import type { CSSProperties } from 'vue';
  import { computed, ref, watchEffect, unref, watch, onMounted, nextTick, onUnmounted } from 'vue';
  import { useWindowSizeFn } from '@mxpio/hooks';
  import { type AnyFunction } from '@mxpio/types';
  import { ScrollContainer } from '../../../Container';
  import { createModalContext } from '../hooks/useModalContext';
  import { useMutationObserver } from '@vueuse/core';

  defineOptions({ name: 'ModalWrapper', inheritAttrs: false });

  const props = defineProps({
    loading: { type: Boolean },
    useWrapper: { type: Boolean, default: true },
    modalHeaderHeight: { type: Number, default: 57 },
    modalFooterHeight: { type: Number, default: 74 },
    minHeight: { type: Number, default: 200 },
    height: { type: Number },
    footerOffset: { type: Number, default: 0 },
    open: { type: Boolean },
    fullScreen: { type: Boolean },
    loadingTip: { type: String },
  });

  const emit = defineEmits(['height-change', 'ext-height']);

  const wrapperRef = ref(null);
  const spinRef = ref(null);
  const realHeightRef = ref(0);
  const minRealHeightRef = ref(0);
  const realHeight = ref(0);

  let stopElResizeFn: AnyFunction = () => {};

  useWindowSizeFn(setModalHeight.bind(null));

  useMutationObserver(
    spinRef,
    (mutationList) => {
      // 检查是否有任何变化涉及vxe-grid或其子元素
      const hasVxeGridMutation = mutationList.some((mutation) => {
        const target = mutation.target;
        // 排除非元素节点
        if (!(target instanceof Element) || target.nodeName === 'svg') return false;
        // 检查目标是否是vxe-grid元素或其子元素
        return target.className.includes?.('vxe-') || target.closest('.vxe-grid');
      });
      // xys 20251128 如果没有涉及vxe-grid的变化才触发高度调整，修复vxe-grid表格滚动条会触发重新渲染问题
      if (!hasVxeGridMutation) {
        setModalHeight();
      }
    },
    {
      attributes: true,
      subtree: true,
    },
  );

  createModalContext({
    redoModalHeight: setModalHeight,
  });

  const spinStyle = computed((): CSSProperties => {
    return {
      minHeight: `${props.minHeight}px`,
      [props.fullScreen ? 'height' : 'maxHeight']: `${unref(realHeightRef)}px`,
    };
  });

  watchEffect(() => {
    props.useWrapper && setModalHeight();
  });

  watch(
    () => props.fullScreen,
    (v) => {
      setModalHeight();
      if (!v) {
        realHeightRef.value = minRealHeightRef.value;
      } else {
        minRealHeightRef.value = realHeightRef.value;
      }
    },
  );

  onMounted(() => {
    const { modalHeaderHeight, modalFooterHeight } = props;
    emit('ext-height', modalHeaderHeight + modalFooterHeight);
  });

  onUnmounted(() => {
    stopElResizeFn && stopElResizeFn();
  });

  async function scrollTop() {
    nextTick(() => {
      const wrapperRefDom = unref(wrapperRef);
      if (!wrapperRefDom) return;
      (wrapperRefDom as any)?.scrollTo?.(0);
    });
  }

  async function setModalHeight() {
    // 解决在弹窗关闭的时候监听还存在,导致再次打开弹窗没有高度
    // 加上这个,就必须在使用的时候传递父级的open
    if (!props.open) return;
    const wrapperRefDom = unref(wrapperRef);
    if (!wrapperRefDom) return;

    const bodyDom = (wrapperRefDom as any).$el.parentElement;
    if (!bodyDom) return;
    bodyDom.style.padding = '0';
    await nextTick();

    try {
      // const modalDom = bodyDom.parentElement && bodyDom.parentElement.parentElement;
      // xys 20251128找到最外层的modal元素，修复之前外层modal查找失败问题
      const modalDom = bodyDom.closest('.ant-modal');
      if (!modalDom) return;
      const modalRect = getComputedStyle(modalDom as Element).top;
      const modalTop = Number.parseInt(modalRect);
      let maxHeight =
        window.innerHeight -
        modalTop * 2 +
        (props.footerOffset! || 0) -
        props.modalFooterHeight -
        props.modalHeaderHeight;
      // 距离顶部过进会出现滚动条
      if (modalTop < 40) {
        maxHeight -= 26;
      }
      await nextTick();
      const spinEl: any = unref(spinRef);

      if (!spinEl) return;
      await nextTick();
      // if (!realHeight) {
      realHeight.value = spinEl.scrollHeight;
      // }

      if (props.fullScreen) {
        realHeightRef.value =
          window.innerHeight - props.modalFooterHeight - props.modalHeaderHeight - 28;
      } else {
        realHeightRef.value = props.height
          ? props.height
          : realHeight.value > maxHeight
            ? maxHeight
            : realHeight.value;
      }
      emit('height-change', unref(realHeightRef));
    } catch (error) {
      console.log(error);
    }
  }

  defineExpose({ scrollTop, setModalHeight });
</script>
