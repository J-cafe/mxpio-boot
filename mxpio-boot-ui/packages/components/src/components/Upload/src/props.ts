import type { PropType } from 'vue';
import { FileBasicColumn } from './types/typing';

import type { Options } from 'sortablejs';

import { Merge } from '@mxpio/utils/src/types';
import { uploadApi } from '@mxpio/api';

type SortableOptions = Merge<
  Omit<Options, 'onEnd'>,
  {
    onAfterEnd?: <T = any, R = any>(params: T) => R;
    // ...可扩展
  }
>;

type ListType = 'text' | 'picture' | 'picture-card';

export const basicProps = {
  listType: {
    type: String as PropType<ListType>,
    default: 'picture-card',
  },
  helpText: {
    type: String as PropType<string>,
    default: '',
  },
  // 文件最大多少MB
  maxSize: {
    type: Number as PropType<number>,
    default: 100,
  },
  // 最大数量的文件，Infinity不限制
  maxNumber: {
    type: Number as PropType<number>,
    default: 1,
  },
  // 根据后缀，或者其他
  accept: {
    type: Array as PropType<string[]>,
    default: () => [],
  },
  multiple: {
    type: Boolean as PropType<boolean>,
    default: false,
  },
  uploadParams: {
    type: Object as PropType<any>,
    default: () => ({
      fileStorageType: 'FileSystem', // 文件存储类型FileSystem、Database
    }),
  },
  api: {
    type: Function as PropType<PromiseFn>,
    default: uploadApi,
    required: false,
  },
  name: {
    type: String as PropType<string>,
    default: 'file',
  },
  filename: {
    type: String as PropType<string>,
    default: null,
  },
  fileListOpenDrag: {
    type: Boolean,
    default: true,
  },

  fileListDragOptions: {
    type: Object as PropType<SortableOptions>,
    default: () => ({}),
  },
  disabled: { type: Boolean, default: false },
};

export const uploadContainerProps = {
  value: {
    type: [Array, String] as PropType<string[] | string>,
    default: () => [],
  },
  ...basicProps,
  showPreviewNumber: {
    type: Boolean as PropType<boolean>,
    default: true,
  },
  emptyHidePreview: {
    type: Boolean as PropType<boolean>,
    default: false,
  },
};

export const previewProps = {
  value: {
    type: Array as PropType<string[]>,
    default: () => [],
  },
  disabled: { type: Boolean, default: false },
};

export const fileListProps = {
  columns: {
    type: Array as PropType<FileBasicColumn[]>,
    default: null,
  },
  actionColumn: {
    type: Object as PropType<FileBasicColumn>,
    default: null,
  },
  dataSource: {
    type: Array as PropType<any[]>,
    default: null,
  },
  openDrag: {
    type: Boolean,
    default: false,
  },
  dragOptions: {
    type: Object as PropType<SortableOptions>,
    default: () => ({}),
  },
};
