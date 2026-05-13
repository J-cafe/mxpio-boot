/**
 * Multi-language related operations
 */
import { getLocaleMessages, i18n, loadLocalePool, setHtmlPageLang } from '@mxpio/locales';
import { useLocaleStoreWithOut } from '@mxpio/stores';
import type { LocaleType } from '@mxpio/types';
import { Locale } from 'ant-design-vue/es/locale';
import type { Ref } from 'vue';
import { computed, unref } from 'vue';

function setI18nLanguage(locale: LocaleType) {
  const localeStore = useLocaleStoreWithOut();
  if (i18n.mode === 'legacy') {
    (i18n.global.locale as unknown as string) = locale;
  } else {
    (i18n.global.locale as Ref<string>).value = locale;
  }
  localeStore.setLocaleInfo({ locale });
  setHtmlPageLang(locale);
}

export function useLocale() {
  const localeStore = useLocaleStoreWithOut();
  const getLocale = computed(() => localeStore.getLocale);
  const getShowLocalePicker = computed(() => localeStore.getShowPicker);

  const getAntdLocale = computed((): any => {
    const localeMessage = i18n.global.getLocaleMessage<{ antdLocale: Locale }>(unref(getLocale));
    return localeMessage?.antdLocale ?? {};
  });

  // Switching the language will change the locale of useI18n
  // And submit to configuration modification
  async function changeLocale(locale: LocaleType) {
    const globalI18n = i18n.global;
    const currentLocale = unref(globalI18n.locale);
    if (currentLocale === locale) {
      return locale;
    }

    if (loadLocalePool.includes(locale)) {
      setI18nLanguage(locale);
      return locale;
    }
    const message = await getLocaleMessages(locale);
    globalI18n.setLocaleMessage(locale, message);
    loadLocalePool.push(locale);

    setI18nLanguage(locale);
    return locale;
  }

  return {
    getLocale,
    getShowLocalePicker,
    changeLocale,
    getAntdLocale,
  };
}
