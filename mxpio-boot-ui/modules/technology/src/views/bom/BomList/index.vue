<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: row.version ? '变更' : '维护',
              onClick: handleEdit.bind(null, row),
              auth: 'erp:BomList:edit',
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '草稿',
              onClick: handleTmpEdit.bind(null, row),
              auth: 'erp:BomList:edit',
            },
            {
              label: '复制',
              onClick: handleCopy.bind(null, row),
              auth: 'erp:BomList:edit',
            },
            {
              label: '历史版本',
              onClick: handleHistory.bind(null, row),
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <BomModal width="80%" @register="registerModal" @success="handleSuccess" />
    <BomEndlessModal width="900px" @register="registerEndlessModal" />
    <BomHistoryModal width="80%" @register="registerHistoryModal" />
  </div>
</template>
<script lang="ts" setup>
  import { onBeforeMount, ref, reactive } from 'vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import type { VxeGridPropTypes, VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';
  import { bomPage, bomConfig, getTmpBom, exportUnBomList } from '@mxpio/bizcommon';
  import { columns, searchFormSchema } from './bom.data';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import { useProfile, useCommon } from '@mxpio/hooks';
  import BomModal from './BomModal.vue';
  import BomEndlessModal from './BomEndlessModal.vue';
  import BomHistoryModal from './BomHistoryModal.vue';

  defineOptions({ name: 'BomList' });
  const [registerModal, { openModal }] = useModal();
  const [registerEndlessModal, { openModal: openEndlessModal }] = useModal();
  const [registerHistoryModal, { openModal: openHistoryModal }] = useModal();

  const { downloadByFileNo } = useCommon();
  const { restoreStore, updateStore } = useProfile();
  const importCode = ref();
  const tableRef = ref<VxeGridInstance>();
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'BomList',
    columns: columns,
    toolbarConfig: {
      buttons: [
        {
          content: '死循环校验',
          buttonRender: {
            name: 'AButton',
            props: {
              type: 'primary',
              preIcon: 'mdi:settings',
            },
            attrs: {
              class: 'ml-2',
            },
            events: {
              click: () => {
                handleEndLess();
              },
            },
          },
        },
        {
          content: '未配置BOM物料明细',
          buttonRender: {
            name: 'AButton',
            props: {
              type: 'primary',
              preIcon: 'mdi:database-arrow-down',
            },
            attrs: {
              class: 'ml-2',
            },
            events: {
              click: () => {
                handleExportUnBom();
              },
            },
          },
        },
      ],
      tools: [
        {
          toolRender: {
            name: 'ExportButton',
            attrs: {
              class: 'ml-2',
            },
            props: {
              export: 'erp:BomList:export',
            },
          },
        },
      ],
    },
    formConfig: {
      enabled: true,
      items: searchFormSchema,
    },
    customConfig: {
      storage: {
        visible: true,
        resizable: true,
        sort: true,
        fixed: true,
      }, // 启用自定义列状态保存功能
      restoreStore: restoreStore,
      updateStore: updateStore,
    },
    minHeight: 700,
    proxyConfig: {
      ajax: {
        query: async ({ page, form, sorts }: VxeGridPropTypes.ProxyAjaxQueryParams<any>) => {
          return loadData({ page, form, sorts });
        },
      },
    },
  });

  async function loadData({ page, form, sorts }: VxeTableQueryParams) {
    const params = getVxeTableQueryParams({ page, form, sorts });
    const res = await bomPage(params);
    return res;
  }

  async function handleEdit(record: Recordable) {
    const res = await getTmpBom(record.parentCode);
    if (res && res.bomId) {
      //已有编辑中草稿
      openModal(true, {
        record,
        isUpdate: true,
        isTmp: true,
      });
      return;
    }
    openModal(true, {
      record,
      isUpdate: record.version ? true : false, // 判断是否第一次维护
    });
  }

  function handleTmpEdit(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: true,
      isTmp: true,
    });
  }

  function handleDetail(record: Recordable) {
    openModal(true, {
      record,
      disabled: true,
      isUpdate: true,
    });
  }

  function handleCopy(record: Recordable) {
    openModal(true, {
      record,
      isCopy: true,
      disabled: false,
      isUpdate: true,
    });
  }

  function handleSuccess() {
    tableRef.value?.commitProxy('query');
  }

  async function handleExportUnBom() {
    try {
      const res = await exportUnBomList();
      downloadByFileNo(res.fileNo, res.fileName);
    } catch (error) {
      console.log(error);
    }
  }

  function handleEndLess() {
    openEndlessModal();
  }

  function handleHistory(record: Recordable) {
    openHistoryModal(true, {
      record,
    });
  }

  onBeforeMount(async () => {
    const res = await bomConfig();
    if (res) {
      importCode.value = res.importTemplate;
    }
  });
</script>
