import type { Component } from 'vue';

import type { ComponentType } from './componentType';
import { ApiSelect, ApiTreeSelect, ApiSelectPage } from '../../Form';
// import ExportButton from '../../zz/VxeTable/ExportButton.vue';
// import ImportButton from '../../zz/VxeTable/ImportButton.vue';
import { BasicUpload } from '../../Upload';
import {
  Input,
  Select,
  Radio,
  Checkbox,
  AutoComplete,
  Cascader,
  DatePicker,
  InputNumber,
  Switch,
  TimePicker,
  TreeSelect,
  Rate,
  Empty,
} from 'ant-design-vue';
import { Button } from '../../Button';

const componentMap = new Map<ComponentType | string, Component>();

componentMap.set('AButton', Button);

componentMap.set('AInput', Input);
componentMap.set('AInputSearch', Input.Search);
componentMap.set('AInputNumber', InputNumber);
componentMap.set('AAutoComplete', AutoComplete);

componentMap.set('ASelect', Select);
componentMap.set('ATreeSelect', TreeSelect);
componentMap.set('ASwitch', Switch);
componentMap.set('ARadioGroup', Radio.Group);
componentMap.set('ACheckboxGroup', Checkbox.Group);
componentMap.set('ACascader', Cascader);
componentMap.set('ARate', Rate);

componentMap.set('ADatePicker', DatePicker);
componentMap.set('AMonthPicker', DatePicker.MonthPicker);
componentMap.set('ARangePicker', DatePicker.RangePicker);
componentMap.set('AWeekPicker', DatePicker.WeekPicker);
componentMap.set('AYearPicker', DatePicker.YearPicker);
componentMap.set('ATimePicker', TimePicker);

componentMap.set('ApiSelect', ApiSelect);
componentMap.set('AApiTreeSelect', ApiTreeSelect);

componentMap.set('AEmpty', Empty);

// componentMap.set('ExportButton', ExportButton);
// componentMap.set('ImportButton', ImportButton);

componentMap.set('ApiSelectPage', ApiSelectPage);
componentMap.set('Upload', BasicUpload);
export function add<T extends string, R extends Component>(
  compName: ComponentType | T,
  component: R,
) {
  componentMap.set(compName, component);
}

export function del<T extends string>(compName: ComponentType | T) {
  componentMap.delete(compName);
}

export { componentMap };
