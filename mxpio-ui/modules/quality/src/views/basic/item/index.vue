<template>
  <div class="m-3">
    <VxeBasicTable
      ref="tableRef"
      v-bind="gridOptions"
      @checkbox-change="checkboxChange"
      @checkbox-all="checkboxChange"
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
              ifShow: row.bpmnStatus === '02' || row.bpmnStatus === '03',
            },
            {
              label: '删除',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                placement: 'left',
                confirm: handleDelete.bind(null, row),
              },
              ifShow: row.bpmnStatus === '01',
              auth: auth.delete,
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <QualityItemModal width="900px" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable } from '@mxpio/components';
  import {
    deleteIiApi,
    iiPageApi,
    iiConfigApi,
    submitIiApi,
    auditIiApi,
    abandonIiApi,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './item.data';
  import QualityItemModal from './ItemModal.vue';

  const componentName = 'QualityItemList';
  const authConfig = {
    submit: `erp:${componentName}:submit`,
    audit: `erp:${componentName}:audit`,
    abandon: `erp:${componentName}:abandon`,
  };

  defineOptions({ name: componentName });
  const {
    tableRef,
    gridOptions,
    registerModal,
    checkboxChange,
    handleEdit,
    handleDelete,
    handleDetail,
    handleSuccess,
    auth,
  } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: iiPageApi,
    deleteApi: deleteIiApi,
    configApi: iiConfigApi,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'code',
      },
    },
  });

  async function handleSubmit(row: Recordable) {
    try {
      await submitIiApi(row.code);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleAudit(row: Recordable) {
    try {
      await auditIiApi(row.code);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleAbandon(row: Recordable) {
    try {
      await abandonIiApi(row.code);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }
</script>
