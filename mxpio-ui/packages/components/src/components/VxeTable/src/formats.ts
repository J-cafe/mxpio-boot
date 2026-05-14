import { VxeUI } from 'vxe-pc-ui';

// 字典格式化显示
VxeUI.formats.add('dictText', {
  cellFormatMethod({ row, column, cellValue }) {
    return (row['textMap'] && row['textMap'][column.field + '$DICT_TEXT_']) || cellValue;
  },
});
