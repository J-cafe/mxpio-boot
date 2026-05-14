import { registerI18nMessages } from '@mxpio/locales';
import { getLocalesLang } from '@mxpio/utils';

export async function setupI18n() {
  const modules = import.meta.glob('./lang/*.ts', { eager: true });
  const messages = getLocalesLang(modules);
  registerI18nMessages(messages);
}
