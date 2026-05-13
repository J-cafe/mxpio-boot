import { useComponentRegister } from '@mxpio/components';
import { createAsyncComponent } from '@mxpio/utils';
/**
 * 注册表单组件
 * 组件名称
 * 组件
 */
export function formComponentRegister() {
  useComponentRegister(
    'WorkShopSelect',
    createAsyncComponent(() => import('./WorkShopSelect/index.vue')),
  );
  useComponentRegister(
    'WorkCenterSelect',
    createAsyncComponent(() => import('./WorkCenterSelect/index.vue')),
  );
  useComponentRegister(
    'WorkUnitSelect',
    createAsyncComponent(() => import('./WorkUnitSelect/index.vue')),
  );
  useComponentRegister(
    'WorkTeamSelect',
    createAsyncComponent(() => import('./WorkTeamSelect/index.vue')),
  );
  useComponentRegister(
    'MaterialSelect',
    createAsyncComponent(() => import('./MaterialSelect/index.vue')),
  );
  useComponentRegister(
    'ItemGroupSelect',
    createAsyncComponent(() => import('./ItemGroupSelect/index.vue')),
  );
  useComponentRegister(
    'WareHouseSelect',
    createAsyncComponent(() => import('./WareHouseSelect/index.vue')),
  );
  useComponentRegister(
    'CustomerSelect',
    createAsyncComponent(() => import('./CustomerSelect/index.vue')),
  );
  useComponentRegister(
    'SupplySelect',
    createAsyncComponent(() => import('./SupplySelect/index.vue')),
  );
  useComponentRegister(
    'ProcInfoSelect',
    createAsyncComponent(() => import('./ProcInfoSelect/index.vue')),
  );
  useComponentRegister(
    'ProcGroupSelect',
    createAsyncComponent(() => import('./ProcGroupSelect/index.vue')),
  );
  useComponentRegister(
    'QualityTemplateSelect',
    createAsyncComponent(() => import('./QualityTemplateSelect/index.vue')),
  );
  useComponentRegister(
    'AreaSelect',
    createAsyncComponent(() => import('./AreaSelect/index.vue')),
  );
  useComponentRegister(
    'EqpCategorySelect',
    createAsyncComponent(() => import('./EqpCategorySelect/index.vue')),
  );
  useComponentRegister(
    'EqpGroupSelect',
    createAsyncComponent(() => import('./EqpGroupSelect/index.vue')),
  );
  useComponentRegister(
    'EqpInfoSelect',
    createAsyncComponent(() => import('./EqpInfoSelect/index.vue')),
  );
  useComponentRegister(
    'EqpBasicsSelect',
    createAsyncComponent(() => import('./EqpBasicsSelect/index.vue')),
  );
}
