import { ErrorTypeEnum, MenuModeEnum, MenuTypeEnum } from '@mxpio/enums';

export type FixedType = 'left' | 'right' | boolean;
export type SizeType = 'default' | 'middle' | 'small' | 'large';

export interface RoleInfo {
  actorId: string;
  roleId: string;
}

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

// Lock screen information
export interface LockInfo {
  // Password required
  pwd?: string | undefined;
  // Is it locked?
  isLock?: boolean;
}

export interface ApiAddress {
  key: string;
  val: string;
}

// Error-log information
export interface ErrorLogInfo {
  // Type of error
  type: ErrorTypeEnum;
  // Error file
  file: string;
  // Error name
  name?: string;
  // Error message
  message: string;
  // Error stack
  stack?: string;
  // Error detail
  detail: string;
  // Error url
  url: string;
  // Error time
  time?: string;
}

// Department information
export interface DeptInfo {
  deptCode: string;
  deptName: string;
  deptType?: string;
  deptLevel?: string;
  deptManager?: string;
}

export interface UserInfo {
  userId: string | number;
  username: string;
  realName: string;
  nickname: string;
  phone?: string;
  email?: string;
  introduction?: string;
  avatar: string;
  avatarImage?: string;
  desc?: string;
  rank?: string;
  postId?: string;
  homePath?: string;
  roles: RoleInfo[];
  authorities: RoleInfo[];
  pwdExpiredFlag?: boolean;
  dept?: DeptInfo;
}

export interface BeforeMiniState {
  menuCollapsed?: boolean;
  menuSplit?: boolean;
  menuMode?: MenuModeEnum;
  menuType?: MenuTypeEnum;
}

export interface TableSetting {
  size: Nullable<SizeType>;
  showIndexColumn: Recordable<Nullable<boolean>>;
  columns: Recordable<Nullable<Array<ColumnOptionsType>>>;
  showRowSelection: Recordable<Nullable<boolean>>;
}
