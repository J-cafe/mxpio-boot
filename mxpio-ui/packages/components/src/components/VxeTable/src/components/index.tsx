import { VxeUIExport, VxeUIPluginObject } from 'vxe-pc-ui';
import { handleClearEvent } from './event';
import AAutoComplete from './AAutoComplete';
import AInput from './AInput';
import AInputNumber from './AInputNumber';
import ASelect from './ASelect';
import ACascader from './ACascader';
import ADatePicker from './ADatePicker';
import AMonthPicker from './AMonthPicker';
import ARangePicker from './ARangePicker';
import AWeekPicker from './AWeekPicker';
import ATreeSelect from './ATreeSelect';
import ATimePicker from './ATimePicker';
import ARate from './ARate';
import ASwitch from './ASwitch';
import ARadioGroup from './ARadioGroup';
import ACheckboxGroup from './ACheckboxGroup';
import AButton from './AButton';
import AButtonGroup from './AButtonGroup';
import ApiSelect from './ApiSelect';
import AApiTreeSelect from './AApiTreeSelect';
import AEmpty from './AEmpty';
import AInputSearch from './AInputSearch';
import AYearPicker from './AYearPicker';
import ApiSelectPage from './ApiSelectPage';
import Upload from './Upload';

/**
 * 基于 vxe-table 表格的适配插件，用于兼容 ant-design-vue 组件库
 */
export const VXETablePluginAntd: VxeUIPluginObject = {
  install(vxetablecore: VxeUIExport): void {
    const { interceptor, renderer } = vxetablecore;

    renderer.mixin({
      AAutoComplete,
      AInput,
      AInputNumber,
      ASelect,
      ACascader,
      ADatePicker,
      AMonthPicker,
      ARangePicker,
      AWeekPicker,
      ATimePicker,
      ATreeSelect,
      ARate,
      ASwitch,
      ARadioGroup,
      ACheckboxGroup,
      AButton,
      AButtonGroup,
      ApiSelect,
      AApiTreeSelect,
      AEmpty,
      AInputSearch,
      AYearPicker,
      Upload,
      ApiSelectPage,
    });

    interceptor.add('event.clearFilter', handleClearEvent);
    interceptor.add('event.clearEdit', handleClearEvent);
    interceptor.add('event.clearAreas', handleClearEvent);
  },
};

if (typeof window !== 'undefined' && window.VxeUI && window.VxeUI.use) {
  window.VxeUI.use(VXETablePluginAntd);
}

export default VXETablePluginAntd;
