import { genMessage } from '@mxpio/locales';

const modules = import.meta.glob('./en/*.{json,ts,js}', { eager: true });
export default {
  message: {
    ...genMessage(modules as Recordable<Recordable>, 'en'),
  },
};
