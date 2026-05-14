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
        <Card title="关联供应商" :bodyStyle="{ height: '700px', 'padding-top': '18px' }">
          <WithInSupplier :currentRow="currentRow" @success="handleSuccess" />
        </Card>
      </Col>
    </Row>
  </div>
</template>
<script lang="ts" setup>
  import { Row, Col, Card } from 'ant-design-vue';
  import { VxeBasicTable } from '@mxpio/components';
  import { supplychainItemPage } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import Criteria from '@mxpio/utils/src/criteria';
  import { OperatorEnum } from '@mxpio/enums';
  import { columns, searchFormSchema } from './byItem.data';
  import WithInSupplier from './WithInSupplier.vue';
  import { ref } from 'vue';
  import { isNil, omitBy } from '@mxpio/utils';

  const componentName = 'SupplyChainsByItem';
  defineOptions({ name: componentName });
  const currentRow = ref<Recordable>({});
  const { tableRef, gridOptions, checkboxChange, handleSuccess } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: supplychainItemPage,
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
    loadBefore: ({ page, form, filters }) => {
      const queryParam = new Criteria();
      const query = { ...form };
      queryParam.addCriterions(
        Object.assign({}, omitBy(query, isNil), omitBy(filters, isNil)),
        OperatorEnum.LIKE,
      );
      queryParam
        .or()
        .addCriterion('outsourceAble', OperatorEnum.EQ, '1')
        .addCriterion('itemSource', OperatorEnum.EQ, '2')
        .end();
      var param: any = Object.assign({}, { criteria: queryParam.getEncode() });
      param.pageNo = page?.currentPage;
      param.pageSize = page?.pageSize;
      return param;
    },
  });

  function onCurrentChange({ row }) {
    currentRow.value = row;
  }

  function onDataChange({ visibleData }) {
    const itemCode = currentRow.value?.itemCode;
    currentRow.value = {};
    visibleData.forEach((item) => {
      if (item.itemCode === itemCode) {
        currentRow.value = item;
      }
    });
    tableRef.value?.setCurrentRow(currentRow.value);
  }
</script>
