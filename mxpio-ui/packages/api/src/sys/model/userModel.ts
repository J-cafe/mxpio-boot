/**
 * @description: Login interface parameters
 */
export interface LoginParams {
  username?: string;
  password?: string;
  captcha?: string;
  uuid?: string;
  authCode?: string;
  thirdPlatformType?: string;
  // 多租户模式下的租户ID
  organization?: string;
}

export interface RoleInfo {
  actorId: string;
  roleId: string;
}

export interface PermInfo {
  elementId: string;
  hasCriteria: boolean;
  hasFilter: boolean;
  id: string;
  key: string;
  parentId: string;
  path: string;
  resourceType: string;
  title: string;
}

/**
 * @description: Login interface return value
 */
export interface LoginResultModel {
  userId: string | number;
  token: string;
  refreshToken: string;
  roles: RoleInfo[];
}

/**
 * @description: Get user information return value
 */
export interface GetUserInfoModel {
  roles: RoleInfo[];
  authorities: RoleInfo[];
  // 用户id
  userId: string | number;
  // 用户名
  realName: string;
  //用户名， 两个都可以用
  username: string;
  // 真实名字
  nickname: string;
  // 头像
  avatar: string;
  // 介绍
  desc?: string;

  pwdExpiredFlag?: boolean;
}

/**
 * @description: Get user information return value
 */
export interface RefreshTokenResult {
  token: string;
  refreshToken: string;
}

export interface UserItem {
  //用户名， 两个都可以用
  username: string;
  // 真实名字
  nickname: string;
  // 头像
  avatar: string;
  // 邮箱
  email?: string;
  // 电话
  phone?: string;
  // 是否可用
  accountNonLocked?: string;
  // 是否管理员
  administrator?: string;
}

/**
 * @description: Get User return value
 */
export type getUserListResultModel = UserItem[];

/**
 * @description: 获取用户权限返回值
 */
export interface PermResult {
  allDatas: PermInfo[];
  datas: PermInfo[];
}
