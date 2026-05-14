<template>
  <div>
    <div class="button-warp">
      <a-button :icon="h(CaretRightOutlined)" class="mr-2" size="small" @click="handelExecute"
        >执行全部SQL</a-button
      >
      <a-button
        :icon="h(PlayCircleOutlined)"
        class="mr-2"
        size="small"
        @click="handelExecuteSelect"
      >
        执行选中SQL
      </a-button>
      <a-button :icon="h(DeleteOutlined)" size="small" @click="handelClear"> 清空SQL </a-button>
    </div>
    <CodeEditor
      style="height: 300px"
      v-model:value="codeValue"
      class="m-2"
      :bordered="true"
      :mode="modeValue"
      ref="codeMirror"
    />
    <VxeBasicTable class="!px-0 !py-0" ref="tableRef" v-bind="gridOptions" />
  </div>
</template>
<script lang="ts" setup>
  import { VxeBasicTable, CodeEditor, MODE } from '@mxpio/components';
  import type { VxeGridPropTypes, BasicVxeTableProps, VxeGridInstance } from '@mxpio/components';
  import { dataList } from '../../api/dbconsole';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import { reactive, ref, defineProps, h } from 'vue';
  import { CaretRightOutlined, PlayCircleOutlined, DeleteOutlined } from '@ant-design/icons-vue';

  defineOptions({ name: 'SQL' });

  const props = defineProps({
    table: {
      type: Object,
      default: () => ({}),
    },
  });

  const modeValue = ref<MODE>(MODE.SQL);
  const codeValue = ref('');
  const codeMirror = ref<{ getSelection: () => void } | null>(null);

  const tableRef = ref<VxeGridInstance>();
  let columns = reactive<VxeGridPropTypes.Columns>([]);
  let other = reactive({});

  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'SQL',
    columns: [],
    toolbarConfig: {
      buttons: [],
      tools: [
        {
          toolRender: {
            name: 'ExportButton',
            attrs: {
              class: 'ml-2',
            },
            props: {
              export: 'sys:SQL:export',
            },
          },
        },
      ],
    },
    proxyConfig: {
      autoLoad: false,
      ajax: {
        query: async ({ page, form, sorts }: VxeGridPropTypes.ProxyAjaxQueryParams<any>) => {
          return loadData({ page, form, sorts });
        },
      },
    },
    height: 400,
  });

  async function loadData({ page, form, sorts }: VxeTableQueryParams) {
    const params = getVxeTableQueryParams({
      page,
      form,
      sorts,
      other: other,
    });
    const res = await dataList(props.table?.dbInfoId, params);
    columns = [
      {
        title: '序号',
        type: 'seq',
        fixed: 'left',
        width: '50',
        align: 'center',
      },
    ];
    Object.keys(res.content[0]).forEach((key: string) => {
      columns.push({
        title: key,
        field: key,
        width: 100,
      });
    });
    tableRef.value?.loadColumn(columns);
    return res;
  }

  // 执行全部
  function handelExecute() {
    other = {
      sql: codeValue.value,
    };
    tableRef.value?.commitProxy('query');
  }

  // 执行选中数据
  function handelExecuteSelect() {
    other = {
      sql: codeMirror.value?.getSelection(),
    };
    tableRef.value?.commitProxy('query');
  }

  // 清空
  function handelClear() {
    codeValue.value = '';
    tableRef.value?.reloadData([]);
    tableRef.value?.loadColumn([]);
  }
</script>
