import { i18n, setupI18n, registerI18nMessages, getLocaleMessages } from './setupI18n';

export { genMessage, loadLocalePool, setHtmlPageLang } from './helper';

const $t: any = i18n.global.t;
const $te = i18n.global.te;

export { $t, $te, i18n, registerI18nMessages, setupI18n, getLocaleMessages };
