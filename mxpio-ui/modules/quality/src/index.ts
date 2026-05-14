const pages = {
  QualityItemList: () => import('./views/basic/item/index.vue'),
  QualityTemplateList: () => import('./views/basic/template/index.vue'),
  QualityChainsByTemplate: () => import('./views/basic/chains/byTemplate/index.vue'),
  QualityChainsByItem: () => import('./views/basic/chains/byItem/index.vue'),
  QualityChainsByItemGroup: () => import('./views/basic/chains/byItemGroup/index.vue'),
  PurchaseQualityList: () => import('./views/notice/purchase/index.vue'),
  ManufactureQualityList: () => import('./views/notice/manufacture/index.vue'),
  SaleReturnQualityList: () => import('./views/notice/saleReturn/index.vue'),
  OutsourceQualityList: () => import('./views/notice/outsource/index.vue'),
  InventoryQualityList: () => import('./views/notice/inventory/index.vue'),
  QualityOrderList: () => import('./views/order/quality/index.vue'),
  QualityUDList: () => import('./views/order/ud/index.vue'),
  QualityIIAList: () => import('./views/order/iia/index.vue'),
  QualityRecordList: () => import('./views/report/record/index.vue'),
  QualityUnqualifiedList: () => import('./views/report/unqualified/index.vue'),
};

export default pages;
