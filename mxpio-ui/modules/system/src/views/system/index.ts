const system = {
  user: () => import('./user/index.vue'),
  import: () => import('./import/index.vue'),
  role: () => import('./role/index.vue'),
  dept: () => import('./dept/index.vue'),
  quartzJob: () => import('./quartzJob/index.vue'),
  export: () => import('./export/index.vue'),
  post: () => import('./post/index.vue'),
  menu: () => import('./menu/index.vue'),
  dict: () => import('./dict/index.vue'),
  posttype: () => import('./posttype/index.vue'),
};

export default system;
