export * from './src';
const pages = {
  OUTSIDE: () => import('./src/outside/index.vue'),
  IFRAME: () => import('./src/iframe/index.vue'),
  LAYOUT: () => import('./src/default/index.vue'),
};
export default pages;
