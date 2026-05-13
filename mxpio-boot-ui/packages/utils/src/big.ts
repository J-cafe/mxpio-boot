import Big from 'big.js';

// 导出原始 Big 类，保持兼容性
export { Big };

// Big 可接受的输入类型
export type BigSource = number | string | Big;

/**
 * 将值转换为 Big 实例，自动处理 null/undefined
 */
function toBig(value: BigSource | null | undefined): Big {
  return new Big(value ?? 0);
}

/**
 * 精确加法
 * @param args 要相加的值，自动处理 null/undefined
 * @returns 结果数值
 */
export function plus(...args: (BigSource | null | undefined)[]): number {
  if (args.length === 0) return 0;
  return args.reduce((acc: Big, val) => acc.plus(val ?? 0), toBig(args[0])).toNumber();
}

/**
 * 精确减法
 * @param minuend 被减数
 * @param subtrahends 减数（可多个）
 * @returns 结果数值
 * @example
 * minus(10, 3, 2) // 10 - 3 - 2 = 5
 * minus(item.actualQuantity, item.planRejectQuantity, item.actualRejectQuantity)
 */
export function minus(
  minuend: BigSource | null | undefined,
  ...subtrahends: (BigSource | null | undefined)[]
): number {
  return subtrahends.reduce((acc: Big, val) => acc.minus(val ?? 0), toBig(minuend)).toNumber();
}

/**
 * 精确乘法
 * @param args 要相乘的值
 * @returns 结果数值
 */
export function times(...args: (BigSource | null | undefined)[]): number {
  if (args.length === 0) return 0;
  return args.reduce((acc: Big, val) => acc.times(val ?? 0), toBig(args[0])).toNumber();
}

/**
 * 精确除法
 * @param dividend 被除数
 * @param divisors 除数（可多个）
 * @returns 结果数值
 */
export function div(
  dividend: BigSource | null | undefined,
  ...divisors: (BigSource | null | undefined)[]
): number {
  if (divisors.length === 0) return toBig(dividend).toNumber();
  return divisors
    .reduce((acc: Big, val) => {
      const divisor = val ?? 0;
      if (Number(divisor) === 0) {
        throw new Error('Division by zero');
      }
      return acc.div(divisor);
    }, toBig(dividend))
    .toNumber();
}

/**
 * 链式计算构建器
 * @example
 * calc(10).plus(5).minus(3).times(2).value() // (10 + 5 - 3) * 2 = 24
 */
export function calc(value: BigSource | null | undefined) {
  let instance = toBig(value);

  return {
    plus: (val: BigSource | null | undefined) => {
      instance = instance.plus(val ?? 0);
      return calc(instance);
    },
    minus: (val: BigSource | null | undefined) => {
      instance = instance.minus(val ?? 0);
      return calc(instance);
    },
    times: (val: BigSource | null | undefined) => {
      instance = instance.times(val ?? 0);
      return calc(instance);
    },
    div: (val: BigSource | null | undefined) => {
      const divisor = val ?? 0;
      if (Number(divisor) === 0) {
        throw new Error('Division by zero');
      }
      instance = instance.div(divisor);
      return calc(instance);
    },
    value: () => instance.toNumber(),
    toString: () => instance.toString(),
    toBig: () => instance,
  };
}

/**
 * 比较两个数值是否相等
 */
export function eq(a: BigSource | null | undefined, b: BigSource | null | undefined): boolean {
  return toBig(a).eq(b ?? 0);
}

/**
 * 比较是否大于
 */
export function gt(a: BigSource | null | undefined, b: BigSource | null | undefined): boolean {
  return toBig(a).gt(b ?? 0);
}

/**
 * 比较是否大于等于
 */
export function gte(a: BigSource | null | undefined, b: BigSource | null | undefined): boolean {
  return toBig(a).gte(b ?? 0);
}

/**
 * 比较是否小于
 */
export function lt(a: BigSource | null | undefined, b: BigSource | null | undefined): boolean {
  return toBig(a).lt(b ?? 0);
}

/**
 * 比较是否小于等于
 */
export function lte(a: BigSource | null | undefined, b: BigSource | null | undefined): boolean {
  return toBig(a).lte(b ?? 0);
}
