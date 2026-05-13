const pages = {
  MaterialList: () => import('./views/material/index.vue'),
  WorkShopList: () => import('./views/workshop/index.vue'),
  WorkCenterList: () => import('./views/workCenter/index.vue'),
  WorkUnitList: () => import('./views/workUnit/index.vue'),
  WorkTeamList: () => import('./views/workTeam/index.vue'),
  FactoryList: () => import('./views/factory/index.vue'),
  BomList: () => import('./views/bom/BomList/index.vue'),
  BomAuditList: () => import('./views/bom/BomAuditList/index.vue'),
  BomStoreyList: () => import('./views/bom/BomStorey/index.vue'),
  BomReverseList: () => import('./views/bom/BomReverse/index.vue'),
  ProcInfoList: () => import('./views/procInfo/index.vue'),
  ProcGroupList: () => import('./views/procGroup/index.vue'),
  ProcStandroutList: () => import('./views/procStandard/index.vue'),
  ProcBomList: () => import('./views/procBom/procBomList/index.vue'),
  ProcBomAuditList: () => import('./views/procBom/procBomAuditList/index.vue'),
  ProcParamList: () => import('./views/procParam/index.vue'),
  ProcDrawingfileList: () => import('./views/procDrawingfile/index.vue'),
  ProcDrawingfileAuditList: () => import('./views/procDrawingfileAuditList/index.vue'),
};

export default pages;
