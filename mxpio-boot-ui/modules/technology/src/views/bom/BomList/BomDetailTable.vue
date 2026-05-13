<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable, VxeGridPropTypes } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance } from '@mxpio/components';
  import { getItemConsumption } from '@mxpio/bizcommon';
  import { CuryTypeEnum } from '@mxpio/enums';
  import XEUtils from 'xe-utils';
  import { debounce } from 'lodash-es';
  import { isNull } from '@mxpio/utils';

  defineOptions({ name: 'BomDetailTable' });

  const isUpdate = ref(true);
  const isDisabled = ref(false);
  const isTmp = ref(false);
  const isCopy = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const formData: Recordable = {};

  const detailColumns: VxeGridPropTypes.Columns = [
    { type: 'checkbox', width: 40 },
    {
      title: '序号',
      type: 'seq',
      width: '50',
      align: 'center',
    },
    {
      title: '子项物料编码',
      field: 'itemCode',
      editRender: {
        name: 'MaterialSelect',
        placeholder: '请选择',
        events: {
          change: (params: any, value, items) => {
            const { row, $grid } = params;
            const insertData: Recordable[] = [];
            items.forEach((item: any, i) => {
              if (i === 0) {
                XEUtils.assign(row, getInsertData(item));
              } else {
                insertData.push(getInsertData(item));
              }
            });
            $grid.insert(insertData);
          },
        },
      },
      width: 140,
    },
    {
      title: '子项物料名称',
      field: 'itemName',
      width: 120,
    },
    {
      title: '子项规格型号',
      field: 'itemSpec',
      width: 120,
    },
    {
      title: '图号',
      field: 'drawingNo',
      width: 100,
    },
    {
      title: '子项物料组',
      field: 'itemProp',
      width: 100,
    },
    {
      title: '是否原材料',
      field: 'material',
      formatter: 'dictText',
      width: 120,
    },
    {
      title: '材料类型',
      field: 'materialType',
      formatter: 'dictText',
      width: 120,
    },
    {
      title: '材料牌号',
      field: 'materialBrand',
      width: 100,
    },
    {
      title: '基本用量',
      field: 'itemConsumption',
      editRender: {
        name: 'AInputNumber',
        props: ({ row }) => ({
          // 示例：根据行数据动态设置禁用状态
          disabled: row.material === '1' && row.materialType !== '5',
        }),
      },
      width: 120,
    },
    {
      title: '下料长度',
      field: 'makeLength',
      editRender: {
        name: 'AInputNumber',
        props: ({ row }) => ({
          // 示例：根据行数据动态设置禁用状态
          disabled: row.material !== '1',
        }),
        events: {
          change: (params: any) => {
            changeMakea(params);
          },
        },
      },
      width: 120,
    },
    {
      title: '下料宽度',
      field: 'makeArea',
      editRender: {
        name: 'AInputNumber',
        props: ({ row }) => ({
          // 示例：根据行数据动态设置禁用状态
          disabled: row.material !== '1',
        }),
        events: {
          change: (params: any) => {
            changeMakea(params);
          },
        },
      },
      width: 120,
    },
    {
      title: '可制数量',
      field: 'makeNum',
      editRender: {
        name: 'AInputNumber',
        props: ({ row }) => ({
          // 示例：根据行数据动态设置禁用状态
          disabled: row.material !== '1',
        }),
        events: {
          change: (params: any) => {
            changeMakea(params);
          },
        },
      },
      width: 120,
    },
    {
      title: '损耗率',
      field: 'wastage',
      editRender: {
        name: 'AInputNumber',
      },
      width: 120,
    },
  ];
  const pattern = /^(0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*)$/;
  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'BomDetailTable',
      keepSource: true,
      minHeight: '200px',
      editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true,
        autoClear: false,
        enabled: !isDisabled.value,
      },
      columns: detailColumns,
      toolbarConfig: {
        buttons: [
          {
            content: '新增',
            buttonRender: {
              name: 'AButton',
              attrs: {
                class: 'ml-2',
              },
              props: {
                type: 'primary',
                preIcon: 'mdi:page-next-outline',
                disabled: isDisabled.value,
              },
              events: {
                click: () => {
                  tableRef.value?.insert({});
                },
              },
            },
          },
          {
            content: '删除',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                danger: true,
                preIcon: 'mdi:delete-forever',
                disabled: isDisabled.value,
              },
              events: {
                click: async () => {
                  const selectRecords = tableRef.value?.getCheckboxRecords() || [];
                  if (selectRecords.length > 0) {
                    const removeList: Recordable[] = [];
                    const pendingList: Recordable[] = [];
                    selectRecords.forEach((item: Recordable) => {
                      if (item.tmpCrudType === CuryTypeEnum.SAVE || !item.tmpCrudType) {
                        // tmpCrudType状态为save的直接在草稿表删除
                        removeList.push(item);
                      } else {
                        pendingList.push(item);
                      }
                    });
                    removeList.length > 0 && tableRef.value?.remove(removeList);
                    pendingList.length > 0 && tableRef.value?.setPendingRow(pendingList, true);
                  }
                },
              },
            },
          },
          {
            content: '取消删除',
            buttonRender: {
              name: 'AButton',
              props: {
                // type: 'primary',
                preIcon: 'mdi:undo-variant',
                disabled: isDisabled.value,
              },
              events: {
                click: async () => {
                  const selectRecords = tableRef.value?.getCheckboxRecords() || [];
                  if (selectRecords.length > 0) {
                    await tableRef.value?.setPendingRow(selectRecords, false);
                  }
                },
              },
            },
          },
        ],
        import: false,
        print: false,
        export: false,
        refresh: false,
        custom: false,
      },
      data: dataSource.value,
      editRules: {
        itemCode: [
          { required: true, message: '请选择物料', trigger: 'change' },
          { validator: validatorMatertal, trigger: 'manual' },
        ],
        itemConsumption: [
          { required: true, message: '请输入基本用量', trigger: 'change' },
          {
            type: 'number',
            pattern: pattern,
            message: '基本用量不能小于0',
            trigger: 'change',
          },
        ],
        wastage: [
          {
            type: 'number',
            pattern: pattern,
            message: '基本用量不能小于0',
            trigger: 'change',
          },
        ],
        makeArea: [
          {
            validator: ({ cellValue, row }) => {
              if (!cellValue && row.material === '1' && ['4'].includes(row.materialType)) {
                // 板料时需要填写下料宽度
                return new Error(`请输入下料宽度`);
              }
            },
          },
          {
            type: 'number',
            pattern: pattern,
            message: '基本用量不能小于0',
            trigger: 'change',
          },
        ],
        makeLength: [
          {
            validator: ({ cellValue, row }) => {
              if (
                !cellValue &&
                row.material === '1' &&
                ['1', '2', '3', '4'].includes(row.materialType)
              ) {
                // 管料、棒料、六方料、板料时需要填写下料长度
                return new Error(`请输入下料长度`);
              }
            },
          },
          {
            type: 'number',
            pattern: pattern,
            message: '基本用量不能小于0',
            trigger: 'change',
          },
        ],
        makeNum: [
          {
            validator: ({ cellValue, row }) => {
              if (
                !cellValue &&
                row.material === '1' &&
                ['1', '2', '3', '4'].includes(row.materialType)
              ) {
                // 管料、棒料、六方料、板料时需要填写可制数量
                return new Error(`请输入可制数量`);
              }
            },
          },
          {
            type: 'number',
            pattern: pattern,
            message: '基本用量不能小于0',
            trigger: 'change',
          },
        ],
      },
      proxyConfig: { enabled: false },
      'cell-class-name': ({ row }) => {
        if (row.tmpCrudType === CuryTypeEnum.SAVE || !row.tmpCrudType) {
          return 'row--add';
        }
      },
      pagerConfig: {
        enabled: false,
      },
    };
  });

  async function setData(data: Recordable) {
    isDisabled.value = !!data?.disabled;
    isUpdate.value = !!data?.isUpdate;
    isTmp.value = !!data?.isTmp;
    isCopy.value = !!data?.isCopy;
    const record = data.record || {};
    const dataSource = record.bomInfoLineList;
    formData.value = record;
    const pendingList: Recordable[] = [];
    dataSource.forEach((item: Recordable) => {
      item.tmpCrudType = item.tmpCrudType || CuryTypeEnum.NONE; // 数据对于bom表为未修改
      if (!isTmp.value && !isDisabled.value) {
        // 当前为非草稿编辑状态时，bomLineId、bomId 需要先清空
        item.bomId = null;
        item.bomLineId = null;
      }
      if (item.tmpCrudType === CuryTypeEnum.DELETE) {
        pendingList.push(item);
      }
    });
    if (isCopy.value) {
      // 复制时，需要直接插入数据
      tableRef.value?.insert(dataSource || []);
    } else {
      tableRef.value?.loadData(dataSource || []);
      pendingList.length > 0 && tableRef.value?.setPendingRow(pendingList, true);
    }
  }

  async function validate() {
    let errMap = await tableRef.value?.validate(true);
    if (errMap) {
      return Promise.reject(errMap);
    }
    return Promise.resolve();
  }

  function getRecordset() {
    return isTmp.value ? getTmpDataCrud() : getDataCrud();
  }

  // 获取变更时数据
  function getDataCrud() {
    const {
      insertRecords = [],
      updateRecords = [],
      pendingRecords = [],
    } = tableRef.value?.getRecordset() || {};
    const { fullData } = tableRef.value?.getTableData() || {};
    const modifiedIds: string[] = [];
    insertRecords &&
      insertRecords.forEach((item) => {
        // 新插入
        item.crudType = CuryTypeEnum.SAVE; // 数据对于草稿表为新增
        item.tmpCrudType = CuryTypeEnum.SAVE; // 数据对于bom表为新增
        modifiedIds.push(item._X_ROW_KEY);
      });
    updateRecords &&
      updateRecords.forEach((item) => {
        // 编辑中
        item.crudType = CuryTypeEnum.SAVE; // 数据对于草稿表为新增
        item.tmpCrudType = CuryTypeEnum.UPDATE; // 数据对于bom表为修改
        modifiedIds.push(item._X_ROW_KEY);
      });
    pendingRecords &&
      pendingRecords.forEach((item) => {
        // 已删除
        item.crudType = CuryTypeEnum.SAVE; // 数据对于草稿表为新增
        item.tmpCrudType = CuryTypeEnum.DELETE; // 数据对于bom表为删除
        modifiedIds.push(item._X_ROW_KEY);
      });
    const noneRecords: Recordable[] = [];
    fullData?.forEach((item) => {
      // 处理第一次变更时，未修改数据
      !modifiedIds.includes(item._X_ROW_KEY) &&
        noneRecords.push({
          ...item,
          crudType: CuryTypeEnum.SAVE, // 数据对于草稿表为新增
          tmpCrudType: CuryTypeEnum.NONE, // 数据对于bom表为未修改
        });
    });
    return [
      ...(insertRecords as Recordable[]),
      ...(updateRecords as Recordable[]),
      ...(pendingRecords as Recordable[]),
      ...noneRecords,
    ];
  }

  function getTmpDataCrud() {
    const {
      insertRecords = [],
      updateRecords = [],
      pendingRecords = [],
      removeRecords = [],
    } = tableRef.value?.getRecordset() || {};
    const { fullData } = tableRef.value?.getTableData() || {};
    const modifiedIds: string[] = [];
    insertRecords &&
      insertRecords.forEach((item) => {
        // 新插入
        item.crudType = CuryTypeEnum.SAVE; // 数据对于草稿表为新增
        item.tmpCrudType = CuryTypeEnum.SAVE; // 数据对于bom表为新增
        modifiedIds.push(item.bomLineId);
      });
    updateRecords &&
      updateRecords.forEach((item) => {
        // 编辑中
        item.crudType = CuryTypeEnum.UPDATE; // 数据对于草稿表为修改
        item.tmpCrudType =
          item.tmpCrudType === CuryTypeEnum.DELETE || item.tmpCrudType === CuryTypeEnum.NONE
            ? CuryTypeEnum.UPDATE
            : item.tmpCrudType; // 处理撤下删除后又有修改
        modifiedIds.push(item.bomLineId);
      });
    pendingRecords &&
      pendingRecords.forEach((item) => {
        // 已删除
        item.crudType = CuryTypeEnum.UPDATE; // 数据对于草稿表为修改
        item.tmpCrudType = CuryTypeEnum.DELETE; // 数据对于bom表为删除
        modifiedIds.push(item.bomLineId);
      });
    removeRecords &&
      removeRecords.forEach((item) => {
        // 已删除
        item.crudType = CuryTypeEnum.DELETE; // 数据对于草稿表为删除
        item.tmpCrudType = CuryTypeEnum.DELETE; // 数据对于bom表为删除
        modifiedIds.push(item.bomLineId);
      });
    const cancelPendingRecords: Recordable = [];
    fullData?.forEach((item) => {
      // 处理撤下删除，table脏数据未变化
      if (!modifiedIds.includes(item.bomLineId) && item.tmpCrudType === CuryTypeEnum.DELETE) {
        cancelPendingRecords.push({
          ...item,
          crudType: CuryTypeEnum.UPDATE, // 数据对于草稿表为新增
          tmpCrudType: CuryTypeEnum.UPDATE, // 数据对于bom表为未修改
        });
      }
    });
    return [
      ...(insertRecords as Recordable[]),
      ...(updateRecords as Recordable[]),
      ...(pendingRecords as Recordable[]),
      ...removeRecords,
      ...(cancelPendingRecords as Recordable[]),
    ];
  }

  function validatorMatertal({ cellValue, row }) {
    if (row.tmpCrudType === CuryTypeEnum.DELETE) {
      return undefined;
    }
    if (cellValue === formData.value.parentCode) {
      return new Error('不能选择父物料');
    }
    const { fullData } = tableRef.value?.getTableData() || {};
    const list =
      fullData?.filter((item) => {
        return (
          item.tmpCrudType !== CuryTypeEnum.DELETE &&
          item._X_ROW_KEY !== row._X_ROW_KEY &&
          item.itemCode === cellValue
        );
      }) || [];
    if (list.length > 0) {
      return new Error('物料编码重复');
    }
    // 验证通过时显式返回 undefined
    return undefined;
  }

  function getInsertData(item: Recordable) {
    return {
      itemName: item.itemName,
      itemCode: item.itemCode,
      itemSpec: item.itemSpec,
      drawingNo: item.drawingNo,
      unitCode: item.unitCode,
      material: item.material,
      itemProp: item.itemProp,
      itemSource: item.itemSource,
      materialType: item.itemMaterialProp?.materialType,
      tmpCrudType: CuryTypeEnum.SAVE,
      materialBrand: item.materialBrand,
      itemConsumption:
        item.material === '1' && item.itemMaterialProp?.materialType !== '5' ? '' : 1,
      textMap: {
        itemProp$DICT_TEXT_: item.textMap?.itemGroupCode$DICT_TEXT_,
        unitCode$DICT_TEXT_: item.textMap?.unitCode$DICT_TEXT_,
        materialType$DICT_TEXT_: item.itemMaterialProp.textMap?.materialType$DICT_TEXT_,
        material$DICT_TEXT_: item.textMap?.material$DICT_TEXT_,
        itemSource$DICT_TEXT_: item.textMap?.itemSource$DICT_TEXT_,
      },
    };
  }

  const changeMakea = debounce((params) => {
    const { row } = params;
    if (['1', '2', '3'].includes(row.materialType)) {
      // 材料类型为管料、棒料、六方料 只考虑长度与可制数量
      if (isNull(row.itemCode) || isNull(row.makeLength) || isNull(row.makeNum)) {
        return false;
      }
      consumption(row);
    } else if (row.materialType === '4') {
      // 材料类型为板材时 考虑长度、宽度与可制数量
      if (
        isNull(row.itemCode) ||
        isNull(row.makeArea) ||
        isNull(row.makeLength) ||
        isNull(row.makeNum)
      ) {
        return false;
      }
      consumption(row);
    }
  }, 1000);

  function consumption(row) {
    getItemConsumption({
      itemCode: row.itemCode,
      makeArea: Number(row.makeArea),
      makeLength: Number(row.makeLength),
      makeNum: Number(row.makeNum),
    }).then((res) => {
      if (res) {
        XEUtils.set(row, 'itemConsumption', res);
      }
    });
  }

  defineExpose({
    setData,
    getRecordset,
    validate,
  });
</script>
<style lang="less">
  .row--add {
    background-color: #f1e3c9 !important;
  }
</style>
