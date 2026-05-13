<template>
  <div class="m-3">
    <row :gutter="10">
      <Col :span="5">
        <Card size="small" ref="treeCard" title="设备分类" :bodyStyle="{ height: height + 'px' }">
          <BasicTree
            v-model:selectedKeys="selectedKeys"
            ref="materialGroupRef"
            :field-names="fieldNames"
            :treeData="treeData"
            @select="treeSelect"
          />
        </Card>
      </Col>
      <Col :span="19">
        <div class="bg-white">
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
                    label: '编辑',
                    onClick: handleEdit.bind(null, row),
                    auth: auth.edit,
                  },
                ]"
                :dropDownActions="[
                  {
                    label: '详情',
                    onClick: handleDetail.bind(null, row),
                  },
                  {
                    label: '复制',
                    onClick: handleCopy.bind(null, row),
                    auth: auth.add,
                  },
                  {
                    label: '删除',
                    color: 'error',
                    popConfirm: {
                      title: '是否确认删除',
                      placement: 'left',
                      confirm: handleDelete.bind(null, row),
                    },
                    auth: auth.delete,
                  },
                ]"
              />
            </template>
          </VxeBasicTable>
          <a-tabs class="px-6">
            <a-tab-pane key="attr" tab="设备档案">
              <EqpInfoAttrTable :basicsCode="currentRow.basicsCode" />
            </a-tab-pane>
            <a-tab-pane key="sop" tab="设备SOP">
              <EqpBasicsSopTable :basicsCode="currentRow.basicsCode" />
            </a-tab-pane>
            <a-tab-pane key="bom" tab="设备备件">
              <EqpInfoBomTable :eqpCode="currentRow.eqpCode" />
            </a-tab-pane>
            <a-tab-pane key="remark" tab="重要备注">
              <EqpBasicsRemarkTable :basicsCode="currentRow.basicsCode" />
            </a-tab-pane>
            <a-tab-pane key="sub" tab="设备子项">
              <EqpBasicsSubTable :basicsCode="currentRow.basicsCode" />
            </a-tab-pane>
          </a-tabs>
        </div>
      </Col>
    </row>
    <EqqpInfoModal width="80%" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { Card, Row, Col } from 'ant-design-vue';
  import { ref, onMounted } from 'vue';
  import { TableAction, VxeBasicTable, BasicTree } from '@mxpio/components';
  import {
    deleteEqpBasicsApi,
    eqpBasicsPageApi,
    resConfig,
    eqpCategoryTreeApi,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './eqpBasics.data';
  import EqqpInfoModal from './EqpBasicsModal.vue';
  import EqpInfoAttrTable from './EqpInfoAttrTable.vue';
  import EqpInfoBomTable from './EqpInfoBomTable.vue';
  import EqpBasicsSopTable from './EqpBasicsSopTable.vue';
  import EqpBasicsRemarkTable from './EqpBasicsRemarkTable.vue';
  import EqpBasicsSubTable from './EqpBasicsSubTable.vue';

  const componentName = 'EqpBasicsList';
  defineOptions({ name: componentName });

  const treeCard = ref();
  const selectedKeys = ref([]);
  const fieldNames = {
    title: 'name',
    key: 'code',
    children: 'children',
  };
  const treeData = ref([]);
  const height = ref(0);
  const currentRow = ref<Recordable>({});
  const {
    tableRef,
    auth,
    gridOptions,
    registerModal,
    openModal,
    handleEdit,
    handleDelete,
    handleDetail,
    handleSuccess,
  } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: eqpBasicsPageApi,
    deleteApi: deleteEqpBasicsApi,
    configApi: () => resConfig('EquipmentEqpBasics'),
    vxeGridOptions: {
      tableClass: 'pb-0',
      minHeight: 450,
      height: 'auto',
      rowConfig: {
        keyField: 'basicsCode',
      },
      radioConfig: {
        trigger: 'row',
      },
    },
    module: 'erp',
    appendButtons: [
      {
        content: '打印',
        buttonRender: {
          name: 'AButton',
          props: {
            type: 'primary',
            preIcon: 'ant-design:printer-outlined',
          },
          attrs: {
            class: 'ml-2',
          },
          events: {
            // click: () => handleAllSync(),
          },
        },
      },
    ],
    filters: () => {
      return {
        eqpTypeId: selectedKeys.value[0],
      };
    },
  });

  const handleCopy = (row: Recordable) => {
    openModal(true, {
      record: row,
      isCopy: true,
      isUpdate: false,
    });
  };

  function onCurrentChange({ row }) {
    currentRow.value = row;
  }

  function onDataChange({ visibleData }) {
    currentRow.value = visibleData[0] || {};
    tableRef.value?.setCurrentRow(visibleData[0]);
    tableRef.value?.setRadioRow(visibleData[0]);
  }

  function treeSelect(selectedKeys) {
    selectedKeys.value = selectedKeys;
    tableRef.value?.commitProxy('query');
  }

  function getMaxHeight() {
    // 获取页面高度
    if (treeCard.value) {
      const clientHeight = document.documentElement.clientHeight;
      const tableTop = treeCard.value.$el.getBoundingClientRect().top;
      height.value = clientHeight - tableTop - 60;
    }
  }

  onMounted(() => {
    eqpCategoryTreeApi().then((res) => {
      treeData.value = res || [];
    });
    getMaxHeight();
  });
</script>
