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
          <template #expand_content="{ row }">
            <ExpandTable :dataSource="row.childList" />
          </template>
        </VxeBasicTable>
      </Col>
      <Col :span="12">
        <div class="p-4 bg-white">
          <a-tabs>
            <a-tab-pane key="item" tab="关联物料" forceRender>
              <WithInItem :currentRow="currentRow" @success="handleSuccess" />
            </a-tab-pane>
            <a-tab-pane key="itemGroups" tab="关联物料组" forceRender>
              <WithInItemGroup :currentRow="currentRow" @success="handleSuccess" />
            </a-tab-pane>
          </a-tabs>
        </div>
      </Col>
    </Row>
  </div>
</template>
<script lang="ts" setup>
  import { Row, Col } from 'ant-design-vue';
  import { VxeBasicTable } from '@mxpio/components';
  import { qtPageApi, qtLineListApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './byTemplate.data';
  import WithInItem from './WithInItem.vue';
  import WithInItemGroup from './WithInItemGroup.vue';
  import ExpandTable from './ExpandTable.vue';
  import { ref, nextTick } from 'vue';

  const componentName = 'QualityChainsByTemplate';
  defineOptions({ name: componentName });
  const currentRow = ref<Recordable>({});
  const { tableRef, gridOptions, handleSuccess } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: qtPageApi,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'code',
      },
      toolbarConfig: {
        buttons: [],
        tools: [],
        export: false,
        import: false,
      },
      expandConfig: { accordion: false, lazy: true, loadMethod: loadContentMethod },
    },
    module: 'erp',
    filters: {
      'bpmnStatus@EQ': '03',
    },
  });

  function onCurrentChange({ row }) {
    currentRow.value = row;
  }

  function onDataChange({ visibleData }) {
    const code = currentRow.value?.code;
    if (!code && visibleData.length) {
      nextTick(() => {
        tableRef.value?.setCurrentRow(visibleData[0]);
        currentRow.value = visibleData[0];
      });
      return;
    }
    currentRow.value = {};
    visibleData.forEach((item) => {
      if (item.code === code) {
        currentRow.value = item;
      }
    });
    nextTick(() => {
      tableRef.value?.setCurrentRow(currentRow.value);
    });
  }

  async function loadContentMethod({ row }) {
    const res = await qtLineListApi(row.code);
    row.childList = res;
    return res;
  }
</script>
