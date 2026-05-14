import type { VxeGridInstance } from '@mxpio/components';
import { Ref } from 'vue';

export function getMaxLineNo(tableRef: Ref<VxeGridInstance | undefined>) {
  const instance = tableRef.value;
  if (!instance) return 0;
  const { removeRecords = [] }: { removeRecords?: Recordable[] } =
    tableRef.value?.getRecordset() || {};
  const { fullData = [] }: { fullData?: Recordable[] } = tableRef.value?.getTableData() || {};
  let maxLineNo = 0;
  removeRecords.concat(fullData).forEach((item) => {
    if (Number(item.lineNo) > maxLineNo) {
      maxLineNo = Number(item.lineNo);
    }
  });
  return maxLineNo;
}
