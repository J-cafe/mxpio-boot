const pages = {
  OrganizeNeedOrderList: () => import('./views/order/organizeNeed/index.vue'),
  OrganizeNeedDetailList: () => import('./views/order/organizeNeedDetail/index.vue'),
  MainPlanList: () => import('./views/order/mainPlan/index.vue'),
  MainPlanAuditList: () => import('./views/order/mainPlanAudit/index.vue'),
  ManufacturePlanList: () => import('./views/order/manufacturePlan/index.vue'),
  OutsourcePlanList: () => import('./views/order/outsourcePlan/index.vue'),
  PurchasePlanList: () => import('./views/order/purchasePlan/index.vue'),
  SaleForecastList: () => import('./views/order/saleForecast/index.vue'),
  SupplyChainsByForecast: () => import('./views/order/supplyChains/byForecast/index.vue'),
  SupplyChainsBySale: () => import('./views/order/supplyChains/bySale/index.vue'),
};

export default pages;
