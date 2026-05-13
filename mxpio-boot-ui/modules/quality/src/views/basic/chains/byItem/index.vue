<template>
  <div class="m-3">
    <Row :gutter="10">
      <Col :span="12">
        <VxeBasicTable
          ref="tableRef"
          v-bind="gridOptions"
          @current-change="onCurrentChange"
          @data-change="onDataChange"
        />
      </Col>
      <Col :span="12">
        <Card title="关联方案" :bodyStyle="{ height: '700px', 'padding-top': '18px' }">
          <WithInTemplate :code="currentRow.itemCode" />
        </Card>
      </Col>
    </Row>
  </div>
</template>
<script lang="ts" setup>
  import { Row, Col, Card } from 'ant-design-vue';
  import { VxeBasicTable } from '@mxpio/components';
  import { itemList } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { itemColumns, searchFormItemSchema } from './byItem.data';
  import WithInTemplate from './WithInTemplate.vue';

  import { ref, nextTick } from 'vue';

  const componentName = 'QualityChainsByItem';
  defineOptions({ name: componentName });
  const currentRow = ref<Recordable>({});
  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    columns: itemColumns,
    searchFormSchema: searchFormItemSchema,
    pageApi: itemList,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'itemCode',
      },
      toolbarConfig: {
        buttons: [],
        tools: [],
        export: false,
        import: false,
      },
    },
    module: 'erp',
  });

  function onCurrentChange({ row }) {
    currentRow.value = row;
  }

  function onDataChange({ visibleData }) {
    const itemCode = currentRow.value?.itemCode;
    if (!itemCode && visibleData.length) {
      nextTick(() => {
        tableRef.value?.setCurrentRow(visibleData[0]);
        currentRow.value = visibleData[0];
      });
      return;
    }
    currentRow.value = {};
    visibleData.forEach((item) => {
      if (item.itemCode === itemCode) {
        currentRow.value = item;
      }
    });
    nextTick(() => {
      tableRef.value?.setCurrentRow(currentRow.value);
    });
  }
</script>
