import { CritertaTypeEnum, OperatorEnum } from '@mxpio/enums';
import type { RequestCriteriaParams, VxeGridPropTypes, VxeTableDefines } from '@mxpio/types';

import { isArray, isNil, omitBy } from 'lodash-es';

export interface Params {
  criterions: Criterions[];
  orders: Orders[];
}

export interface Orders {
  fieldName: string;
  desc: boolean;
}

export interface Criterions<T = any> {
  fieldName?: string;
  operator?: OperatorEnum;
  value?: T;
  type?: CritertaTypeEnum;
  criterions?: Criterions[];
}

export interface QueryParamParams {
  page?: string | number | undefined;
  size?: string | number | undefined;
  field?: string;
  order?: string;
  [key: string]: any;
}

export interface VxeTableQueryParams {
  page?: VxeGridPropTypes.ProxyAjaxQueryPageParams;
  form?: {
    [key: string]: any;
  };
  sorts?: VxeGridPropTypes.ProxyAjaxQuerySortCheckedParams[];
  filters?: {
    [key: string]: any;
  };
  tableFilters?: VxeTableDefines.FilterCheckedParams[];
  other?: {
    [key: string]: any;
  };
  callback?: (params: Criteria) => Criteria;
}

/**
 * 查询构造器
 */
export default class Criteria {
  #stack: Stack = new Stack();
  param: Params = {
    criterions: [],
    orders: [],
  };

  addCriterion(key: string, operator: any, value: any) {
    const obj = {
      fieldName: key,
      operator: operator,
      value: value,
    };
    if (this.#stack.length() === 0) {
      this.param.criterions.push(obj);
    } else {
      this.#stack.peek()?.criterions?.push(obj);
    }
    return this;
  }
  addOrder({ fieldName, desc }: { fieldName: string; desc?: boolean }) {
    this.param.orders.push({
      fieldName: fieldName,
      desc: desc || false,
    });
    return this;
  }
  or() {
    this.#stack.push({ type: CritertaTypeEnum.OR, criterions: [] });
    return this;
  }
  and() {
    this.#stack.push({ type: CritertaTypeEnum.AND, criterions: [] });
    return this;
  }
  end() {
    const stack = this.#stack.pop();
    if (this.#stack.length() === 0) {
      this.param.criterions.push(stack);
    } else {
      this.#stack.peek().criterions?.push(stack);
    }
    return this;
  }
  concatCriterions(list: Criterions[]) {
    this.param.criterions = this.param.criterions.concat(list);
  }
  addCriterions(param: { [key: string]: any }, operator: OperatorEnum = OperatorEnum.EQ) {
    // 生成单层简单查询条件
    Object.keys(param).forEach((key: string) => {
      if (key.includes('@')) {
        //处理查询类型不一致的情况 使用示例'key@EQ'
        const i = key.indexOf('@');
        const operatorKey: OperatorEnum = key.slice(i + 1, key.length) as OperatorEnum;
        this.param.criterions.push({
          fieldName: key.slice(0, i),
          operator: OperatorEnum[operatorKey],
          value: param[key],
        });
      } else if (isArray(param[key])) {
        //处理范围查询情况，使用示例[1,10]或['2022-01-01','2022-12-31']
        if (param[key][0]) {
          this.param.criterions.push({
            fieldName: key,
            operator: OperatorEnum.GE,
            value: param[key][0],
          });
        }
        if (param[key][1]) {
          this.param.criterions.push({
            fieldName: key,
            operator: OperatorEnum.LE,
            value: param[key][1],
          });
        }
      } else {
        this.param.criterions.push({
          fieldName: key,
          operator: operator,
          value: param[key],
        });
      }
    });
    return this;
  }
  addOrders(order: { [key: string]: boolean }) {
    Object.keys(order).forEach((key) => {
      this.param.orders.push({
        fieldName: key,
        desc: !!order[key],
      });
    });
    return this;
  }
  getEncode() {
    return encodeURIComponent(JSON.stringify(this.param));
  }
}

export class Stack {
  dataStore: Criterions[] = [];
  top: number = 0;
  push(element: Criterions) {
    // 入栈
    this.dataStore[this.top++] = element;
  }
  pop() {
    // 出栈
    return this.dataStore[--this.top];
  }
  peek() {
    // 查看栈顶元素
    return this.dataStore[this.top - 1];
  }
  clear() {
    // 清空栈
    this.top = 0;
  }
  length() {
    // 栈内存放元素的个数
    return this.top;
  }
}

/**
 * 获取查询参数
 * @param params 查询参数对象，包括分页、排序和其他查询条件
 * @param filters 额外的过滤条件
 * @returns 返回构造的查询参数对象，包括分页信息、排序规则和编码后的查询条件
 */
export function getQueryParams(params: QueryParamParams, filters?: {}) {
  const queryParam = new Criteria();
  // 初始化分页信息
  let ipagination: {
    pageNo?: string | number | undefined;
    pageSize?: string | number | undefined;
  } = {};
  if (params.page || params.size) {
    ipagination = {
      pageNo: params.page,
      pageSize: params?.size,
    };
    delete params.page; // 删除已经处理的分页参数
    delete params.size;
  }

  // 处理排序条件
  if (params.field) {
    queryParam.addOrder({
      // 根据参数添加排序规则
      fieldName: params.field,
      desc: params.order === 'descend',
    });
    delete params.field; // 删除已经处理的排序字段参数
    delete params.order;
  }

  // 组装查询条件
  const query = { ...params };
  queryParam.addCriterions(
    Object.assign({}, omitBy(query, isNil), omitBy(filters, isNil)),
    OperatorEnum.LIKE,
  ); // 将查询参数转换为JPA支持的格式

  // 最终构造返回的参数对象
  const param: RequestCriteriaParams = Object.assign(
    ipagination.pageNo
      ? {
          pageNo: ipagination.pageNo,
          pageSize: ipagination.pageSize,
        }
      : {},
    { criteria: queryParam.getEncode() },
  );
  return param;
}

/**
 * 获取查询参数
 * @param page 分页信息
 * @param form 查询信息
 * @param sorts 排序信息
 * @param filters 额外的过滤条件
 * @returns 返回构造的查询参数对象，包括分页信息、排序规则和编码后的查询条件
 */
export function getVxeTableQueryParams({
  page,
  form,
  sorts,
  filters,
  tableFilters,
  other = {},
  callback,
}: VxeTableQueryParams) {
  let queryParam = new Criteria();
  // 处理排序条件
  if (sorts?.length) {
    // 根据参数添加排序规则
    sorts.forEach((item) => {
      queryParam.addOrder({
        fieldName: item.field,
        desc: item.order === 'desc',
      });
    });
  }
  const tableFilters_: Recordable = {};
  if (tableFilters) {
    tableFilters.forEach((item) => {
      const operator = item.column.filterRender?.props?.operator;
      operator
        ? (tableFilters_[item.field + '@' + operator] = item.datas[0])
        : (tableFilters_[item.field] = item.datas[0]);
    });
  }
  // 组装查询条件
  const query = { ...form };
  // 合并查询参数，排除空值
  let queryParams = Object.assign(
    {},
    omitBy(query, isNil),
    omitBy(filters, isNil),
    omitBy(tableFilters_, isNil),
  );
  // 排除空数组
  queryParams = omitBy(queryParams, (value) => Array.isArray(value) && value.length === 0);
  queryParam.addCriterions(queryParams, OperatorEnum.LIKE); // 将查询参数转换为JPA支持的格式
  if (callback && typeof callback === 'function') {
    queryParam = callback(queryParam);
  }
  // 最终构造返回的参数对象
  const param: RequestCriteriaParams = Object.assign(
    {
      pageNo: page?.currentPage,
      pageSize: page?.pageSize,
    },
    { ...other },
    { criteria: queryParam.getEncode() },
  );
  return param;
}
