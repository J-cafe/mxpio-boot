/**
 * DataPath 表达式解析器（对标 Dorado DataPath）
 *
 * 语法：
 *   "#"           — 当前记录
 *   "#.items"     — 当前记录的 items 属性（子集合）
 *   "#.dept.employees" — 嵌套导航
 */
export class DataPath {
  static resolve(root: Record<string, any> | null, path: string): any {
    if (!root) return null;
    const parts = path.split('.');
    let current: any = root;
    for (const part of parts) {
      if (part === '#') continue;
      if (current == null) return null;
      current = current[part];
    }
    return current;
  }

  static set(root: Record<string, any>, path: string, value: any): void {
    const parts = path.split('.');
    let current: any = root;
    for (let i = 0; i < parts.length - 1; i++) {
      if (parts[i] === '#') continue;
      if (!(parts[i] in current)) {
        current[parts[i]] = {};
      }
      current = current[parts[i]];
    }
    const last = parts[parts.length - 1];
    if (last !== '#') {
      current[last] = value;
    }
  }
}
