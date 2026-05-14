<template>
  <div class="m-3">
    <Row :gutter="10">
      <Col :span="12">
        <VxeBasicTable ref="tableRef" v-bind="gridOptions" @current-change="onCurrentChange" />
      </Col>
      <Col :span="12">
        <Card title="销售明细" :bodyStyle="{ height: '700px', 'padding-top': '18px' }">
          <SolineTable
            ref="bomStoreyTree"
            :itemCode="currentRow.itemCode"
            :parentData="currentRow"
          />
        </Card>
      </Col>
    </Row>
  </div>
</template>
<script lang="ts" setup>
  import { VxeBasicTable } from '@mxpio/components';
  import { Row, Col, Card } from 'ant-design-vue';
  import { flowSumPage } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './byInv.data';
  import SolineTable from './SolineTable.vue';
  import { ref } from 'vue';

  const componentName = 'SaleDeliveryByInv';
  defineOptions({ name: componentName });
  const currentRow = ref<Recordable>({});

  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: flowSumPage,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'id',
      },
      toolbarConfig: {
        buttons: [],
        tools: [],
      },
    },
    module: 'erp',
  });

  function onCurrentChange({ row }) {
    currentRow.value = row;
  }
</script>
