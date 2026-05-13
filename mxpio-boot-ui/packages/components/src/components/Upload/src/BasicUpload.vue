<template>
  <div>
    <Space>
      <a-button
        type="primary"
        @click="openUploadModal"
        :disabled="props.disabled"
        preIcon="carbon:cloud-upload"
      >
        {{ $t('component.upload.upload') }}
      </a-button>
      <Tooltip placement="bottom" v-if="showPreview">
        <template #title>
          {{ $t('component.upload.uploaded') }}
          <template v-if="fileList.length">
            {{ fileList.length }}
          </template>
        </template>
        <a-button @click="openPreviewModal">
          <Icon icon="bi:eye" />
          <template v-if="fileList.length && showPreviewNumber">
            {{ fileList.length }}
          </template>
        </a-button>
      </Tooltip>
    </Space>
    <UploadModal
      v-bind="bindValue"
      :previewFileList="fileList"
      :fileListOpenDrag="fileListOpenDrag"
      :fileListDragOptions="fileListDragOptions"
      @register="registerUploadModal"
      @change="handleChange"
      @delete="handleDelete"
    />

    <UploadPreviewModal
      :value="fileList"
      :disabled="props.disabled"
      @register="registerPreviewModal"
      @list-change="handlePreviewChange"
      @delete="handlePreviewDelete"
    />
  </div>
</template>
<script lang="ts" setup>
  import { ref, watch, unref, computed, useAttrs } from 'vue';
  import { Recordable } from '@mxpio/types';
  import Icon from '../../Icon/Icon.vue';
  import { Tooltip, Space } from 'ant-design-vue';
  import { useModal } from '../../Modal';
  import { uploadContainerProps } from './props';
  import { omit } from 'lodash-es';
  import { $t } from '@mxpio/locales';
  import { isArray, isString } from '@mxpio/utils/src/is';
  import UploadModal from './components/UploadModal.vue';
  import UploadPreviewModal from './components/UploadPreviewModal.vue';

  defineOptions({ name: 'BasicUpload' });

  const props = defineProps(uploadContainerProps);
  const emit = defineEmits(['change', 'delete', 'preview-delete', 'update:value']);

  const attrs = useAttrs();
  // 上传modal
  const [registerUploadModal, { openModal: openUploadModal }] = useModal();

  //   预览modal
  const [registerPreviewModal, { openModal: openPreviewModal }] = useModal();

  const fileList = ref<string[]>([]);

  const showPreview = computed(() => {
    const { emptyHidePreview } = props;
    if (!emptyHidePreview) return true;
    return emptyHidePreview ? fileList.value.length > 0 : true;
  });

  const bindValue = computed(() => {
    const value = { ...attrs, ...props };
    return omit(value, 'onChange');
  });

  watch(
    () => props.value,
    (value = []) => {
      fileList.value = isArray(value) ? value : isString(value) ? value.split(',') : [];
    },
    { immediate: true },
  );

  // 上传modal保存操作
  function handleChange(urls: string[], files: File) {
    fileList.value = [...unref(fileList), ...(urls || [])];
    emit(
      'update:value',
      isArray(fileList.value) ? fileList.value.join(',') : fileList.value,
      files,
    );
    emit('change', isArray(fileList.value) ? fileList.value.join(',') : fileList.value, files);
  }

  // 预览modal保存操作
  function handlePreviewChange(urls: string[]) {
    fileList.value = [...(urls || [])];
    emit('update:value', fileList.value);
    emit('change', fileList.value);
  }

  function handleDelete(record: Recordable<any>) {
    emit('delete', record);
  }

  function handlePreviewDelete(url: string) {
    emit('preview-delete', url);
  }
</script>
