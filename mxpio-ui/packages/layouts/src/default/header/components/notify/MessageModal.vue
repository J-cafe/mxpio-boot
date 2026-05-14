<template>
  <BasicModal
    destroyOnClose
    v-bind="$attrs"
    @register="registerModal"
    title="详情"
    cancelText="关闭"
    :okButtonProps="{ class: { '!hidden': true } }"
  >
    <a-card :bordered="false" :bodyStyle="{ padding: '10px 24px' }">
      <h1 class="font-semibold text-center" style="font-size: 18px">{{ content.messageTitle }}</h1>
      <a-divider class="!m-3" />
      <a-card-meta
        class="text-center"
        :description="'发布人：' + content.fromNickName + ' 发布时间： ' + content.createTime"
      />
      <a-divider class="!m-3" />
      <div class="overflow-y-scroll">
        <div v-html="content.messageContent" class="article-content"> </div>
      </div>
    </a-card>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner } from '@mxpio/components';
  import { readMessage } from '@mxpio/api';

  defineOptions({ name: 'MessageModal' });

  const emit = defineEmits(['success', 'register']);

  let content = ref<Recordable>({});

  const [registerModal] = useModalInner(async (data) => {
    content.value = data.record;
    readMessage('innerMsg', data.record.id).then(() => {
      emit('success');
    });
  });
</script>
