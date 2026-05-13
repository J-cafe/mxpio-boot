import type { Component } from '@mxpio/types';

type PagesType = {
  LAYOUT: Component | string;
  EXCEPTION_COMPONENT?: Component;
  IFRAME?: Component;
  [key: string]: Component;
};

class Pages {
  pages: PagesType = {
    LAYOUT: undefined,
    EXCEPTION_COMPONENT: undefined,
    IFRAME: undefined,
  };
  setPages(options: PagesType) {
    this.pages = options;
  }
  getPage(pageKey: string): Component {
    return this.pages[pageKey]?.();
  }
  hasPage(pageKey: string): boolean {
    return !!this.pages[pageKey];
  }
}

const PAGES = new Pages();

export default PAGES;
