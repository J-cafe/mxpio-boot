export * from './error-log/index.vue';
export * from './exception/index';
export * from './login/Login.vue';
export * from './loginSSO/index.vue';
export * from './redirect/index.vue';

const sys = {
  EXCEPTION_COMPONENT: () => import('./exception/Exception.vue'),
  REDIRECT: () => import('./redirect/index.vue'),
  ERROR_LOG: () => import('./error-log/index.vue'),
  Login: () => import('./login/Login3.vue'),
  LoginSSO: () => import('./loginSSO/index.vue'),
  HomePage: () => import('./homePage/index.vue'),
};

export default sys;
