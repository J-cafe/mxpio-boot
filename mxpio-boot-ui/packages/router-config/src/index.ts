import PAGES from './component';

export * from './helper/menuHelper';
export * from './helper/routeHelper';
export * from './helper/resetHelper';
export * from './routes/basic';
export * from './routes/index';

export { PAGES };

export function registerRouter(options: any) {
  const { pages } = options;
  PAGES.setPages(pages);
}
