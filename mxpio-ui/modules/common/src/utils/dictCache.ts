/**
 * 字典缓存服务
 * 自动加载字典数据并提供缓存
 */
import { getDictByCode } from '@mxpio/api';

export type DictItem = {
  label: string;
  value: any;
  disabled?: boolean;
};

class DictCacheService {
  private cache = new Map<string, DictItem[]>();
  private loading = new Map<string, Promise<DictItem[]>>();
  private pendingRenders = new Map<string, (() => void)[]>();

  /**
   * 获取字典选项（带缓存）
   */
  async get(dictCode: string): Promise<DictItem[]> {
    // 如果已缓存，直接返回
    if (this.cache.has(dictCode)) {
      return this.cache.get(dictCode)!;
    }

    // 如果正在加载，返回加载中的 Promise
    if (this.loading.has(dictCode)) {
      return this.loading.get(dictCode)!;
    }

    // 发起加载请求
    const promise = this.fetchDict(dictCode);
    this.loading.set(dictCode, promise);

    try {
      const options = await promise;
      this.cache.set(dictCode, options);
      // 加载完成后，通知所有等待的渲染器重新渲染
      const callbacks = this.pendingRenders.get(dictCode) || [];
      callbacks.forEach((cb) => cb());
      this.pendingRenders.delete(dictCode);
      return options;
    } finally {
      this.loading.delete(dictCode);
    }
  }

  /**
   * 获取字典标签
   */
  async getLabel(dictCode: string, value: any): Promise<string | undefined> {
    const options = await this.get(dictCode);
    const item = options.find((opt) => opt.value === value);
    return item?.label;
  }

  /**
   * 批量获取字典标签（用于多选）
   */
  async getLabels(dictCode: string, values: any[]): Promise<string> {
    const options = await this.get(dictCode);
    return values
      .map((val) => options.find((opt) => opt.value === val)?.label)
      .filter(Boolean)
      .join(',');
  }

  /**
   * 同步获取字典选项（如果未缓存返回 undefined）
   */
  getSync(dictCode: string): DictItem[] | undefined {
    return this.cache.get(dictCode);
  }

  /**
   * 同步获取字典标签（如果未缓存返回 undefined）
   */
  getLabelSync(dictCode: string, value: any): string | undefined {
    const options = this.getSync(dictCode);
    if (!options) return undefined;
    const item = options.find((opt) => opt.value === value);
    return item?.label;
  }

  /**
   * 批量同步获取字典标签（如果未缓存返回 undefined）
   */
  getLabelsSync(dictCode: string, values: any[]): string | undefined {
    const options = this.getSync(dictCode);
    if (!options) return undefined;
    return values
      .map((val) => options.find((opt) => opt.value === val)?.label)
      .filter(Boolean)
      .join(',');
  }

  /**
   * 注册渲染器回调（当字典加载完成后触发）
   */
  onLoaded(dictCode: string, callback: () => void): void {
    if (!this.pendingRenders.has(dictCode)) {
      this.pendingRenders.set(dictCode, []);
    }
    this.pendingRenders.get(dictCode)!.push(callback);
  }

  /**
   * 预加载多个字典
   */
  async preload(dictCodes: string[]): Promise<void> {
    await Promise.all(dictCodes.map((code) => this.get(code)));
  }

  /**
   * 设置字典缓存（手动设置）
   */
  set(dictCode: string, options: DictItem[]): void {
    this.cache.set(dictCode, options);
  }

  /**
   * 清除缓存
   */
  clear(dictCode?: string): void {
    if (dictCode) {
      this.cache.delete(dictCode);
    } else {
      this.cache.clear();
    }
  }

  /**
   * 获取所有已缓存的字典码
   */
  getCacheKeys(): string[] {
    return Array.from(this.cache.keys());
  }

  /**
   * 从 API 获取字典数据
   */
  private async fetchDict(dictCode: string): Promise<DictItem[]> {
    try {
      const res = await getDictByCode(dictCode);
      if (Array.isArray(res?.items)) {
        return res.items
          .sort((a, b) => a.itemSort - b.itemSort)
          .map((item) => ({
            label: item.itemText,
            value: item.itemValue,
            disabled: item.itemStatus === '0',
          }));
      }
      return [];
    } catch (error) {
      console.warn(`[DictCache] Failed to fetch dict: ${dictCode}`, error);
      return [];
    }
  }
}

// 导出单例
export const dictCache = new DictCacheService();
