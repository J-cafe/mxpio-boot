const pages = {
  ManufactureOrderList: () => import('./views/order/manufacture/manufactureOrder/index.vue'),
  ManufactureExecuteList: () => import('./views/order/manufacture/manufactureExecute/index.vue'),
  ManufacturePickingList: () => import('./views/notice/manufacture/picking/index.vue'),
  ManufactureSupplementList: () => import('./views/notice/manufacture/supplement/index.vue'),
  ManufactureReturnList: () => import('./views/notice/manufacture/return/index.vue'),
  ManufactureScrapList: () => import('./views/notice/manufacture/scrap/index.vue'),
  ManufactureOrderInList: () => import('./views/notice/manufacture/in/index.vue'),
  OutsourceOrderList: () => import('./views/order/outsource/outsourceOrder/index.vue'),
  OutsourceExecuteList: () => import('./views/order/outsource/outsourceExecute/index.vue'),
  OutsourcePickingList: () => import('./views/notice/outsource/picking/index.vue'),
  OutsourceReturnList: () => import('./views/notice/outsource/return/index.vue'),
  OutsourceSupplementList: () => import('./views/notice/outsource/supplement/index.vue'),
  OutsourceScrapList: () => import('./views/notice/outsource/scrap/index.vue'),
  OutsourceOrderInList: () => import('./views/notice/outsource/in/index.vue'),
};

export default pages;
