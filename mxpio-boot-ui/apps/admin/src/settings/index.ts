// 项目内自定义配置项，后续根据实际项目需要添加
const modules = import.meta.glob('./modules/**/*.ts', { eager: true });

const setting: Recordable = {};
Object.keys(modules).forEach((key) => {
  const mod = (modules as Recordable)[key] || {};
  Object.assign(setting, mod);
});

export default setting;
