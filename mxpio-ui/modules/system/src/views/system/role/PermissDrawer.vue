<template>
  <BasicDrawer
    width="500px"
    v-bind="$attrs"
    @register="registerDrawer"
    destroyOnClose
    title="权限分配"
    @ok="handleSubmit"
    showFooter
  >
    <ATree
      :defaultExpandAll="true"
      :checkStrictly="checkStrictly"
      :checkable="true"
      :showIcon="true"
      :treeData="treeData"
      :fieldNames="{ key: 'id', title: 'title' }"
      v-model:checkedKeys="selectedKeys"
      v-model:expandedKeys="expandedKeys"
    >
      <template #icon="{ hasFilter, id }">
        <FilterOutlined
          @click="handleDatafilter(id)"
          v-if="hasFilter"
          style="color: red; font-size: 16px"
        />
      </template>
    </ATree>
    <template #footer>
      <a-row>
        <a-col :span="8" style="text-align: left">
          <a-dropdown>
            <a class="ant-dropdown-link" @click.prevent>
              操作
              <DownOutlined />
            </a>
            <template #overlay>
              <a-menu>
                <a-menu-item>
                  <a href="javascript:;" @click="handleCheckStrictly(false)">父子关联</a>
                </a-menu-item>
                <a-menu-item>
                  <a href="javascript:;" @click="handleCheckStrictly(true)">取消关联</a>
                </a-menu-item>
                <a-menu-item>
                  <a href="javascript:;" @click="handleSelect(true)">选中全部</a>
                </a-menu-item>
                <a-menu-item>
                  <a href="javascript:;" @click="handleSelect(false)">取消选中</a>
                </a-menu-item>
                <a-menu-item>
                  <a href="javascript:;" @click="handleExpand(true)">展开全部</a>
                </a-menu-item>
                <a-menu-item>
                  <a href="javascript:;" @click="handleExpand(false)">收起全部</a>
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </a-col>
        <a-col :span="16" style="text-align: right">
          <a-button class="mr-2" @click="closeDrawer">取消</a-button>
          <a-button class="mr-2" :loading="confirmLoading" @click="handleSubmit(false)">
            仅保存</a-button
          >
          <a-button type="primary" :loading="confirmLoading" @click="handleSubmit(true)"
            >保存并关闭</a-button
          >
        </a-col>
      </a-row>
    </template>
    <DatafilterDrawe @register="registerDatafilterDrawer" />
  </BasicDrawer>
</template>
<script lang="ts" setup>
  import { reactive, ref, Ref } from 'vue';
  import {
    Tree as ATree,
    Row as ARow,
    Col as ACol,
    Dropdown as ADropdown,
    Menu as AMenu,
  } from 'ant-design-vue';
  import { BasicDrawer, useDrawerInner, useDrawer } from '@mxpio/components';
  import { resList, permisList, savePermiss } from '@mxpio/api';
  import { useMessage } from '@mxpio/hooks/src/web/useMessage';
  import { FilterOutlined, DownOutlined } from '@ant-design/icons-vue';
  import DatafilterDrawe from './DatafilterDrawe.vue';

  defineOptions({ name: 'PermissDrawer' });
  const AMenuItem = AMenu.Item;

  let treeData = reactive([]);
  let selectedKeys = ref<string[] | { checked: string[]; halfChecked: string[] }>([]);
  let treeDataObj = reactive({}); // 全部树数据对象
  let defaultCheckedObj = reactive({}); //保存默认选中
  let roleId: Ref<string> = ref('');
  let confirmLoading = ref(false);
  let checkStrictly = ref(true);
  let expandedKeys = ref<string[]>([]);

  defineEmits(['success', 'register']);

  const [registerDatafilterDrawer, { openDrawer: openDatafilterDrawer }] = useDrawer();

  const { createMessage } = useMessage();

  const [registerDrawer, { closeDrawer }] = useDrawerInner(async ({ record }) => {
    // setDrawerProps({ confirmLoading: false });
    // 获取角色id
    roleId.value = record.id;
    confirmLoading = ref(false);
    // 获取资源列表
    const res = await resList();
    // 将资源列表转换为对象结构
    resListToObject(res);
    // 将资源列表转换为树形结构
    treeData = Object.assign(treeData, arrayToTree(res));
    // 获取权限
    getPermiss();
  });

  // 设置父子关联
  function handleCheckStrictly(strictly) {
    checkStrictly.value = strictly;
  }

  // 设置是否展开
  function handleExpand(expand) {
    if (expand) {
      Object.keys(treeDataObj).forEach((key) => {
        expandedKeys.value.push(key);
      });
    } else {
      expandedKeys.value = [];
    }
  }

  // 设置是否选中全部
  function handleSelect(all) {
    if (all) {
      Object.keys(treeDataObj).forEach((key) => {
        // selectedKeys.value.push(key);
        if (Array.isArray(selectedKeys.value)) {
          selectedKeys.value.push(key);
        } else {
          selectedKeys.value.checked.push(key);
        }
      });
    } else {
      selectedKeys.value = [];
    }
  }

  async function getPermiss() {
    const permiss = await permisList({ roleId: roleId.value });
    selectedKeys.value = [];
    permiss.forEach((item) => {
      if (Array.isArray(selectedKeys.value)) {
        selectedKeys.value.push(item.resourceId);
      } else {
        selectedKeys.value.checked.push(item.resourceId);
      }

      defaultCheckedObj[item.resourceId] = item;
    });
  }

  async function resListToObject(list) {
    list.forEach((item) => {
      treeDataObj[item.id] = item;
    });
  }

  // 将数组转换为树形结构
  const arrayToTree = (arr: any[]) => {
    if (!Array.isArray(arr) || arr.length < 1) return null;
    const addChildren = (node, dataList: any[]) => {
      const children = dataList
        .filter((item) => {
          if (item.parentId === node.id) {
            delete item.icon;
            return item;
          }
        })
        .map((item) => addChildren(item, dataList));
      return { ...node, children };
    };
    const tree: any[] = [];
    arr.forEach((item) => {
      if (!item.parentId) {
        delete item.icon;
        const root: { children: any[] } = item;
        tree.push(addChildren(root, arr));
      }
    });
    return tree;
  };

  // 获取当前弹窗分配的权限数据
  function getPermissData(selectedKeys) {
    const permissData: any[] = [];
    // 检查脏数据
    selectedKeys.forEach((key) => {
      const item = treeDataObj[key];
      const data = {
        resourceType: item.resourceType,
        crudType: defaultCheckedObj[item.id] ? 'UPDATE' : 'SAVE',
        resourceId: item.id,
        roleId: roleId.value,
        id: defaultCheckedObj[item.id] && defaultCheckedObj[item.id].id,
      };
      permissData.push(data);
    });
    Object.keys(defaultCheckedObj).forEach((key) => {
      if (!selectedKeys.includes(key)) {
        const item = defaultCheckedObj[key];
        const data = {
          resourceType: item.resourceType,
          crudType: 'DELETE',
          resourceId: item.resourceId,
          roleId: roleId.value,
          id: item.id,
        };
        permissData.push(data);
      }
    });
    return permissData;
  }

  async function handleSubmit(close: boolean) {
    try {
      let selectedKeys_: string[] = [];
      if (Array.isArray(selectedKeys.value)) {
        selectedKeys_ = selectedKeys.value;
      } else {
        selectedKeys_ = selectedKeys.value.checked;
      }
      if (selectedKeys_.length > 0) {
        confirmLoading = ref(true);
        await savePermiss(getPermissData(selectedKeys_));
        close ? closeDrawer() : getPermiss();
        createMessage.success('操作成功');
      } else {
        createMessage.success('请选择需要分配的权限');
      }
    } finally {
      confirmLoading = ref(false);
    }
  }

  function handleDatafilter(id: string) {
    let selectedKeys_: string[] = [];
    if (Array.isArray(selectedKeys.value)) {
      selectedKeys_ = selectedKeys.value;
    } else {
      selectedKeys_ = selectedKeys?.value?.checked;
    }
    if (!defaultCheckedObj[id] || !selectedKeys_.includes(id)) {
      createMessage.warning('请先选择并保存数据权限');
      return;
    }
    openDatafilterDrawer(true, {
      resId: id,
      roleId: roleId.value,
    });
  }
</script>
