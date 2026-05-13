import { defineStore } from 'pinia';
import { store } from '../pinia';
import { TABLE_SETTING_KEY } from '@mxpio/enums';

import { Persistent } from '@mxpio/utils';

import type { TableSetting } from '@mxpio/types';
// xys 202406
// import type { SizeType, ColumnOptionsType } from '@/components/Table/src/types/table';
import { FixedType } from 'ant-design-vue/es/vc-table/interface';

type SizeType = 'default' | 'middle' | 'small' | 'large';

export interface ColumnOptionsType {
  value: string;
  label: string;
  //
  column: {
    defaultHidden?: boolean;
  };
  //
  fixed?: FixedType;
}

export interface TableSettingState {
  setting: Nullable<Partial<TableSetting>>;
}

export const useTableSettingStore = defineStore({
  id: 'table-setting',
  state: (): TableSettingState => ({
    setting: Persistent.getLocal(TABLE_SETTING_KEY),
  }),
  getters: {
    getTableSetting(state): Nullable<Partial<TableSetting>> {
      return state.setting;
    },
    //
    getTableSize(state) {
      return state.setting?.size || 'middle';
    },
    //
    getShowIndexColumn(state) {
      return (routerName: string) => {
        return state.setting?.showIndexColumn?.[routerName];
      };
    },
    //
    getShowRowSelection(state) {
      return (routerName: string) => {
        return state.setting?.showRowSelection?.[routerName];
      };
    },
    //
    getColumns(state) {
      return (routerName: string) => {
        return state.setting?.columns && state.setting?.columns[routerName]
          ? state.setting?.columns[routerName]
          : null;
      };
    },
  },
  actions: {
    setTableSetting(setting: Partial<TableSetting>) {
      this.setting = Object.assign({}, this.setting, setting);
      Persistent.setLocal(TABLE_SETTING_KEY, this.setting, true);
    },
    resetTableSetting() {
      Persistent.removeLocal(TABLE_SETTING_KEY, true);
      this.setting = null;
    },
    //
    setTableSize(size: SizeType) {
      this.setTableSetting(
        Object.assign({}, this.setting, {
          size,
        }),
      );
    },
    //
    setShowIndexColumn(routerName: string, show: boolean) {
      this.setTableSetting(
        Object.assign({}, this.setting, {
          showIndexColumn: {
            ...this.setting?.showIndexColumn,
            [routerName]: show,
          },
        }),
      );
    },
    //
    setShowRowSelection(routerName: string, show: boolean) {
      this.setTableSetting(
        Object.assign({}, this.setting, {
          showRowSelection: {
            ...this.setting?.showRowSelection,
            [routerName]: show,
          },
        }),
      );
    },
    //
    setColumns(routerName: string, columns: Array<ColumnOptionsType>) {
      this.setTableSetting(
        Object.assign({}, this.setting, {
          columns: {
            ...this.setting?.columns,
            [routerName]: columns,
          },
        }),
      );
    },
    clearColumns(routerName: string) {
      this.setTableSetting(
        Object.assign({}, this.setting, {
          columns: {
            ...this.setting?.columns,
            [routerName]: undefined,
          },
        }),
      );
    },
  },
});

export function useTabelSettingStoreWithOut() {
  return useTableSettingStore(store);
}
