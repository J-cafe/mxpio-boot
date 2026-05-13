<template>
  <BasicModal
    width="800px"
    :title="$t('component.upload.preview')"
    class="upload-preview-modal"
    v-bind="$attrs"
    @register="register"
    :showOkBtn="false"
  >
    <FileList :dataSource="fileListRef" :columns="columns" :actionColumn="actionColumn" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { watch, ref } from 'vue';
  import FileList from './FileList.vue';
  import { BasicModal, useModalInner } from '../../../Modal';
  import { previewProps } from '../props';
  import { PreviewFileItem } from '../types/typing';
  import { useCommon } from '@mxpio/hooks';
  import { createPreviewColumns, createPreviewActionColumn } from './data';
  import { $t } from '@mxpio/locales';
  import { isArray } from '@mxpio/utils/src/is';
  import { getFileInfo } from '@mxpio/api/src/sys/file';

  const props = defineProps(previewProps);

  const emit = defineEmits(['list-change', 'register', 'delete']);
  const { downloadByFileNo } = useCommon();
  const columns = createPreviewColumns() as any[];
  const actionColumn = ref<any>(
    createPreviewActionColumn({
      handleRemove,
      handleDownload,
      disabled: props.disabled,
    }),
  );

  const [register] = useModalInner();

  watch(
    () => props.disabled,
    (newDisabled) => {
      // 当 disabled 变化时重新创建操作列
      actionColumn.value = createPreviewActionColumn({
        handleRemove,
        handleDownload,
        disabled: newDisabled,
      });
    },
  );

  const fileListRef = ref<PreviewFileItem[]>([]);
  watch(
    () => props.value,
    (value) => {
      if (!isArray(value)) value = [];
      const axiosList: Promise<any>[] = [];
      value.forEach((fileNo) => {
        axiosList.push(getFileInfo(fileNo));
      });
      Promise.all(axiosList).then((rspList) => {
        fileListRef.value = rspList.map((file) => {
          return {
            url: file.fileNo,
            type: file.fileName.split('.').pop() || '',
            name: file.fileName || '',
            size: file.length,
            fileNo: file.fileNo,
          };
        });
      });
      // fileListRef.value = value
      //   .filter((item) => !!item)
      //   .map((item) => {
      //     return {
      //       url: item,
      //       type: item.split('.').pop() || '',
      //       name: item.split('/').pop() || '',
      //     };
      //   });
    },
    { immediate: true },
  );

  // 删除
  function handleRemove(record: PreviewFileItem) {
    const index = fileListRef.value.findIndex((item) => item.url === record.url);
    if (index !== -1) {
      const removed = fileListRef.value.splice(index, 1);
      emit('delete', removed[0].url);
      emit(
        'list-change',
        fileListRef.value.map((item) => item.url),
      );
    }
  }

  // 下载
  function handleDownload(record: PreviewFileItem) {
    const { fileNo } = record;
    downloadByFileNo(fileNo as string);
  }
</script>
<style lang="less">
  .upload-preview-modal {
    .ant-upload-list {
      display: none;
    }

    .ant-table-wrapper .ant-spin-nested-loading {
      padding: 0;
    }
  }
</style>
