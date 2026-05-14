import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';

/**
 * 设置提交数据脏数据状态
 * @param records 包含插入、删除和更新记录的对象数组。
 * @param isUpdate 判断是编辑还是新增标识
 * @returns 返回一个数组，包含经过处理的插入、删除和更新记录。
 */
export function setDataCrud(records: Recordable, isUpdate: Boolean) {
  const { insertRecords, removeRecords, updateRecords } = records;
  if (isUpdate) {
    // 当编辑时，对插入、删除和更新记录分别设置CRUD类型和映射规则ID，并移除_X_ROW_KEY字段
    insertRecords &&
      insertRecords.forEach((item: Recordable) => {
        item.crudType = CuryTypeEnum.SAVE;
        delete item._X_ROW_KEY;
      });
    removeRecords &&
      removeRecords.forEach((item: Recordable) => {
        item.crudType = CuryTypeEnum.DELETE;
        delete item._X_ROW_KEY;
      });
    updateRecords &&
      updateRecords.forEach((item: Recordable) => {
        item.crudType = CuryTypeEnum.UPDATE;
        delete item._X_ROW_KEY;
      });
    // 返回合并后的插入、删除和更新记录数组
    return [].concat(insertRecords, removeRecords, updateRecords);
  } else {
    // 当新增时，默认插入和更新记录的CRUD类型为保存，并移除_X_ROW_KEY字段
    insertRecords &&
      insertRecords.forEach((item: Recordable) => {
        item.crudType = CuryTypeEnum.SAVE;
        delete item._X_ROW_KEY;
      });
    updateRecords &&
      updateRecords.forEach((item: Recordable) => {
        item.crudType = CuryTypeEnum.SAVE;
        delete item._X_ROW_KEY;
      });
    // 返回合并后的插入和更新记录数组
    return [].concat(insertRecords, updateRecords);
  }
}
