<template>
  <div>
    <List :class="prefixCls" bordered v-if="getData.length > 0">
      <template v-for="item in getData" :key="item.id">
        <List.Item class="list-item">
          <List.Item.Meta>
            <template #title>
              <div class="title">
                <Typography.Paragraph
                  @click="handleTitleClick(item)"
                  :ellipsis="
                    titleRows && titleRows > 0
                      ? { rows: titleRows, tooltip: !!item.messageTitle }
                      : false
                  "
                  :content="item.messageTitle"
                />
                <div class="extra" v-if="item.extra">
                  <Tag class="tag" :color="item.color">
                    {{ item.extra }}
                  </Tag>
                </div>
              </div>
            </template>

            <template #avatar>
              <Avatar class="avatar" style="background-color: #87d068">{{
                item.fromNickName.length > 2 ? item.fromNickName.substring(0, 2) : item.fromNickName
              }}</Avatar>
            </template>

            <template #description>
              <div class="datetime">
                {{ item.createTime }}
              </div>
            </template>
          </List.Item.Meta>
        </List.Item>
      </template>
    </List>
    <a-empty description="无新消息" v-else />
    <div style="margin-top: 5px; text-align: center">
      <a-button @click="toMyAnnouncement()" type="dashed" block>查看更多</a-button>
    </div>
    <MessageModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { computed, PropType } from 'vue';
  import { ListItem } from './data';
  import { useDesign } from '@mxpio/hooks/src/web/useDesign';
  import { List, Avatar, Tag, Typography } from 'ant-design-vue';
  import { useBridge } from '@mxpio/bridge';
  import MessageModal from './MessageModal.vue';
  import { useModal } from '@mxpio/components';

  const [registerModal, { openModal }] = useModal();
  const props = defineProps({
    list: {
      type: Array as PropType<ListItem[]>,
      default: () => [],
    },
    titleRows: {
      type: Number,
      default: 1,
    },
    descRows: {
      type: Number,
      default: 2,
    },
    onTitleClick: {
      type: Function as PropType<(Recordable) => void>,
    },
  });

  const { prefixCls } = useDesign('header-notify-list');
  const getData = computed(() => {
    const { list } = props;
    return list;
  });

  function toMyAnnouncement() {
    const { router } = useBridge();
    router.push('/message/messageList');
  }

  function handleTitleClick(item: ListItem) {
    openModal(true, {
      record: item,
    });
  }

  function handleSuccess() {
    props.onTitleClick && props?.onTitleClick();
  }
</script>
<style lang="less" scoped>
  @prefix-cls: ~'@{namespace}-header-notify-list';

  .@{prefix-cls} {
    &::-webkit-scrollbar {
      display: none;
    }

    ::v-deep(.ant-pagination-disabled) {
      display: inline-block !important;
    }

    .list-item {
      padding: 6px;
      overflow: hidden;
      transition: all 0.3s;
      cursor: pointer;

      .title {
        margin-bottom: 8px;
        font-weight: normal;

        .extra {
          margin-top: -1.5px;
          margin-right: 0;
          float: right;
          font-weight: normal;

          .tag {
            margin-right: 0;
          }
        }
      }

      .avatar {
        margin-top: 4px;
      }

      .description {
        font-size: 12px;
        line-height: 18px;
      }

      .datetime {
        margin-top: 4px;
        font-size: 12px;
        line-height: 18px;
      }
    }
  }
</style>
