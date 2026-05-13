import { useAppStore, useAppStoreWithOut } from './modules/app';
import { useErrorLogStore, useErrorLogStoreWithOut } from './modules/errorLog';
import { useLocaleStore, useLocaleStoreWithOut } from './modules/locale';
import { useLockStore, useLockStoreWithOut } from './modules/lock';
import { useMultipleTabStore, useMultipleTabStoreWithOut } from './modules/multipleTab';
import { usePermissionStore, usePermissionStoreWithOut } from './modules/permission';
import { useSystemStore, useSystemStoreWithOut } from './modules/system';
import { useTableSettingStore, useTabelSettingStoreWithOut } from './modules/tableSetting';
import { useUserStore, useUserStoreWithOut } from './modules/user';

export * from './pinia';

// interface StoreRegistry {
//   useAppStore: typeof useAppStore;
//   useErrorLogStore: typeof useErrorLogStore;
//   useLocaleStore: typeof useLocaleStore;
//   useLockStore: typeof useLockStore;
//   useMultipleTabStore: typeof useMultipleTabStore;
//   usePermissionStore: typeof usePermissionStore;
//   useSystemStore: typeof useSystemStore;
//   useTableSettingStore: typeof useTableSettingStore;
//   useUserStore: typeof useUserStore;
//   useAppStoreWithOut: typeof useAppStoreWithOut;
//   useErrorLogStoreWithOut: typeof useErrorLogStoreWithOut;
//   useLocaleStoreWithOut: typeof useLocaleStoreWithOut;
//   useMultipleTabStoreWithOut: typeof useMultipleTabStoreWithOut;
//   usePermissionStoreWithOut: typeof usePermissionStoreWithOut;
//   useUserStoreWithOut: typeof useUserStoreWithOut;
//   useSystemStoreWithOut: typeof useSystemStoreWithOut;
//   [key: string]: (...args: any[]) => any;
// }

// // 自动推导Store实例类型
// export type StoreManager = {
//   [K in keyof StoreRegistry]: ReturnType<StoreRegistry[K]>;
// };

// // 新增实例缓存机制（避免重复创建Store实例）
// class Stores {
//   storeInstance: StoreRegistry = {} as StoreRegistry;
//   private instanceCache: Partial<StoreManager> = {};

//   // 注册Store定义函数
//   registerStore<K extends keyof StoreRegistry>(key: K, useStore: StoreRegistry[K]) {
//     this.storeInstance[key] = useStore;
//   }

//   // 获取Store定义函数（原功能保留）
//   getStore<K extends keyof StoreRegistry>(key: K): StoreRegistry[K] {
//     return this.storeInstance[key];
//   }

//   // 新增：获取Store实例（带缓存）
//   getStoreInstance<K extends keyof StoreRegistry>(key: K): StoreManager[K] {
//     if (!this.instanceCache[key]) {
//       const useStore = this.getStore(key);
//       this.instanceCache[key] = useStore();
//     }
//     return this.instanceCache[key]!;
//   }
// }

// // 实例化时绑定类型（与 StoreRegistry 强关联）
// const storesInstance = new Stores();

// // 批量注册配置（替代手动逐个调用，减少冗余）
// const STORE_REGISTRY: Array<[keyof StoreRegistry, StoreRegistry[keyof StoreRegistry]]> = [
//   ['useAppStore', useAppStore],
//   ['useErrorLogStore', useErrorLogStore],
//   ['useLocaleStore', useLocaleStore],
//   ['useLockStore', useLockStore],
//   ['useMultipleTabStore', useMultipleTabStore],
//   ['usePermissionStore', usePermissionStore],
//   ['useSystemStore', useSystemStore],
//   ['useTableSettingStore', useTableSettingStore],
//   ['useUserStore', useUserStore],
//   ['useAppStoreWithOut', useAppStoreWithOut],
//   ['useErrorLogStoreWithOut', useErrorLogStoreWithOut],
//   ['useLocaleStoreWithOut', useLocaleStoreWithOut],
//   ['useMultipleTabWithOutStore', useMultipleTabStoreWithOut],
//   ['usePermissionStoreWithOut', usePermissionStoreWithOut],
//   ['useUserStoreWithOut', useUserStoreWithOut],
//   ['useSystemStoreWithOut', useSystemStoreWithOut],
//   ['useLockStoreWithOut', useLockStoreWithOut],
//   ['useTabelSettingStoreWithOut', useTabelSettingStoreWithOut],
// ];

// // 批量注册 Store
// function install(storesInstance: Stores) {
//   STORE_REGISTRY.forEach(([key, useStore]) => {
//     storesInstance.registerStore(key, useStore);
//   });
// }

// install(storesInstance);

// export const stores = {
//   useAppStore,
//   useErrorLogStore,
//   useLocaleStore,
//   useMultipleTabStore,
//   usePermissionStore,
//   useSystemStore,
//   useTableSettingStore,
//   useUserStore,
//   useErrorLogStoreWithOut,
//   useLocaleStoreWithOut,
//   useMultipleTabStoreWithOut,
//   usePermissionStoreWithOut,
//   useSystemStoreWithOut,
//   useUserStoreWithOut,
// };

// export { storesInstance };

export {
  useAppStore,
  useErrorLogStore,
  useLocaleStore,
  useMultipleTabStore,
  usePermissionStore,
  useSystemStore,
  useTableSettingStore,
  useUserStore,
  useLockStore,
  useErrorLogStoreWithOut,
  useLocaleStoreWithOut,
  useMultipleTabStoreWithOut,
  usePermissionStoreWithOut,
  useSystemStoreWithOut,
  useUserStoreWithOut,
  useAppStoreWithOut,
  useLockStoreWithOut,
  useTabelSettingStoreWithOut,
};
