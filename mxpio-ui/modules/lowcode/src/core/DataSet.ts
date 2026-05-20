/**
 * 客户端 DataSet（对标 Dorado DataSet）
 *
 * 核心职责：
 * 1. 数据容器：维护当前数据、新增/修改/删除记录
 * 2. 数据加载：调用后端 DataProvider API
 * 3. 数据提交：调用后端 DataResolver API（批量保存）
 * 4. 导航：first/last/next/prev + currentRecord
 * 5. 子 DataSet：支持主从表（children Map）
 */

import { useBridge } from '@mxpio/bridge';

export interface DataSetConfig {
  id: string;
  modelCode: string;
  pageSize?: number;
  parentDataSet?: DataSet;
  dataPath?: string;
}

export interface BatchSaveRequest {
  toInsert: Record<string, any>[];
  toUpdate: Record<string, any>[];
  toDelete: string[];
}

export class DataSet {
  id: string;
  modelCode: string;
  data: Record<string, any>[] = [];
  inserted: Record<string, any>[] = [];
  updated: Record<string, any>[] = [];
  deleted: string[] = [];
  currentIndex = -1;
  totalCount = 0;
  pageSize: number;
  currentPage = 0;
  loading = false;
  children: Map<string, DataSet> = new Map();

  // 查询条件
  criteria: any = null;

  constructor(config: DataSetConfig) {
    this.id = config.id;
    this.modelCode = config.modelCode;
    this.pageSize = config.pageSize || 20;
  }

  get currentRecord(): Record<string, any> | null {
    if (this.currentIndex >= 0 && this.currentIndex < this.data.length) {
      return this.data[this.currentIndex];
    }
    return null;
  }

  // === 数据操作 ===

  async load(criteria?: any): Promise<void> {
    this.loading = true;
    this.criteria = criteria || null;
    try {
      const { http } = useBridge();
      const res: any = await http.get({
        url: `/api/lowcode/${this.modelCode}/list`,
        params: criteria
          ? { criteriaJson: JSON.stringify(criteria) }
          : {},
      });
      this.data = res || [];
      this.currentIndex = this.data.length > 0 ? 0 : -1;
    } finally {
      this.loading = false;
    }
  }

  async loadPage(page: number = 0, criteria?: any): Promise<void> {
    this.loading = true;
    this.criteria = criteria || null;
    this.currentPage = page;
    try {
      const { http } = useBridge();
      const res: any = await http.get({
        url: `/api/lowcode/${this.modelCode}/page`,
        params: {
          page,
          size: this.pageSize,
          ...(criteria ? { criteriaJson: JSON.stringify(criteria) } : {}),
        },
      });
      this.data = res?.content || [];
      this.totalCount = res?.totalElements || 0;
      this.currentIndex = this.data.length > 0 ? 0 : -1;
    } finally {
      this.loading = false;
    }
  }

  async loadById(id: string): Promise<Record<string, any> | null> {
    const { http } = useBridge();
    const res: any = await http.get({
      url: `/api/lowcode/${this.modelCode}/${id}`,
    });
    return res;
  }

  async submit(): Promise<void> {
    if (
      this.inserted.length === 0 &&
      this.updated.length === 0 &&
      this.deleted.length === 0
    ) {
      return;
    }
    const { http } = useBridge();
    const body: BatchSaveRequest = {
      toInsert: this.inserted,
      toUpdate: this.updated,
      toDelete: this.deleted,
    };
    await http.post({
      url: `/api/lowcode/${this.modelCode}/batch`,
      data: body,
    });
    // 清空变更状态
    this.inserted = [];
    this.updated = [];
    this.deleted = [];
  }

  insert(record: Record<string, any>): void {
    this.inserted.push({ ...record });
    this.data.push({ ...record });
    this.currentIndex = this.data.length - 1;
  }

  update(id: string, changes: Record<string, any>): void {
    // 先检查是否在插入列表中
    const insertedIdx = this.inserted.findIndex((r) => r['id'] === id);
    if (insertedIdx >= 0) {
      Object.assign(this.inserted[insertedIdx], changes);
    } else {
      const existing = this.updated.find((r) => r['id'] === id);
      if (existing) {
        Object.assign(existing, changes);
      } else {
        this.updated.push({ id, ...changes });
      }
    }
    // 同步更新显示数据
    const dataIdx = this.data.findIndex((r) => r['id'] === id);
    if (dataIdx >= 0) {
      Object.assign(this.data[dataIdx], changes);
    }
  }

  remove(id: string): void {
    this.deleted.push(id);
    this.data = this.data.filter((r) => r['id'] !== id);
    if (this.currentIndex >= this.data.length) {
      this.currentIndex = this.data.length - 1;
    }
  }

  // === 导航 ===

  first(): void {
    this.currentIndex = this.data.length > 0 ? 0 : -1;
  }

  last(): void {
    this.currentIndex = this.data.length > 0 ? this.data.length - 1 : -1;
  }

  next(): void {
    if (this.currentIndex < this.data.length - 1) this.currentIndex++;
  }

  prev(): void {
    if (this.currentIndex > 0) this.currentIndex--;
  }

  // === 子 DataSet ===

  addChild(dataSet: DataSet): void {
    this.children.set(dataSet.id, dataSet);
  }

  getChild(id: string): DataSet | undefined {
    return this.children.get(id);
  }

  /**
   * 加载所有子 DataSet 的数据（基于当前记录的 DataPath）
   * 例如：当前记录是订单，子 DataSet 配置了 dataPath="#.items"
   */
  async loadChildren(): Promise<void> {
    for (const [, child] of this.children) {
      if (this.currentRecord) {
        await child.load();
      }
    }
  }
}
