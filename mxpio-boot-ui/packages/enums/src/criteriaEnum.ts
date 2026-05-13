// 查询构造器条件类型
export enum CritertaTypeEnum {
  'AND' = 'AND',
  'OR' = 'OR',
}

// 查询构造器匹配类型
export enum OperatorEnum {
  EQ = 'EQ',
  NE = 'NE',
  LIKE = 'LIKE',
  LIKE_END = 'LIKE_END',
  LIKE_START = 'LIKE_START',
  NOT_LIKE = 'NOT_LIKE',
  NOT_IN = 'NOT_IN',
  GT = 'GT',
  LT = 'LT',
  GE = 'GE',
  LE = 'LE',
  IN = 'IN',
}
