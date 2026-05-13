import { addProfile, editProfile, profileList } from '@mxpio/api';
import { useBridge } from '@mxpio/bridge';
import { OperatorEnum } from '@mxpio/enums';
import { useUserStore } from '@mxpio/stores';
import Criteria from '@mxpio/utils/src/criteria';

export interface ProfileOptions {
  userId: string;
  pageKey: string;
  elementKey?: string;
  [key: string]: any;
}

class UserProfile {
  profileMap: Map<any, any>;
  profileAjax: Map<any, any>;
  profileAwaitAjax: Map<any, any>;
  constructor() {
    this.profileMap = new Map(); // 后台存储的配置
    this.profileAjax = new Map(); // 正在请求的方法
    this.profileAwaitAjax = new Map(); // 正在等待请求的方法
  }
  setProfile(data: ProfileOptions[]) {
    // 前台按用户、页面、组件暂存数据
    data.forEach((item) => {
      this.profileMap.set(`${item.userId}:${item.pageKey}:${item.elementKey}`, item);
    });
  }
  getProfileByPage(data: ProfileOptions): Promise<ProfileOptions[]> {
    // 请求后台用户配置
    return new Promise((resolve, reject) => {
      const key = `${data.userId}:${data.pageKey}`;
      if (this.profileAjax.get(key)) {
        // 页面请求已发送。不再继续请求,避免重复请求
        const awaitAjax = this.profileAwaitAjax.get(key) || [];
        awaitAjax.push((res) => {
          // 暂存重复的请求方法
          if (res.code === 200) {
            resolve(res);
          } else {
            reject(res);
          }
        });
        this.profileAwaitAjax.set(key, awaitAjax);
      } else {
        // 按页面请求数据
        const param = this.getProfileParams({
          userId: data.userId,
          pageKey: data.pageKey,
        });
        const ajax = profileList(param)
          .then((res) => {
            this.setProfile(res);
            resolve(res || []);
            // 回调暂存的请求方法
            this.profileAwaitAjax.get(key)?.forEach((fn: (res: ProfileOptions[]) => void) => {
              fn(res);
            });
            // 清空页面请求map
            this.profileAjax.delete(key);
            this.profileAwaitAjax.delete(key);
          })
          .catch((err) => {
            reject(err);
          });
        this.profileAjax.set(key, ajax); // 前端缓存正在请求的方法
      }
    });
  }
  getProfileParams(data: Recordable) {
    // 获取查询条件
    const queryParam = new Criteria();
    queryParam.addCriterions(Object.assign({}, data), OperatorEnum.EQ); // 过滤参数转换为jpa支持格式
    const param = Object.assign({}, { criteria: queryParam.getEncode() });
    return param;
  }
}

const profile = new UserProfile();

export function useProfile() {
  function getPageInfo() {
    // 获取当前页面信息
    const { router } = useBridge();
    const userStore = useUserStore();
    return {
      userId: userStore.getUserInfo.username,
      pageKey: router.currentRoute.value.fullPath,
    };
  }
  async function getProfile(data: ProfileOptions) {
    // 获取用户页面组件配置
    const key = `${data.userId}:${data.pageKey}:${data.elementKey}`;
    if (profile.profileMap.get(key)) {
      // 先获取缓存，若已存在，直接返回
      return profile.profileMap.get(key);
    }
    const res = await profile.getProfileByPage(data); // 未获取到缓存，按页面请求用户配置数据
    const profileData = res.filter((item: ProfileOptions) => {
      return item.elementKey === data.elementKey;
    });
    profile.profileMap.set(key, profileData[0] || {});
    return profileData[0] || {};
  }

  // 保存用户配置
  async function saveProfile(data: ProfileOptions) {
    try {
      let res;
      const key = `${data.userId}:${data.pageKey}:${data.elementKey}`;
      const profileData = profile.profileMap.get(key);
      // 若用户未先获取数据，先调用保存接口，会重复产生配置数据，实际使用需要避免
      if (profileData && profileData.id) {
        // 判断是否已存在，若存在调用编辑接。目前这个地方会有bug
        res = await editProfile([
          {
            ...data,
            id: profile.profileMap.get(key).id,
          },
        ]);
      } else {
        // 调用新增接口
        res = await addProfile([data]);
      }
      profile.setProfile(res);
      return res;
    } catch (error) {
      console.log(error);
    }
  }

  function restoreStore(data): Promise<Recordable> {
    const pageInfo = getPageInfo();
    const elementKey = data.id;
    // 从服务端调用接口获取当前用户表格自定义列数据，支持异步，返回 Promise
    return new Promise((resolve) => {
      getProfile({
        userId: pageInfo.userId,
        pageKey: pageInfo.pageKey,
        elementKey: elementKey,
      })
        .then((profile) => {
          const properties = profile?.properties ? JSON.parse(profile.properties) : {};
          resolve(properties);
        })
        .catch((err) => {
          console.log(err);
          resolve({});
        });
    });
  }

  function updateStore(data) {
    const pageInfo = getPageInfo();
    const elementKey = data.id;
    // 当 storage 启用后，默认会自动保存在浏览器本地 localStorage 里面，可以通过自定义改方法，使用服务端保存
    // 将用户自定义的列数据保存到服务端，支持异步，返回 Promise
    return saveProfile({
      properties: JSON.stringify(data.storeData),
      userId: pageInfo.userId,
      pageKey: pageInfo.pageKey,
      elementKey: elementKey,
    });
  }

  return {
    getProfile,
    saveProfile,
    restoreStore,
    updateStore,
  };
}
