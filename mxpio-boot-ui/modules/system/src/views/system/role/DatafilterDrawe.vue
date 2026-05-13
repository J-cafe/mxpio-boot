<template>
  <BasicDrawer
    width="500px"
    v-bind="$attrs"
    @register="registerDrawer"
    destroyOnClose
    title="数据过滤"
    @ok="handleSubmit"
    showFooter
  >
    <BasicTable @register="registerTable" />
  </BasicDrawer>
</template>
<script lang="ts" setup>
  import { ref, reactive, Ref } from 'vue';
  import { BasicDrawer, useDrawerInner, BasicTable, useTable } from '@mxpio/components';
  import { datafilterList, saveRoleDatafilter, roleWithinDatafilter } from '@mxpio/api';
  import { datafilterColumns } from './role.data';
  import { useMessage } from '@mxpio/hooks/src/web/useMessage';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';
  import type { Key } from 'ant-design-vue/lib/table/interface';

  defineOptions({ name: 'DatafilterDrawe' });

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();

  const resId: Ref<string> = ref('');
  const roleId: Ref<string> = ref('');

  let defaultCheckedObj: object = reactive({});
  let defaultChecked: string[] = reactive([]);

  const [registerTable, { reload, getSelectRowKeys, setSelectedRowKeys }] = useTable({
    api: loadData,
    rowKey: 'id',
    rowSelection: { type: 'checkbox' },
    columns: datafilterColumns,
    striped: false,
    showTableSetting: true,
    bordered: true,
    pagination: false,
    immediate: false,
    canResize: false,
  });

  const [registerDrawer, { closeDrawer }] = useDrawerInner(async (data) => {
    resId.value = data.resId;
    roleId.value = data.roleId;
    reload();
    getRoleWithinDatafilter();
  });

  async function loadData() {
    if (resId.value) {
      const data = await datafilterList(resId.value);
      return data;
    }
    return [];
  }

  async function getRoleWithinDatafilter() {
    const data = await roleWithinDatafilter(resId.value, roleId.value);
    defaultCheckedObj = Object.assign({}); // 保存初始化选中
    defaultChecked = [];
    data.forEach((item) => {
      defaultCheckedObj[item.id] = item;
      defaultChecked.push(item.dataFilterId);
    });
    setSelectedRowKeys(defaultChecked);
  }

  async function handleSubmit() {
    try {
      const ids: Key[] = getSelectRowKeys();
      const data = getDataFilterData(ids);
      await saveRoleDatafilter(data);
      createMessage.success('操作成功');
      closeDrawer();
      emit('success');
    } finally {
      // setModalProps({ confirmLoading: false });
    }
  }

  // 获取当前弹窗分配的权限数据
  function getDataFilterData(selectedKeys) {
    const dataFilterData: any[] = [];
    // 检查脏数据
    selectedKeys.forEach((key) => {
      const item = defaultCheckedObj[key];
      const data = {
        roleId: roleId.value,
        dataFilterId: key,
        id: item?.id,
        crudType: item?.id ? CuryTypeEnum.UPDATE : CuryTypeEnum.SAVE,
      };
      dataFilterData.push(data);
    });
    Object.keys(defaultCheckedObj).forEach((id) => {
      const item = defaultCheckedObj[id];
      if (!selectedKeys.includes(id)) {
        const data = {
          roleId: roleId.value,
          dataFilterId: item.dataFilterId,
          id: item && item.id,
          crudType: CuryTypeEnum.DELETE,
        };
        dataFilterData.push(data);
      }
    });
    return dataFilterData;
  }
</script>
