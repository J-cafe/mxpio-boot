<template>
  <BasicModal
    cancelText="关闭"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="查看区域二维码"
    :showOkBtn="false"
  >
    <div class="text-center">
      <QrCode :value="qrCodeUrl" />
      <div>
        <span style="margin-top: 10px">区域名称：{{ area.areaName }}</span>
        <br />
        <span style="margin-top: 5px">区域编码：{{ area.areaCode }}</span>
      </div>
    </div>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner, QrCode } from '@mxpio/components';

  defineOptions({ name: 'AreaQrcodeModal' });

  const isUpdate = ref(true);
  const isDisabled = ref(false);
  let qrCodeUrl = ref('');
  const area = ref<Recordable>({});
  const [registerModal, { setModalProps }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    console.log(data);
    isUpdate.value = !!data?.isUpdate;
    isDisabled.value = !!data?.disabled;
    qrCodeUrl.value = JSON.stringify({
      areaCode: data?.areaCode,
      areaName: data?.areaName,
      id: data?.id,
    });
    area.value = data;
  });
</script>
