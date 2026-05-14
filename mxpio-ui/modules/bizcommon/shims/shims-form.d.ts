import type WorkShopSelect from './src/components/form/WorkShopSelect/index.vue';
import type WorkCenterSelect from './src/components/form/WorkCenterSelect/index.vue';
import type WorkUnitSelect from './src/components/form/WorkUnitSelect/index.vue';
import type WorkTeamSelect from './src/components/form/WorkTeamSelect/index.vue';
import type ItemGroupSelect from './src/components/form/ItemGroupSelect/index.vue';
import type MaterialSelect from './src/components/form/MaterialSelect/index.vue';
import type WareHouseSelect from './src/components/form/WareHouseSelect/index.vue';
import type CustomerSelect from './src/components/form/CustomerSelect/index.vue';
import type SupplySelect from './src/components/form/SupplySelect/index.vue';
import type ProcInfoSelect from './src/components/form/ProcInfoSelect/index.vue';
import type ProcGroupSelect from './src/components/form/ProcGroupSelect/index.vue';
import type QualityTemplateSelect from './src/components/form/QualityTemplateSelect/index.vue';
import type AreaSelect from './src/components/form/AreaSelect/index.vue';
import type EqpCategorySelect from './src/components/form/EqpCategorySelect/index.vue';
import type EqpGroupSelect from './src/components/form/EqpGroupSelect/index.vue';
import type EqpInfoSelect from './src/components/form/EqpInfoSelect/index.vue';
import type EqpBasicsSelect from './src/components/form/EqpBasicsSelect/index.vue';
// 提取自定义组件的 props 类型
type WorkShopSelect = InstanceType<typeof WorkShopSelect>['$props'];
type WorkCenterSelect = InstanceType<typeof WorkCenterSelect>['$props'];
type WorkUnitSelect = InstanceType<typeof WorkUnitSelect>['$props'];
type WorkTeamSelect = InstanceType<typeof WorkTeamSelect>['$props'];
type ItemGroupSelect = InstanceType<typeof ItemGroupSelect>['$props'];
type MaterialSelect = InstanceType<typeof MaterialSelect>['$props'];
type WareHouseSelect = InstanceType<typeof WareHouseSelect>['$props'];
type CustomerSelect = InstanceType<typeof CustomerSelect>['$props'];
type SupplySelect = InstanceType<typeof SupplySelect>['$props'];
type ProcInfoSelect = InstanceType<typeof ProcInfoSelect>['$props'];
type ProcGroupSelect = InstanceType<typeof ProcGroupSelect>['$props'];
type QualityTemplateSelect = InstanceType<typeof QualityTemplateSelect>['$props'];
type AreaSelect = InstanceType<typeof AreaSelect>['$props'];
type EqpCategorySelect = InstanceType<typeof EqpCategorySelect>['$props'];
type EqpGroupSelect = InstanceType<typeof EqpGroupSelect>['$props'];
type EqpInfoSelect = InstanceType<typeof EqpInfoSelect>['$props'];
type EqpBasicsSelect = InstanceType<typeof EqpBasicsSelect>['$props'];

// 扩展公共组件库的 ComponentProps 接口
declare module '@mxpio/components' {
  interface ComponentProps {
    // 添加自定义组件的类型定义
    WorkShopSelect: WorkShopSelect;
    WorkCenterSelect: WorkCenterSelect;
    WorkUnitSelect: WorkUnitSelect;
    WorkTeamSelect: WorkTeamSelect;
    ItemGroupSelect: ItemGroupSelect;
    MaterialSelect: MaterialSelect;
    WareHouseSelect: WareHouseSelect;
    CustomerSelect: CustomerSelect;
    SupplySelect: SupplySelect;
    ProcInfoSelect: ProcInfoSelect;
    ProcGroupSelect: ProcGroupSelect;
    QualityTemplateSelect: QualityTemplateSelect;
    AreaSelect: AreaSelect;
    EqpCategorySelect: EqpCategorySelect;
    EqpGroupSelect: EqpGroupSelect;
    EqpInfoSelect: EqpInfoSelect;
    EqpBasicsSelect: EqpBasicsSelect;
  }
}
