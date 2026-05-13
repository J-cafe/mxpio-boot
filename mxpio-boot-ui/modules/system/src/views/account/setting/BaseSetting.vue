<template>
  <collapse-container title="基本设置" :canExpan="false">
    <a-row :gutter="24">
      <a-col :span="14">
        <BasicForm @register="register" />
        <div>
          <a-button type="primary" style="width: 400px; margin-left: 120px" @click="handleSubmit">
            更新基本信息
          </a-button>
        </div>
      </a-col>
      <a-col :span="10">
        <div class="change-avatar">
          <div class="mb-2">头像</div>
          <cropper-avatar
            :value="avatar"
            btnText="更换头像"
            :btnProps="{ preIcon: 'ant-design:cloud-upload-outlined' }"
            @change="updateAvatar"
            width="150"
          />
        </div>
      </a-col>
    </a-row>
  </collapse-container>
</template>

<script lang="ts" setup>
  import { Row as ARow, Col as ACol } from 'ant-design-vue';
  import { computed, onMounted } from 'vue';
  import { BasicForm, useForm, CollapseContainer, CropperAvatar } from '@mxpio/components';
  import { useMessage } from '@mxpio/hooks/src/web/useMessage';
  import { useUserStore } from '@mxpio/stores/src/modules/user';
  import { baseSetschemas } from './data';
  import { editUser } from '@mxpio/api';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';

  const { createMessage } = useMessage();
  const userStore = useUserStore();

  const [register, { setFieldsValue, validate }] = useForm({
    labelWidth: 120,
    schemas: baseSetschemas,
    showActionButtonGroup: false,
  });

  onMounted(async () => {
    setFieldsValue({
      ...userStore.getUserInfo,
    });
  });

  const avatar = computed(() => {
    const { avatar } = userStore.getUserInfo;
    return avatar;
  });

  async function updateAvatar({ data }) {
    const userinfo = userStore.getUserInfo;
    userinfo.avatar = data;
    await editUser({
      ...userinfo,
      username: userinfo.username,
      crudType: CuryTypeEnum.UPDATE,
      avatar: data,
      authorities: null,
      roles: null,
    });
    createMessage.success('更新成功！');
    userStore.setUserInfo(userinfo);
  }

  async function handleSubmit() {
    let values = await validate();
    const userinfo = userStore.getUserInfo;
    await editUser({
      crudType: CuryTypeEnum.UPDATE,
      ...userinfo,
      ...values,
      authorities: null,
      roles: null,
      // avatar: userinfo.avatar,
    });
    userStore.setUserInfo({
      ...userinfo,
      nickname: values.nickname,
      phone: values.phone,
      email: values.email,
      introduction: values.introduction,
    });
    createMessage.success('更新成功！');
  }
</script>

<style lang="less" scoped>
  .change-avatar {
    img {
      display: block;
      margin-bottom: 15px;
      border-radius: 50%;
    }
  }
</style>
