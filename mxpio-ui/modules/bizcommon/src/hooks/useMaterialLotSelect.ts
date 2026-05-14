import XEUtils from 'xe-utils';
import { merge } from 'lodash-es';
import type { Recordable } from '@mxpio/types';
import { getMaxLineNo } from '../utils/vxe-table-utils';

export function useMaterialLotSelect(options: {
  tableRef: any;
  extraMaterialProps?: Recordable | ((row: Recordable) => Recordable);
  defaultValues?: Recordable;
  onMaterialChange?: (params: any, value: any, items: any) => void;
  extraLotProps?: Recordable | ((row: Recordable) => Recordable);
  onLotChange?: (params: any, value: any, items: any) => void;
  extraMaterialFileds?: (item: Recordable, isClear?: boolean) => Recordable;
}) {
  const {
    tableRef,
    extraMaterialProps = {},
    extraMaterialFileds,
    onMaterialChange,
    defaultValues = {},
    extraLotProps = {},
    onLotChange,
  } = options;

  // 物料选择配置
  const materialSelectConfig = {
    name: 'MaterialSelect',
    placeholder: '请选择',
    props: ({ row }: { row: Recordable }) => {
      if (typeof extraMaterialProps === 'function') {
        return extraMaterialProps(row);
      }
      return {
        ...extraMaterialProps,
      };
    },
    events: {
      change: (params: any, value: any, items: any) => {
        const { row, $grid } = params;
        const insertData: Recordable[] = [];
        const insertDataDef = getInsertData();

        if (items.length === 0) {
          XEUtils.assign(row, {
            itemCode: '',
            itemName: '',
            itemSpec: '',
            drawingNo: '',
            unitCode: '',
            quantity: '',
            lotNo: '',
            subLotIndex: '',
            textMap: {},
            ...(extraMaterialFileds ? extraMaterialFileds({}, true) : {}),
          });
        } else {
          items.forEach((item: any, i: number) => {
            if (i === 0) {
              XEUtils.merge(
                row,
                getMaterialData(item),
                extraMaterialFileds ? extraMaterialFileds(item) : {},
              );
            } else {
              insertData.push(
                merge(
                  getMaterialData(item),
                  insertDataDef,
                  {
                    lineNo: insertDataDef.lineNo++,
                  },
                  extraMaterialFileds ? extraMaterialFileds(item) : {},
                ),
              );
            }
          });

          $grid.insert(insertData);
        }

        // 调用自定义物料变更事件
        if (onMaterialChange) {
          onMaterialChange(params, value, items);
        }
      },
    },
  };

  // 批次选择配置
  const lotSelectConfig = {
    name: 'InvLotSelect',
    events: {
      change: (params: any, value: any, item: any) => {
        const { row } = params;
        XEUtils.assign(row, {
          ...getMaterialData(item),
          quantity: item?.executeQuantity,
          subLotIndex: item?.subLotIndex,
        });
        // 调用自定义批次变更事件
        if (onLotChange) {
          onLotChange(params, value, item);
        }
      },
    },
    props: ({ row }: { row: Recordable }) => {
      if (typeof extraLotProps === 'function') {
        return {
          multiple: false, // 目前不支持多选
          ...extraLotProps(row),
        };
      }
      return {
        multiple: false, // 目前不支持多选
        ...extraLotProps,
      };
    },
  };

  // 获取物料数据
  function getMaterialData(item: Recordable) {
    if (!item) {
      return {};
    }
    return {
      itemName: item?.itemName,
      itemCode: item?.itemCode,
      itemSpec: item?.itemSpec,
      drawingNo: item?.drawingNo,
      unitCode: item?.unitCode,
      textMap: {
        unitCode$DICT_TEXT_: item?.textMap?.unitCode$DICT_TEXT_,
      },
    };
  }

  // 获取插入数据
  function getInsertData() {
    const maxLineNo = getMaxLineNo(tableRef);
    return {
      lineNo: maxLineNo + 1,
      ...defaultValues,
    };
  }

  return {
    materialSelectConfig,
    lotSelectConfig,
    getMaterialData,
    getInsertData,
  };
}
