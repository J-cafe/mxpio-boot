import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  ItemList = '/erp/common/item/page',
  ItemByCode = '/erp/common/item/list/',
  ItemWithSubExtends = '/erp/common/item/pageWithSubExtends',
  DeleteItem = '/erp/common/item/remove/',
  Config = '/erp/common/res/list/Item',
  AddItem = '/erp/common/item/add',
  EditItem = '/erp/common/item/edit',
  GetLeadtime = '/erp/common/item/prop/leadtime/list/item/',
  AddLeadtime = '/erp/common/item/prop/leadtime/add',
  EditLeadtime = '/erp/common/item/prop/leadtime/edit',
  GetPurchase = '/erp/common/item/prop/purchase/list/item/',
  AddPurchase = '/erp/common/item/prop/purchase/add',
  EditPurchase = '/erp/common/item/prop/purchase/edit',
  GetStore = '/erp/common/item/prop/store/list/item/',
  AddStore = '/erp/common/item/prop/store/add',
  EditStore = '/erp/common/item/prop/store/edit',
  GetMaterial = '/erp/common/item/prop/material/list/item/',
  AddMaterial = '/erp/common/item/prop/material/add',
  EditMaterial = '/erp/common/item/prop/material/edit',
  GetQuality = '/erp/common/item/prop/quality/list/item/',
  AddQuality = '/erp/common/item/prop/quality/add',
  EditQuality = '/erp/common/item/prop/quality/edit',
  GetDesignfile = '/erp/common/item/designfile/list_by_item/',
  AddDesignfile = '/erp/common/item/designfile/add_batch',
  EditDesignfile = '/erp/common/item/designfile/edit',
  ItemGroup = '/erp/common/item/group/tree',
  AddItemGroup = '/erp/common/item/group/add',
  EditItemGroup = '/erp/common/item/group/edit',
  DeleteItemGroup = '/erp/common/item/group/remove/',
  GetPlan = '/erp/common/item/prop/plan/list/item/',
  AddPlan = '/erp/common/item/prop/plan/add',
  EditPlan = '/erp/common/item/prop/plan/edit',
}

/**
 * @description: 物料列表
 */

export const itemList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.ItemList, params });
};

/**
 * @description: 根据物料编码获取物料信息
 */

export const itemByCodeInfoApi = (itemCode: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.ItemByCode + itemCode });
};

/**
 * @description: 物料列表带属性
 */

export const itemWithSubExtends = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.ItemWithSubExtends, params });
};

/**
 * @description: 物料组树
 */

export const itemGroup = () => {
  const { http } = useBridge();
  return http.get({ url: Api.ItemGroup });
};

/**
 * @description: 物料配置
 */

export const itemConfig = () => {
  const { http } = useBridge();
  return http.get({ url: Api.Config });
};

/**
 * @description: 删除物料
 */

export function deleteItem(itemCode: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.DeleteItem + itemCode,
  });
}

/**
 * @description: 删除物料组
 */

export function deleteItemGroup(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.DeleteItemGroup + code,
  });
}

/**
 * @description: 新增物料
 */

export const addItem = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddItem, params });
};

/**
 * @description: 编辑物料
 */

export function editItem(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.EditItem, params });
}

/**
 * @description: 新增物料组
 */

export const addItemGroup = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddItemGroup, params });
};

/**
 * @description: 编辑物料组
 */

export function editItemGroup(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.EditItemGroup, params });
}

/**
 * @description: 获取物料提前期
 */

export const getLeadtime = (itemCode: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.GetLeadtime + itemCode });
};

/**
 * @description: 新增物料提前期
 */

export const addLeadtime = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddLeadtime, params }, { successMessageMode: 'none' });
};

/**
 * @description: 编辑物料 提前期
 */

export function editLeadtime(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.EditLeadtime, params }, { successMessageMode: 'none' });
}

/**
 * @description: 获取物料采购
 */
export const getPurchase = (itemCode: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.GetPurchase + itemCode });
};

/**
 * @description: 新增物料采购
 */
export const addPurchase = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddPurchase, params }, { successMessageMode: 'none' });
};

/**
 * @description: 编辑物料采购
 */
export function editPurchase(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.EditPurchase, params }, { successMessageMode: 'none' });
}

/**
 * @description: 获取物料仓储属性
 */
export const getStore = (itemCode: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.GetStore + itemCode });
};

/**
 * @description: 新增物料仓储属性
 */
export const addStore = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddStore, params }, { successMessageMode: 'none' });
};
/**
 * @description: 编辑物料仓储属性
 */
export function editStore(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.EditStore, params }, { successMessageMode: 'none' });
}

/**
 * @description: 获取物料材料属性
 */
export const getMaterial = (itemCode: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.GetMaterial + itemCode });
};

/**
 * @description: 新增物料材料属性
 */
export const addMaterial = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddMaterial, params }, { successMessageMode: 'none' });
};
/**
 * @description: 编辑物料材料属性
 */
export function editMaterial(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.EditMaterial, params }, { successMessageMode: 'none' });
}

/**
 * @description: 获取物料质量
 */
export const getQuality = (itemCode: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.GetQuality + itemCode });
};
/**
 * @description: 新增物料质量
 */
export const addQuality = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddQuality, params }, { successMessageMode: 'none' });
};
/**
 * @description: 编辑物料质量
 */
export function editQuality(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.EditQuality, params }, { successMessageMode: 'none' });
}
/**
 * @description: 获取物料设计文件
 */
export const getDesignfile = (itemCode: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.GetDesignfile + itemCode });
};
/**
 * @description: 新增物料设计文件
 */
export const addDesignfile = (params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    {
      url: Api.AddDesignfile,
      params,
    },
    { isTransformResponse: false, successMessageMode: 'none' },
  );
};
/**
 * @description: 编辑物料设计文件
 */
export function editDesignfile(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.EditDesignfile, params }, { successMessageMode: 'none' });
}

/**
 * @description: 获取物料计划
 */
export const getPlan = (itemCode: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.GetPlan + itemCode });
};

/**
 * @description: 新增物料计划
 */
export const addPlan = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddPlan, params }, { successMessageMode: 'none' });
};

/**
 * @description: 编辑物料计划
 */
export function editPlan(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.EditPlan, params }, { successMessageMode: 'none' });
}
