const pages = {
  SalesOrderList: () => import('./views/order/salesOrder/index.vue'),
  CustomerList: () => import('./views/basic/customer/index.vue'),
  SaleDeliveryByLine: () => import('./views/order/saleDeliveryByLine/index.vue'),
  SaleDeliveryByInv: () => import('./views/order/saleDeliveryByInv/index.vue'),
  SaleDeliveryList: () => import('./views/notice/delivery/index.vue'),
  SaleReturnList: () => import('./views/notice/return/index.vue'),
};

export default pages;
