<template>
  <div class="m-3">
    <Row :gutter="10">
      <Col :span="12">
        <VxeBasicTable
          ref="tableRef"
          v-bind="gridOptions"
          @current-change="onCurrentChange"
          @data-change="onDataChange"
        >
          <template #action="{ row }">
            <TableAction
              :outside="true"
              :actions="[
                {
                  label: '历史记录',
                  onClick: () => openModal(true, { record: row }),
                },
              ]"
            />
          </template>
        </VxeBasicTable>
      </Col>
      <Col :span="12">
        <Card size="small" title="工艺路线" :bodyStyle="{ height: '350px', 'padding-top': '18px' }">
          <ProcBomTable :currentRow="currentRow" @change="onProcBomChange" />
        </Card>
        <Card
          size="small"
          style="margin-top: 10px"
          title="工艺图纸"
          :bodyStyle="{ height: '340px', 'padding-top': '18px' }"
        >
          <ProcDrawingfileTable :rout="currentRow" :prodrout="prodrout" />
        </Card>
      </Col>
    </Row>
    <ProcBomHistoryModal ref="procBomHistoryModalRef" @register="registerModal" />
  </div>
</template>
<script lang="ts" setup>
  import { Row, Col, Card } from 'ant-design-vue';
  import { VxeBasicTable, useModal, TableAction } from '@mxpio/components';
  import { procBomPage } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './procDrawingfile.data';
  import ProcBomTable from './ProcBomTable.vue';
  import ProcDrawingfileTable from './ProcDrawingfileTable.vue';
  import ProcBomHistoryModal from './ProcBomHistoryModal.vue';
  import { ref } from 'vue';

  const componentName = 'ProcDrawingfileList';
  defineOptions({ name: componentName });
  const currentRow = ref<Recordable>({});
  const prodrout = ref({});
  const [registerModal, { openModal }] = useModal();
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
      radioConfig: {
        trigger: 'row',
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
