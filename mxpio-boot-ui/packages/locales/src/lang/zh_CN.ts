import { genMessage } from '../helper';
import antdLocale from 'ant-design-vue/es/locale/zh_CN';
// import { deepMerge } from '@mxpio/utils';

const modules = import.meta.glob('./zh-CN/**/*.{json,ts,js}', { eager: true });
export default {
  message: {
    ...genMessage(modules as Recordable<Recordable>, 'zh-CN'),
    antdLocale,
    // antdLocale: {
    //   ...antdLocale,
    //   DatePicker: deepMerge(
    //     antdLocale.DatePicker,
    //     genMessage(modules as Recordable<Recordable>, 'zh-CN').antdLocale.DatePicker,
    //   ),
    // },
  },
  dateLocale: null,
  dateLocaleName: 'zh-CN',
};
