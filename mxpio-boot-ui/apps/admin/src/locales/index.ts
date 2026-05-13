import type { App } from 'vue';
import { $t, setupI18n as setupI18nLocales, registerI18nMessages } from '@mxpio/locales';
import { useLocaleStoreWithOut } from '@mxpio/stores';
import { getLocalesLang } from '@mxpio/utils';

async function setupI18n(app: App) {
  const localeStore = useLocaleStoreWithOut();
  // 从 store 中获取当前语言
  const locale = localeStore.getLocale;
  // 获取项目内语言的消息
  const modules = import.meta.glob('./lang/*.ts', { eager: true });
  const messages = getLocalesLang(modules);
  // 注册项目内语言的消息
  registerI18nMessages(messages);

  // 配置项目内语言的消息
  await setupI18nLocales(app, {
    locale: locale,
    messages: {
      ...messages,
    },
  });
}

export { $t, setupI18n };
