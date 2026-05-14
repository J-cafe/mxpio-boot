<template>
  <div class="m-3">
    <Row :gutter="10">
      <Col :span="12">
        <VxeBasicTable
          ref="tableRef"
          v-bind="gridOptions"
          @checkbox-change="checkboxChange"
          @checkbox-all="checkboxChange"
          @current-change="onCurrentChange"
          @data-change="onDataChange"
      /></Col>
      <Col :span="12">
        <Card title="关联物料" :bodyStyle="{ height: '700px', 'padding-top': '18px' }">
          <WithInItem :currentRow="currentRow" @success="handleSuccess" />
        </Card>
      </Col>
    </Row>
  </div>
</template>
<script lang="ts" setup>
  import { Row, Col, Card } from 'ant-design-vue';
  import { VxeBasicTable } from '@mxpio/components';
  import { supplychainSupplyPage } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './bySupply.data';
  import WithInItem from './WithInItem.vue';
  import { ref } from 'vue';

  const componentName = 'SupplyChainsBySupply';
  defineOptions({ name: componentName });
  const currentRow = ref<Recordable>({});
  const { tableRef, gridOptions, checkboxChange, handleSuccess } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: supplychainSupplyPage,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'pnCode',
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
    const pnCode = currentRow.value?.pnCode;
    currentRow.value = {};
    visibleData.forEach((item) => {
      if (item.pnCode === pnCode) {
        currentRow.value = item;
      }
    });
    tableRef.value?.setCurrentRow(currentRow.value);
  }
</script>
