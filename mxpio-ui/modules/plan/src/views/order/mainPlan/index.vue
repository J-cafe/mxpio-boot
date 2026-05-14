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
        >
          <template #action="{ row }">
            <TableAction
              :outside="true"
              :actions="[
                {
                  label: '编辑',
                  onClick: handleEdit.bind(null, row),
                  auth: auth.edit,
                  ifShow: row.bpmnStatus === '01',
                },
                {
                  label: '变更',
                  onClick: handleChange.bind(null, row),
                  auth: authConfig.change,
                  ifShow: row.bpmnStatus === '03',
                },
              ]"
              :dropDownActions="[
                {
                  label: '详情',
                  onClick: handleDetail.bind(null, row),
                },
                {
                  label: '提交',
                  onClick: handleSubmit.bind(null, row),
                  auth: authConfig.submit,
                  ifShow: row.bpmnStatus === '01',
                },
                {
                  label: '审核',
                  onClick: handleAudit.bind(null, row),
                  auth: authConfig.audit,
                  ifShow: row.bpmnStatus === '02',
                },
                {
                  label: '弃审',
                  onClick: handleAbandon.bind(null, row),
                  auth: authConfig.abandon,
                  ifShow: row.bpmnStatus === '02',
                },
                {
                  label: '生效',
                  onClick: handleEffect.bind(null, row),
                  auth: authConfig.effect,
                  ifShow: row.bpmnStatus === '03' && row.effectiveStatus === '0',
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
                  ifShow: row.bpmnStatus === '01',
                },
              ]"
            />
          </template>
        </VxeBasicTable>
      </Col>
      <Col :span="12">
        <Card title="计划明细" :bodyStyle="{ height: '700px', 'padding-top': '18px' }">
          <MainPlanLines :code="currentRow.code" />
        </Card>
      </Col>
    </Row>

    <mainPlanModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { Row, Col, Card } from 'ant-design-vue';
  import { TableAction, VxeBasicTable } from '@mxpio/components';
  import {
    mpsDeleteApi,
    mpsPageApi,
    mpsSubmitApi,
    mpsAuditApi,
    mpsAbandonApi,
    mpsEffectApi,
    mpsMrpApi,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './mainPlan.data';
  import MainPlanModal from './MainPlanModal.vue';
  import MainPlanLines from './MainPlanLines.vue';
  import { useMessage } from '@mxpio/hooks';

  const componentName = 'OrganizeNeedOrderList';
  defineOptions({ name: componentName });
  const { createMessage } = useMessage();
  const authConfig = {
    submit: `erp:${componentName}:submit`,
    audit: `erp:${componentName}:audit`,
    abandon: `erp:${componentName}:abandon`,
    effect: `erp:${componentName}:effect`,
    mrp: `erp:${componentName}:mrp`,
    change: `erp:${componentName}:change`,
  };

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
    checkboxChange,
  } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: mpsPageApi,
    deleteApi: mpsDeleteApi,
    deleteBefore: deleteBefore,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'code',
      },
      toolbarConfig: {
        import: false,
      },
    },
    appendButtons: [
      {
        content: 'MRP',
        buttonRender: {
          name: 'AButton',
          props: {
            type: 'primary',
            preIcon: 'mdi:source-pull',
          },
          attrs: {
            class: 'ml-2',
          },
          events: {
            click: handleMrp,
          },
        },
        // visible: hasPermission(authConfig.mrp),
      },
    ],
    module: 'erp',
  });

  async function deleteBefore(params: Recordable | Recordable[]) {
    try {
      const rows = Array.isArray(params) ? params : [params];
      // 修复bug 目前返回的数组内容为undefined
      const codes: string[] = [];
      rows.forEach((row: Recordable) => {
        if (row.bpmnStatus !== '01') {
          codes.push(row.code);
        }
      });
      if (codes.length > 0) {
        createMessage.error(`单据:${codes.join(',')} 状态不允许删除`);
        return Promise.reject(`单据:${codes.join(',')} 状态不允许删除`);
      }
      return Promise.resolve();
    } catch (error) {
      console.log(error);
    }
  }

  async function handleSubmit(row: Recordable) {
    try {
      await mpsSubmitApi(row.code);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleAudit(row: Recordable) {
    try {
      await mpsAuditApi(row.code);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleAbandon(row: Recordable) {
    try {
      await mpsAbandonApi(row.code);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleEffect(row: Recordable) {
    try {
      await mpsEffectApi(row.code);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleChange(row: Recordable) {
    try {
      openModal(true, {
        record: row,
        isUpdate: true,
        isChange: true,
      });
    } catch (error) {
      console.log(error);
    }
  }

  async function handleMrp() {
    try {
      createMessage.loading('MRP处理中...');
      const res = await mpsMrpApi();
      tableRef.value?.commitProxy('query');
      createMessage.destroy();
      createMessage.success(res?.message || 'MRP处理完成');
    } catch (error) {
      console.log(error);
    }
  }

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
