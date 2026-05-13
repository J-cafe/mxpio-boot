<!-- 业务单使用工作流示例 -->
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
              ifShow:
                row.bpmnStatus === '01' && row.closeStatus !== 'closed' && row.orderStatus === '10',
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '提交', // 提交工作流
              onClick: handleSubmit.bind(null, row),
              auth: authConfig.submit,
              ifShow: row.bpmnStatus === '01' && row.closeStatus !== 'closed',
            },
            {
              label: '办理进度', // 查看审批办理进度
              onClick: handleBpmn.bind(null, row),
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
    <SalesOrderModal @register="registerModal" @success="handleSuccess" />
    <!-- 查看审批办理进度弹窗 type类型为view，不显示办理相关按钮，hasCancel为false 不显示撤回按钮 -->
    <BPMNTaskModal :width="1200" type="view" :hasCancel="false" @register="registerBpmnModal" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import {
    deleteSalesOrder,
    salesOrderPage,
    salesOrderConfig,
    saveSalesOrder,
    soLineList,
    startBpmn,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './salesOrder.data';
  import SalesOrderModal from './SalesOrderModal.vue';
  import ExpandTable from './ExpandTable.vue';
  import { useMessage } from '@mxpio/hooks';
  import { CuryTypeEnum } from '@mxpio/enums';
  import BPMNTaskModal from '../../views/task/BPMNTaskModal.vue';

  const componentName = 'SalesOrderList';
  defineOptions({ name: componentName });
  const { createMessage } = useMessage();
  const [registerBpmnModal, { openModal }] = useModal();
  const authConfig = {
    submit: `erp:${componentName}:submit`,
    audit: `erp:${componentName}:audit`,
    abandon: `erp:${componentName}:abandon`,
    close: `erp:${componentName}:close`,
    open: `erp:${componentName}:open`,
    finish: `erp:${componentName}:finish`,
    clear: `erp:${componentName}:clear`,
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
    pageApi: salesOrderPage,
    deleteApi: deleteSalesOrder,
    configApi: salesOrderConfig,
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
        if (row.bpmnStatus !== '01' || row.orderStatus !== '10') {
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

  // 示例：工作流程审核发起
  async function handleSubmit(row: Recordable) {
    try {
      // 获取销售订单行数据
      const res = await soLineList(row.bizNo);
      // 发起工作流程，LC005为销售订单审核流程
      const processInstanceId = await startBpmn('LC005', {
        businessKey: row.bizNo, // 业务单主键
        ...row, // 业务单数据，后续审批展示使用
        salesOrderLines: res, // 销售订单行数据，后续审批展示使用，salesOrderLines键值与业务单salesOrderLines保持一致
      });
      // 修改状态为已提交，且保存流程实例ID到业务单,processInstanceId
      row.bpmnStatus = '02'; // 更新行数据状态
      row.processInstanceId = processInstanceId; // 更新行数据审批实例id
      row.crudType = CuryTypeEnum.UPDATE; // 更新行数据脏数据状态
      await saveSalesOrder(row);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  // 示例：查看工作流程办理进度
  async function handleBpmn() {
    try {
      openModal(true, {
        isUpdate: false,
        record: {
          processInstanceId: '05e208ea-1163-11f1-a8e5-a8934a88565a', // 对应发起流程实例ID,后续替换为row.bpmnProcessInstanceId
        },
      });
    } catch (error) {
      console.log(error);
    }
  }

  async function loadContentMethod({ row }: { row: Recordable }) {
    const res = await soLineList(row.bizNo);
    row.childList = res;
    return res;
  }
</script>
