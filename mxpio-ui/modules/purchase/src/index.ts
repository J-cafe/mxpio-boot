const pages = {
  SupplyList: () => import('./views/basic/supply/index.vue'),
  PurchaseOrderList: () => import('./views/order/purchaseOrder/index.vue'),
  PurchaseOrderDetailList: () => import('./views/order/purchaseOrderDetail/index.vue'),
  PurchaseReceiveList: () => import('./views/notice/receive/index.vue'),
  PurchaseReturnList: () => import('./views/notice/return/index.vue'),
  PurchaseOrderExecuteList: () => import('./views/order/purchaseOrderExecute/index.vue'),
  BuyRequestList: () => import('./views/order/buyRequest/index.vue'),
  BuyRequestDetail: () => import('./views/order/buyRequestDetail/index.vue'),
};

export default pages;
