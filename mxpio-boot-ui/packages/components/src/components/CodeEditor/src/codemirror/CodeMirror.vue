<template>
  <div
    class="relative !h-full w-full overflow-hidden"
    :class="{ 'ant-input': props.bordered, 'css-dev-only-do-not-override-kqecok': props.bordered }"
    ref="el"
  ></div>
</template>

<script lang="ts" setup>
  import {
    type PropType,
    ref,
    onMounted,
    onUnmounted,
    watchEffect,
    watch,
    unref,
    nextTick,
  } from 'vue';
  import type { Nullable } from '@mxpio/types';
  import { useWindowSizeFn } from '@mxpio/hooks';
  import { useDebounceFn } from '@vueuse/core';
  // import { useAppStore } from '@mxpio/stores/src/modules/app';
  import { useAppStore } from '@mxpio/stores';
  import CodeMirror from 'codemirror';
  import { MODE } from './../typing';

  // css
  import './codemirror.css';
  import 'codemirror/theme/base16-light.css';
  // import 'codemirror/theme/idea.css';
  import 'codemirror/theme/material-palenight.css';
  // modes
  import 'codemirror/mode/javascript/javascript';
  import 'codemirror/mode/css/css';
  import 'codemirror/mode/htmlmixed/htmlmixed';
  import 'codemirror/mode/sql/sql.js';
  // 加提示的引用
  import 'codemirror/addon/hint/show-hint.css';
  import 'codemirror/addon/hint/sql-hint.js';
  import 'codemirror/addon/hint/show-hint.js';

  const props = defineProps({
    mode: {
      type: String as PropType<MODE>,
      default: MODE.JSON,
      validator(value: any) {
        // 这个值必须匹配下列字符串中的一个
        return Object.values(MODE).includes(value);
      },
    },
    value: { type: String, default: '' },
    readonly: { type: Boolean, default: false },
    bordered: { type: Boolean, default: false },
  });

  const emit = defineEmits(['change']);

  const el = ref();
  let editor: Nullable<CodeMirror.Editor>;

  const debounceRefresh = useDebounceFn(refresh, 100);
  const appStore = useAppStore();

  watch(
    () => props.value,
    async (value) => {
      await nextTick();
      const oldValue = editor?.getValue();
      if (value !== oldValue) {
        editor?.setValue(value ? value : '');
      }
    },
    { flush: 'post' },
  );

  watchEffect(() => {
    editor?.setOption('mode', props.mode);
  });

  watch(
    () => appStore.getDarkMode,
    async () => {
      setTheme();
    },
    {
      immediate: true,
    },
  );

  function setTheme() {
    unref(editor)?.setOption(
      'theme',
      appStore.getDarkMode === 'light' ? 'base16-light' : 'material-palenight',
    );
  }

  function refresh() {
    editor?.refresh();
  }

  async function init() {
    const addonOptions = {
      autoCloseBrackets: true,
      autoCloseTags: true,
      foldGutter: true,
      gutters: ['CodeMirror-linenumbers'],
    };

    editor = CodeMirror(el.value!, {
      value: '',
      mode: props.mode,
      readOnly: props.readonly,
      tabSize: 2,
      theme: 'base16-light',
      lineWrapping: true,
      lineNumbers: true,
      ...addonOptions,
      hintOptions: {
        completeSingle: false,
      },
    });
    editor?.setValue(props.value);
    setTheme();
    editor?.on('change', () => {
      emit('change', editor?.getValue());
    });
    editor?.on('inputRead', function () {
      editor.showHint();
    });
  }

  onMounted(async () => {
    await nextTick();
    init();
    useWindowSizeFn(debounceRefresh);
  });

  onUnmounted(() => {
    editor = null;
  });

  function getSelection() {
    return editor.getSelection();
  }

  defineExpose({ getSelection });
  //
</script>
