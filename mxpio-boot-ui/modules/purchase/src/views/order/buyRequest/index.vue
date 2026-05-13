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
              ifShow: row.bpmnStatus === '01',
              auth: authConfig.submit,
            },
            {
              label: '审核',
              onClick: handleAudit.bind(null, row),
              ifShow: row.bpmnStatus === '02',
              auth: authConfig.audit,
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
      <template #expand_content="{ row }">
        <ExpandTable :dataSource="row.childList" />
      </template>
    </VxeBasicTable>
    <BuyRequestModal @register="registerModal" @success="handleSuccess" />
    <BuyRequestAuditModal :bizNo="bizNo" @register="registerAuditModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import {
    buyRequestPage,
    deleteBuyRequest,
    buyRequestConfig,
    brLineList,
    submitBuyRequest,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './buyRequest.data';
  import BuyRequestModal from './BuyRequestModal.vue';
  import BuyRequestAuditModal from './BuyRequestAuditModal.vue';
  import ExpandTable from './ExpandTable.vue';
  import { useMessage } from '@mxpio/hooks';
  import { ref } from 'vue';

  const componentName = 'BuyRequestList';
  defineOptions({ name: componentName });
  const bizNo = ref('');
  const [registerAuditModal, { openModal: openAuditModal }] = useModal();
  const { createMessage } = useMessage();

  const authConfig = {
    submit: 'erp:BuyRequestList:submit',
    audit: 'erp:BuyRequestList:audit',
  };

  const {
    tableRef,
    auth,
    gridOptions,
    registerModal,
    handleEdit,
    handleDelete,
    handleDetail,
    handleSuccess,
    checkboxChange,
  } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: buyRequestPage,
    deleteApi: deleteBuyRequest,
    configApi: buyRequestConfig,
    deleteBefore: deleteBefore,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'bizNo',
      },
      expandConfig: { accordion: false, lazy: true, loadMethod: loadContentMethod },
    },
    module: 'erp',
  });

  async function deleteBefore(params: Recordable | Recordable[]) {
    try {
      const rows = Array.isArray(params) ? params : [params];
      // 修复bug 目前返回的数组内容为undefined
      const bizNos: string[] = [];
      rows.forEach((row: Recordable) => {
        if (row.bpmnStatus !== '01') {
          bizNos.push(row.bizNo);
        }
      });
      if (bizNos.length > 0) {
        createMessage.error(`单据:${bizNos.join(',')} 状态不允许删除`);
        return Promise.reject(`单据:${bizNos.join(',')} 状态不允许删除`);
      }
      return Promise.resolve();
    } catch (error) {
      console.log(error);
    }
  }

  async function handleSubmit(row: Recordable) {
    try {
      await submitBuyRequest(row.bizNo);
      createMessage.success('操作成功');
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleAudit(row: Recordable) {
    try {
      bizNo.value = row.bizNo;
      openAuditModal(true, {
        bizNo: row.bizNo,
      });
    } catch (error) {
      console.log(error);
    }
  }

  async function loadContentMethod({ row }: { row: Recordable }) {
    const res = await brLineList(row.bizNo);
    row.childList = res;
    return res;
  }
</script>
