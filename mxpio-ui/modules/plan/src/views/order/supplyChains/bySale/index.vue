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
        <Card title="已冲销" :bodyStyle="{ height: '700px', padding: '0px' }">
          <WithInDetail :currentRow="currentRow" @success="handleSuccess" />
        </Card>
      </Col>
    </Row>
  </div>
</template>
<script lang="ts" setup>
  import { Row, Col, Card } from 'ant-design-vue';
  import { VxeBasicTable } from '@mxpio/components';
  import { solineOfPage } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './bySale.data';
  import WithInDetail from './WithInForecastDetail.vue';
  import { ref } from 'vue';

  const componentName = 'SupplyChainsBySale';
  defineOptions({ name: componentName });
  const currentRow = ref<Recordable>({});
  const { tableRef, gridOptions, checkboxChange, handleSuccess } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: solineOfPage,
    vxeGridOptions: {
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
    const bizNo = currentRow.value?.bizNo;
    const lineNo = currentRow.value?.lineNo;
    currentRow.value = {};
    visibleData.forEach((item) => {
      if (item.bizNo === bizNo && item.lineNo === lineNo) {
        currentRow.value = item;
      }
    });
    tableRef.value?.setCurrentRow(currentRow.value);
  }
</script>
