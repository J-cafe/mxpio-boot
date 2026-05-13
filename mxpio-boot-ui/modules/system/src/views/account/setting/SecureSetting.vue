<template>
  <CollapseContainer title="安全设置" :canExpan="false">
    <List>
      <template v-for="item in secureSettingList" :key="item.key">
        <ListItem>
          <ListItemMeta>
            <template #title>
              {{ item.title }}
              <div
                class="float-right mt-10px mr-30px text-blue-500 text-font-normal cursor-pointer"
                v-if="item.extra"
                @click="handleExtra(item.key)"
              >
                {{ item.extra }}
              </div>
            </template>
            <template #description>
              <div>{{ item.description }}</div>
            </template>
          </ListItemMeta>
        </ListItem>
      </template>
    </List>
    <UpdatePasswordModal :width="700" @register="registerPasswordModal" />
  </CollapseContainer>
</template>
<script lang="ts" setup>
  import { List } from 'ant-design-vue';
  import { CollapseContainer, useModal } from '@mxpio/components';
  import { secureSettingList } from './data';
  import UpdatePasswordModal from './UpdatePasswordModal.vue';

  const [registerPasswordModal, { openModal: openPasswordModal }] = useModal();
  const ListItem = List.Item;
  const ListItemMeta = List.Item.Meta;

  function handleExtra(key: string) {
    switch (key) {
      case '1':
        openPasswordModal(true);
        break;
      default:
        break;
    }
  }
</script>
