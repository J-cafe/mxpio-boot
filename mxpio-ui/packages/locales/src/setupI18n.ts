import type { App } from 'vue';
import type { I18nOptions } from 'vue-i18n';
import type { LocaleType } from '@mxpio/types';
import { mergeWith } from 'lodash-es';
import { createI18n } from 'vue-i18n';

const i18n = createI18n({
  globalInjection: true,
  legacy: false,
  locale: '',
  messages: {},
});

// 新增全局消息存储
const customMessages = new Map<LocaleType, Recordable>();

function setHtmlPageLang(locale: LocaleType) {
  i18n.global.locale.value = locale;
  document.querySelector('html')?.setAttribute('lang', locale);
}

// setup i18n instance with glob
async function setupI18n(app: App, options: I18nOptions) {
  const { locale = 'zh_CN', messages } = options;
  app.use(i18n);
  registerI18nMessages(messages as Record<LocaleType, Recordable>);

  loadLocaleMessages(locale as LocaleType);
}

async function getLocaleMessages(locale: LocaleType) {
  const modules = import.meta.glob('./lang/*.ts', { eager: true });
  const defaultLocal: any = modules[`./lang/${locale}.ts`];
  const baseMessage = defaultLocal.default?.message ?? {};

  const message = {
    ...baseMessage,
    ...(customMessages.get(locale) ?? {}),
  };
  return message;
}

async function loadLocaleMessages(lang: LocaleType) {
  // if (i18n.global.locale.value === lang) {
  //   return setHtmlPageLang(lang);
  // }

  const messages = await getLocaleMessages(lang);
  // 合并自定义消息
  // const mergedMessages = mergeWith(baseMessage, customMessages.get(lang) || {});
  // 注册自定义语言包
  // i18n.global.mergeLocaleMessage(lang, mergedMessages);
  i18n.global.setLocaleMessage(lang, messages);
  return setHtmlPageLang(lang);
}

// 设置自定义语言包
// 新增批量注册方法
function registerI18nMessages(messages: Record<LocaleType, Recordable>) {
  Object.entries(messages).forEach(([lang, msg]) => {
    const locale = lang as LocaleType;

    // 维护可用语言列表
    if (!i18n.global.availableLocales.includes(locale)) {
      i18n.global.availableLocales.push(locale);
    }

    // 深度合并消息
    const existing = customMessages.get(locale) || {};
    customMessages.set(locale, mergeWith(existing, msg));

    // 如果当前语言已激活则立即更新
    if (i18n.global.locale.value === locale) {
      // const base = i18n.global.getLocaleMessage(locale);
      // i18n.global.setLocaleMessage(locale, deepMerge(base, msg));
      i18n.global.mergeLocaleMessage(locale, msg);
    }
  });
}

export { i18n, setupI18n, registerI18nMessages, getLocaleMessages };
