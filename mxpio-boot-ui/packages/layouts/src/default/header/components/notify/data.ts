export interface ListItem {
  id: string;
  // avatar: string;
  // // 通知的标题内容
  // title: string;
  // // 是否在标题上显示删除线
  // titleDelete?: boolean;
  // datetime: string;
  // type: string;
  // read?: boolean;
  // description: string;
  // clickClose?: boolean;
  // extra?: string;
  // color?: string;
  createBy?: string;
  createDept?: string;
  createTime?: string;
  fromNickName?: string;
  fromUserName?: string;
  messageContent?: string;
  messageTitle?: string;
  readStatus?: string;
  toNickName?: string;
  toUserName?: string;
}

export interface TabItem {
  key: string;
  name: string;
  list: ListItem[];
  unreadlist?: ListItem[];
}
