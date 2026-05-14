<template>
  <div class="m-3">
    <Row :gutter="10">
      <Col :span="12">
        <VxeBasicTable ref="tableRef" v-bind="gridOptions" @current-change="onCurrentChange">
          <template #action="{ row }">
            <TableAction
              :outside="true"
              :actions="[
                {
                  label: '历史版本',
                  onClick: handleHistory.bind(null, row),
                },
              ]"
            />
          </template>
        </VxeBasicTable>
      </Col>
      <Col :span="12">
        <Card title="物料BOM" :bodyStyle="{ height: '700px', 'padding-top': '18px' }">
          <BomStoreyTree
            ref="bomStoreyTree"
            :parentCode="currentRow.parentCode"
            :parentData="currentRow"
          />
        </Card>
      </Col>
    </Row>

    <BomHistoryModal width="80%" @register="registerModal" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { Row, Col, Card } from 'ant-design-vue';
  import { bomPage } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './bomStorey.data';
  import BomHistoryModal from '../BomList/BomHistoryModal.vue';
  import BomStoreyTree from './BomStoreyTree.vue';
  import { ref } from 'vue';

  const componentName = 'BomStoreyList';
  defineOptions({ name: componentName });
  const currentRow = ref<Recordable>({});
  const [registerModal, { openModal }] = useModal();

  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: bomPage,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'parentCode',
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

  function handleHistory(record) {
    openModal(true, {
      record,
      disabled: true,
    });
  }
</script>
