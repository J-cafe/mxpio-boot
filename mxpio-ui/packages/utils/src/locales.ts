/**
 * 处理已加载的语言模块，提取消息内容
 * @param modules 已通过 import.meta.glob() 加载的模块对象
 * @returns 按语言分组的消息对象
 */
export function getLocalesLang(modules: Record<string, any>): Record<string, any> {
  const messages = Object.entries(modules).reduce(
    (acc, [path, mod]: [string, any]) => {
      const lang = path.split('/').pop()?.replace('.ts', '');
      if (lang) acc[lang] = mod.default?.message;
      return acc;
    },
    {} as Record<string, any>,
  );
  return messages;
}
