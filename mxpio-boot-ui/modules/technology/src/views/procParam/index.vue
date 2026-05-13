<template>
  <div class="m-3">
    <Row :gutter="10">
      <Col :span="12">
        <VxeBasicTable
          ref="tableRef"
          v-bind="gridOptions"
          @current-change="onCurrentChange"
          @data-change="onDataChange"
      /></Col>
      <Col :span="12">
        <Card size="small" title="工艺路线" :bodyStyle="{ height: '350px', 'padding-top': '18px' }">
          <ProcBomTable :currentRow="currentRow" @change="onProcBomChange" />
        </Card>
        <Card
          size="small"
          style="margin-top: 10px"
          title="工序参数"
          :bodyStyle="{ height: '340px', 'padding-top': '18px' }"
        >
          <ProcEqpTable :prodrout="prodrout" />
        </Card>
      </Col>
    </Row>
  </div>
</template>
<script lang="ts" setup>
  import { Row, Col, Card } from 'ant-design-vue';
  import { VxeBasicTable } from '@mxpio/components';
  import { procBomPage } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './procParam.data';
  import ProcBomTable from './ProcBomTable.vue';
  import ProcEqpTable from './ProcEqpTable.vue';
  import { ref } from 'vue';

  const componentName = 'ProcParamList';
  defineOptions({ name: componentName });
  const currentRow = ref<Recordable>({});
  const prodrout = ref({});
  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: procBomPage,
    vxeGridOptions: {
      height: 'auto',
      rowConfig: {
        keyField: 'routId',
      },
      toolbarConfig: {
        buttons: [],
        tools: [],
        export: false,
        import: false,
      },
    },
    filters: {
      'primaryRout@EQ': 1,
    },
    module: 'erp',
  });

  function onCurrentChange({ row }) {
    currentRow.value = row;
  }

  function onDataChange({ visibleData }) {
    const routId = currentRow.value?.routId;
    currentRow.value = {};
    visibleData.forEach((item) => {
      if (item.routId === routId) {
        currentRow.value = item;
      }
    });
    tableRef.value?.setCurrentRow(currentRow.value);
  }

  function onProcBomChange(row) {
    prodrout.value = row;
  }
</script>
