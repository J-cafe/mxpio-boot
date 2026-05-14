<template>
  <div class="user-select">
    <a-input-search
      :placeholder="placeholder"
      :value="nicknames"
      :disabled="disabled"
      readonly
      allowClear
      @search="showSelectModal"
    />
    <UserByDeptModal
      :multiple="props.multiple"
      @register="registerModal"
      @success="handleSuccess"
    />
  </div>
</template>

<script lang="ts" setup>
  import { useModal } from '@mxpio/components';
  import UserByDeptModal from './UserByDeptModal.vue';
  import { getUserList } from '@mxpio/api';
  import { getQueryParams } from '@mxpio/utils/src/criteria';
  import { ref, watch, Ref } from 'vue';

  defineOptions({ name: 'UserByDeptSelect' });

  const nicknames: Ref<string> = ref(''); // 用户昵称数组
  const selectUsernames: Ref<string[]> = ref([]); // 已选择的用户数组
  const selectUser: Ref<Recordable[]> = ref([]); // 选中的行数据
  const emit = defineEmits(['update:value', 'change']);

  const props = defineProps({
    multiple: { type: Boolean, default: true }, //单选、双选
    value: { type: String },
    disabled: { type: Boolean, default: false },
    placeholder: { type: String, default: '选择用户' },
  });

  const [registerModal, { openModal }] = useModal();

  // 打开选择用户弹窗
  function showSelectModal() {
    openModal(true, {
      selectUser: selectUser.value,
    });
  }

  // 选择用户成功
  function handleSuccess(usernames: string[], users: Recordable[]) {
    selectUsernames.value = usernames;
    selectUser.value = users;
    emit('update:value', usernames.join(','), users);
    emit('change', usernames.join(','), users);
    getUserNames(users);
  }

  // 获取用户昵称
  async function getUserNames(users?) {
    const usernameList: any[] = [];
    if (users) {
      users.forEach((item: Recordable) => {
        usernameList.push(item.nickname);
      });
    } else {
      const params = getQueryParams(
        {
          page: 1,
          size: 1000,
          'username@IN': props.value,
        },
        {},
      );
      const { content } = await getUserList(params);
      content.forEach((item: Recordable) => {
        usernameList.push(item.nickname);
      });
      selectUser.value = content;
    }
    nicknames.value = usernameList.join(',');
  }

  watch(
    () => props.value,
    () => {
      if (!props.value) {
        nicknames.value = '';
      }
      if (props.value && props.value !== selectUsernames.value.join(',')) {
        getUserNames();
      }
    },
    { immediate: true },
  );
</script>
