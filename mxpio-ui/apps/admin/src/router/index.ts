import system from '@mxpio/system';
import flow from '@mxpio/flow';
import dbconsole from '@mxpio/dbconsole';
import demo from '@mxpio/demo';
import technology from '@mxpio/technology';
import inventory from '@mxpio/inventory';
import sales from '@mxpio/sales';
import purchase from '@mxpio/purchase';
import common from '@mxpio/bizcommon';
import workshop from '@mxpio/workshop';
import quality from '@mxpio/quality';
import plan from '@mxpio/plan';
import equipment from '@mxpio/equipment';
import layouts from '@mxpio/layouts';
import lowcode from '@mxpio/lowcode';

// 自动加载modules目录下的所有ts文件路由模块
const modules = import.meta.glob('./modules/**/*.ts', { eager: true });

const modulesComponents = {};

Object.keys(modules).forEach((key) => {
  const mod = (modules as Recordable)[key].default || {};
  Object.assign(modulesComponents, mod);
});
// const LAYOUT = () => Promise.resolve(import('@mxpio/layouts/src/default/index.vue'));

export const pages = {
  ...layouts,
  ...system,
  ...flow,
  ...dbconsole,
  ...demo,
  ...technology,
  ...inventory,
  ...sales,
  ...purchase,
  ...modulesComponents,
  ...common,
  ...workshop,
  ...quality,
  ...plan,
  ...equipment,
  ...lowcode,
};
export default pages;
