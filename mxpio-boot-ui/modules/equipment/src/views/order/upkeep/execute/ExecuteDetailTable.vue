<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions" @edit-closed="handleEditClosed">
    <template #pic="{ row }">
      <a v-if="row.pic" @click="handleDownload(row)">下载</a>
    </template>
    <template #actualVaule="{ row }">
      <div v-show="row.isRecord == 1 || row.isRecord === '1'">
        <a-select
          allowClear
          v-if="row.chooseEnable == 1 || row.chooseEnable === '1'"
          style="min-width: 120px"
          :value="row.actualVaule"
          placeholder="请选择"
          :disabled="isDisabled"
          @focus="listDetailParams(row.id)"
          @change="(value: string) => handleActualValueChange(row, value)"
        >
          <a-select-option
            :value="item.param"
            v-for="(item, index) in actualVauleOptions[row.id] || []"
            :key="index"
          >
            {{ item.param }}
          </a-select-option>
        </a-select>
        <a-input
          v-else-if="row.contentType == 1 || row.contentType === '1'"
          type="number"
          v-model:value="row.actualVaule"
          :disabled="isDisabled"
          @blur="avInputChange(row)"
          placeholder="请输入"
        />
        <a-input
          v-else
          v-model:value="row.actualVaule"
          :disabled="isDisabled"
          @blur="avInputChange(row)"
          placeholder="请输入"
        />
      </div>
    </template>
  </VxeBasicTable>
</template>
<script lang="ts" setup>
  import { ref, computed, nextTick, reactive } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance } from '@mxpio/components';
  import { eqpUpkeepDetailListApi, eqpUpkeepDetailParamListApi } from '@mxpio/bizcommon';
  import { executeDetailColumns } from './execute.data';
  import { useCommon } from '@mxpio/hooks';
  import { getVxeTableQueryParams } from '@mxpio/utils';

  defineOptions({ name: 'EqpUpkeepExecuteDetailTable' });
  const isDisabled = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const task: Recordable = ref({});
  const actualVauleOptions = reactive<Record<string, any[]>>({});
  const { downloadByFileNo } = useCommon();
  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'EqpUpkeepExecuteDetailTable',
      keepSource: true,
      minHeight: 400,
      tableClass: '!px-0 !py-0',
      columns: executeDetailColumns,
      data: dataSource.value,
      proxyConfig: { enabled: false },
      toolbarConfig: {
        buttons: [],
        import: false,
        print: false,
        export: false,
      },
      editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true,
        autoClear: false,
        enabled: !isDisabled.value,
      },
      editRules: {
        actualVaule: [
          {
            validator: ({ row, cellValue }) => {
              if ((row.isRecord === 1 || row.isRecord === '1') && !cellValue) {
                return new Error('请填写实际值');
              }
            },
          },
        ],
        abnormalRemark: [
          {
            validator: ({ row, cellValue }) => {
              if ((row.isAbnormal === 1 || row.isAbnormal === '1') && !cellValue) {
                return new Error('请填写异常描述');
              }
            },
          },
        ],
      },
      pagerConfig: {
        enabled: false,
      },
    };
  });

  async function setData(data: Recordable) {
    task.value = data.record || {};
    await loadData();
  }

  async function loadData() {
    const params = getVxeTableQueryParams({
      filters: { bizNo: task.value.bizNo },
    });
    const res = await eqpUpkeepDetailListApi(params);
    dataSource.value = res;
    nextTick(() => {
      tableRef.value?.loadData(dataSource.value);
    });
  }

  async function validate() {
    let errMap = await tableRef.value?.validate(true);
    if (errMap) {
      return Promise.reject(errMap);
    }
    return Promise.resolve();
  }

  function getData() {
    try {
      const { fullData } = tableRef.value?.getTableData() || {};
      return fullData;
    } catch (err) {
      console.error(err);
    }
  }

  function handleEditClosed({ row, column }: Recordable) {
    if (column.field === 'actualVaule') {
      avInputChange(row);
    }
  }

  function avInputChange(record: Recordable) {
    let abnormalDescr = '';
    if (record.actualVaule && (record.contentType === 1 || record.contentType === '1')) {
      if (record.lowerLimit || record.upperLimit) {
        const actualVal = Number(record.actualVaule);
        const lower = record.lowerLimit ? Number(record.lowerLimit) : null;
        const upper = record.upperLimit ? Number(record.upperLimit) : null;

        if (upper === null || isNaN(upper)) {
          // 不存在上限值，此时比较下限值
          if (lower !== null && !isNaN(lower) && lower > actualVal) {
            record.isAbnormal = '1';
            abnormalDescr =
              record.content +
              '实际值为：' +
              record.actualVaule +
              ',低于下限值' +
              record.lowerLimit;
          } else {
            record.isAbnormal = '0';
          }
        } else if (lower === null || isNaN(lower)) {
          // 不存在下限值，此时比较上限值
          if (upper < actualVal) {
            abnormalDescr =
              record.content +
              '实际值为：' +
              record.actualVaule +
              ',高于上限值' +
              record.upperLimit;
            record.isAbnormal = '1';
          } else {
            record.isAbnormal = '0';
          }
        } else {
          // 上限值和下限值都存在，此时比较是否在范围内
          if (upper < actualVal || lower > actualVal) {
            abnormalDescr = record.content + '实际值为：' + record.actualVaule + ',超出范围';
            record.isAbnormal = '1';
          } else {
            record.isAbnormal = '0';
          }
        }
        nextTick(() => {
          record.abnormalRemark = abnormalDescr;
        });
      }
    }
  }

  async function listDetailParams(id: string) {
    if (actualVauleOptions[id]) {
      return;
    }
    try {
      const res = await eqpUpkeepDetailParamListApi(id);
      actualVauleOptions[id] = res || [];
    } catch (error) {
      console.error('Error loading detail params:', error);
    }
  }

  function handleActualValueChange(row: Recordable, value: string) {
    row.actualVaule = value;
    avInputChange(row);
  }

  async function handleDownload(row: Recordable) {
    try {
      await downloadByFileNo(row.pic);
    } catch (error) {
      console.error('Error downloading file:', error);
    }
  }

  defineExpose({
    setData,
    validate,
    getData,
  });
</script>
