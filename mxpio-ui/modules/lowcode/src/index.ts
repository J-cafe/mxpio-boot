import { setupI18n } from './locales';

const modelList = () => import('./views/model/index.vue');
const pageList = () => import('./views/page/index.vue');
const pageDesigner = () => import('./views/page/PageDesigner.vue');
const dynamicPage = () => import('./views/runtime/DynamicPage.vue');

setupI18n();

export { DataSet } from './core/DataSet';
export { DataPath } from './core/DataPath';

export default {
  modelList,
  pageList,
  pageDesigner,
  dynamicPage,
};
