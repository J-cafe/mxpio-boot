import { withInstall } from '@mxpio/utils';
import impExcel from './src/ImportExcel.vue';
import expExcelModal from './src/ExportExcelModal.vue';

export const ImpExcel = withInstall(impExcel);
export const ExpExcelModal = withInstall(expExcelModal);
export * from './src/typing';
export {
  jsonToSheetXlsx,
  aoaToSheetXlsx,
  jsonToMultipleSheetXlsx,
  aoaToMultipleSheetXlsx,
} from './src/Export2Excel';
