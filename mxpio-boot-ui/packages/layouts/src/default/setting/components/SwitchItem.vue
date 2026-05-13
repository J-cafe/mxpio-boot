<template>
  <div :class="prefixCls">
    <span> {{ title }}</span>
    <Switch
      v-bind="getBindValue"
      @change="handleChange"
      :disabled="disabled"
      :checkedChildren="$t('layout.setting.on')"
      :unCheckedChildren="$t('layout.setting.off')"
    />
  </div>
</template>
<script lang="ts" setup>
  import { PropType, computed } from 'vue';

  import { Switch, type SwitchProps } from 'ant-design-vue';
  import { useDesign } from '@mxpio/hooks/src/web/useDesign';
  import { $t } from '@mxpio/locales';
  import { baseHandler } from '../handler';
  import { HandlerEnum } from '../enum';

  defineOptions({ name: 'SwitchItem' });

  const props = defineProps({
    event: {
      type: Number as PropType<HandlerEnum>,
    },
    disabled: {
      type: Boolean,
    },
    title: {
      type: String,
    },
    def: {
      type: Boolean,
    },
  });

  const { prefixCls } = useDesign('setting-switch-item');

  const getBindValue = computed(() => {
    return props.def ? { checked: props.def } : {};
  });

  const handleChange: SwitchProps['onChange'] = (val) => {
    props.event && baseHandler(props.event, val);
  };
</script>
<style lang="less" scoped>
  @prefix-cls: ~'@{namespace}-setting-switch-item';

  .@{prefix-cls} {
    display: flex;
    justify-content: space-between;
    margin: 16px 0;
  }
</style>
