export function useExecuteClassifyData() {
  /**
   * 处理订单执行数据，展开选中的批次信息并组装执行所需格式
   * @param rows 订单明细行数据数组
   * @returns 格式化后的执行数据 { lines: 处理后的明细数据数组 }
   * 增加外部自定义属性方法
   */
  function classifyOrderFormData(
    rows: Recordable[],
    callBack?: (row: Recordable, lot?: Recordable) => Recordable,
  ) {
    // 展开已选择批次数组，组装执行需要的格式
    const tableData: Recordable[] = [];
    rows.forEach((item: Recordable) => {
      if (item.selectLot && item.selectLot.length > 0) {
        item.selectLot.forEach((lot: Recordable) => {
          const customAttr = callBack?.(item, lot) ?? {};
          tableData.push({
            bizOrderNo: item.bizNo,
            lotNo: lot.lotNo,
            subLotIndex: lot.subLotIndex,
            quantity: lot.executeQuantity,
            whCode: lot.whCode,
            itemCode: item.itemCode,
            bizOrderlineNo: item.lineNo,
            lineQuantity: item.quantity,
            ...customAttr,
          });
        });
      } else {
        const customAttr = callBack?.(item) ?? {};
        tableData.push({
          bizOrderNo: item.bizNo,
          lotNo: item.lotNo || '', //部分业务单会指定批次号，所以这里需要判断
          subLotIndex: item.subLotIndex || '',
          whCode: item.whCode,
          itemCode: item.itemCode,
          bizOrderlineNo: item.lineNo,
          quantity: item.executeQuantity,
          lineQuantity: item.quantity,
          ...customAttr,
        });
      }
    });
    return tableData;
  }

  /**
   * 处理通知单执行数据，展开选中的批次信息并组装执行所需格式
   * @param rows 订单明细行数据数组
   * @param notice 通知单数据对象
   * @returns 格式化后的执行数据 { lines: 处理后的明细数据数组 }
   */
  function classifyNoticeFormData(
    rows: Recordable[],
    notice: Recordable,
    callBack?: (row: Recordable) => Record<string, any>,
  ) {
    // 展开已选择批次数组，组装执行需要的格式
    const tableData: Recordable[] = [];
    rows.forEach((item: Recordable) => {
      const customAttr = callBack?.(item) ?? {};
      tableData.push({
        bizOrderNo: item.originBizNo,
        bizOrderlineNo: item.originBizLineNo,
        itemCode: item.itemCode,
        lineQuantity: Number(item.quantity),
        lotNo: item.lotNo,
        subLotIndex: item.subLotIndex,
        quantity: Number(item.executeQuantity),
        whCode: notice.whCode,
        bizWoLineNo: '',
        bizWoNo: '',
        noticeNo: notice.noticeNo,
        noticeLineNo: item.lineNo,
        ...customAttr,
      });
    });
    return tableData;
  }

  /**
   * 处理仓单执行数据，展开选中的批次信息并组装执行所需格式
   * @param rows 订单明细行数据数组
   * @param notice 通知单数据对象
   * @returns 格式化后的执行数据 { lines: 处理后的明细数据数组 }
   */
  function classifyWhFormData(
    rows: Recordable[],
    callBack?: (row: Recordable, lot?: Recordable) => Record<string, any>,
  ) {
    // 展开已选择批次数组，组装执行需要的格式
    const tableData: Recordable[] = [];
    rows.forEach((item: Recordable) => {
      if (item.selectLot && item.selectLot.length > 0) {
        item.selectLot.forEach((lot: Recordable) => {
          const customAttr = callBack?.(item, lot) ?? {};
          tableData.push({
            bizOrderNo: item.originBizNo,
            bizOrderlineNo: item.originBizLineNo,
            itemCode: item.itemCode,
            lineQuantity: Number(item.quantity),
            lotNo: lot.lotNo,
            subLotIndex: lot.subLotIndex,
            quantity: Number(lot.executeQuantity),
            whCode: item.whCode,
            bizWoLineNo: item.lineNo,
            bizWoNo: item.bizNo,
            noticeNo: item.originNoticeNo,
            noticeLineNo: item.originNoticeLineNo,
            ...customAttr,
          });
        });
      } else {
        const customAttr = callBack?.(item) ?? {};
        tableData.push({
          bizOrderNo: item.originBizNo,
          bizOrderlineNo: item.originBizLineNo,
          itemCode: item.itemCode,
          lineQuantity: Number(item.quantity),
          lotNo: item.lotNo,
          subLotIndex: item.subLotIndex,
          quantity: Number(item.executeQuantity),
          whCode: item.whCode,
          bizWoLineNo: item.lineNo,
          bizWoNo: item.bizNo,
          noticeNo: item.originNoticeNo,
          noticeLineNo: item.originNoticeLineNo,
          ...customAttr,
        });
      }
    });

    return tableData;
  }

  return {
    classifyOrderFormData,
    classifyNoticeFormData,
    classifyWhFormData,
  };
}
