<template>
  <div class="m-3">
    <Row :gutter="10">
      <Col :span="6">
        <Card title="物料组" size="small" :bodyStyle="{ height: '700px', 'padding-top': '18px' }">
          <BasicTree
            ref="materialGroupRef"
            :field-names="fieldNames"
            :treeData="treeData"
            @select="treeSelect"
          />
        </Card>
      </Col>
      <Col :span="18">
        <Card title="关联方案" size="small" :bodyStyle="{ height: '700px', 'padding-top': '18px' }">
          <WithInTemplate :code="groupCode" datascope="2" />
        </Card>
      </Col>
    </Row>
  </div>
</template>
<script lang="ts" setup>
  import { Row, Col, Card } from 'ant-design-vue';
  import { BasicTree } from '@mxpio/components';
  import type { TreeProps } from '@mxpio/components';
  import WithInTemplate from '../byItem/WithInTemplate.vue';
  import { itemGroup } from '@mxpio/bizcommon';
  import { ref, onMounted } from 'vue';

  const componentName = 'QualityChainsByItemGroup';
  defineOptions({ name: componentName });
  const treeData = ref([]);
  const groupCode = ref('');
  const fieldNames: TreeProps['fieldNames'] = {
    children: 'children',
    title: 'groupName',
    key: 'groupCode',
  };

  function treeSelect(keys: string[]) {
    groupCode.value = keys[0];
  }

  async function loadTreeData() {
    const res = await itemGroup();
    if (res) {
      treeData.value = res;
    }
  }

  onMounted(async () => {
    loadTreeData();
  });
</script>
