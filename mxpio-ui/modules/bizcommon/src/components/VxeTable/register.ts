import { useVxeComponentRegister } from '@mxpio/components';
import { createAsyncComponent } from '@mxpio/utils';
import ItemGroupSelectRenderer from './renderer/ItemGroupSelect';
import MaterialSelectRenderer from './renderer/MaterialSelect';
import CustomerSelectRenderer from './renderer/CustomerSelect';
import InvLotSelectRenderer from './renderer/InvLotSelect';
import ProcInfoSelectRenderer from './renderer/ProcInfoSelect';
import ProcGroupSelectRenderer from './renderer/ProcGroupSelect';
import QualityTemplateSelectRenderer from './renderer/QualityTemplateSelect';
import SelectRendererOptions from './renderer/SelectRendererOptions';

/**
 * 注册表格组件
 * 组件名称
 * 组件
 * 渲染器实现
 */
export function vxeComponentRegister() {
  useVxeComponentRegister(
    'WorkShopSelect',
    createAsyncComponent(() => import('../Form/WorkShopSelect/index.vue')),
    SelectRendererOptions,
  );
  useVxeComponentRegister(
    'WorkCenterSelect',
    createAsyncComponent(() => import('../Form/WorkCenterSelect/index.vue')),
    SelectRendererOptions,
  );
  useVxeComponentRegister(
    'WorkUnitSelect',
    createAsyncComponent(() => import('../Form/WorkUnitSelect/index.vue')),
    SelectRendererOptions,
  );
  useVxeComponentRegister(
    'WorkTeamSelect',
    createAsyncComponent(() => import('../Form/WorkTeamSelect/index.vue')),
    SelectRendererOptions,
  );

  useVxeComponentRegister(
    'MaterialSelect',
    createAsyncComponent(() => import('../Form/MaterialSelect/index.vue')),
    MaterialSelectRenderer,
  );

  useVxeComponentRegister(
    'ItemGroupSelect',
    createAsyncComponent(() => import('../Form/ItemGroupSelect/index.vue')),
    ItemGroupSelectRenderer,
  );

  useVxeComponentRegister(
    'WareHouseSelect',
    createAsyncComponent(() => import('../Form/WareHouseSelect/index.vue')),
    SelectRendererOptions,
  );

  useVxeComponentRegister(
    'CustomerSelect',
    createAsyncComponent(() => import('../Form/CustomerSelect/index.vue')),
    CustomerSelectRenderer,
  );

  useVxeComponentRegister(
    'InvLotSelect',
    createAsyncComponent(() => import('./components/InvLotSelect/index.vue')),
    InvLotSelectRenderer,
  );

  useVxeComponentRegister(
    'LotExecutedSelect',
    createAsyncComponent(() => import('./components/LotExecutedSelect/index.vue')),
    InvLotSelectRenderer,
  );
  useVxeComponentRegister(
    'ProcInfoSelect',
    createAsyncComponent(() => import('../Form/ProcInfoSelect/index.vue')),
    ProcInfoSelectRenderer,
  );
  useVxeComponentRegister(
    'ProcGroupSelect',
    createAsyncComponent(() => import('../Form/ProcGroupSelect/index.vue')),
    ProcGroupSelectRenderer,
  );
  useVxeComponentRegister(
    'QualityTemplateSelect',
    createAsyncComponent(() => import('../Form/QualityTemplateSelect/index.vue')),
    QualityTemplateSelectRenderer,
  );

  useVxeComponentRegister(
    'EqpCategorySelect',
    createAsyncComponent(() => import('../Form/EqpCategorySelect/index.vue')),
    SelectRendererOptions,
  );
}
