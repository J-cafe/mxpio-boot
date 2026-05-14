const pages = {
  ResList: () => import('./res/index.vue'),
  NoticeList: () => import('./notice/index.vue'),
  OrderList: () => import('./order/index.vue'),
};

export default pages;
