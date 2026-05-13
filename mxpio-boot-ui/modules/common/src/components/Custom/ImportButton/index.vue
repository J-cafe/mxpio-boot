<template>
  <div>
    <a-button type="primary" @click="handleClick"
      ><Icon icon="ant-design:upload-outlined" /> 导入
    </a-button>
    <ImportModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
  import { onMounted } from 'vue';
  import { useModal, Icon } from '@mxpio/components';
  import ImportModal from './ImportModal.vue';
  import { importUpload } from '@mxpio/api';
  import { useMessage } from '@mxpio/hooks/src/web/useMessage';

  defineOptions({ name: 'ImportButton' });

  const [registerModal, { openModal }] = useModal();
  const { createMessage } = useMessage();
  const props = defineProps({
    importCode: {
      type: String,
      default: null,
      required: true,
    },
    importApi: {
      type: Function,
      default: null,
    },
  });

  function handleClick() {
    openModal(true, {});
  }

  async function handleSuccess({ file }: { file: File }) {
    try {
      const formData = new FormData();
      formData.append('file', file);
      const { data } = await importUpload(props.importCode, {
        file: file,
      });

      if (data.success) {
        createMessage.info(data?.message);
      } else {
        createMessage.error(data?.message);
      }
    } catch {
      createMessage.error('导入失败');
    }
  }

  onMounted(() => {});
</script>
