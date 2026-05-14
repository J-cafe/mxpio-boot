import { createAsyncComponent } from '@mxpio/utils/src/factory/createAsyncComponent';
import FullScreen from './FullScreen.vue';

export const UserDropDown = createAsyncComponent(() => import('./user-dropdown/index.vue'), {
  loading: true,
});

export const LayoutBreadcrumb = createAsyncComponent(() => import('./Breadcrumb.vue'));

export const Notify = createAsyncComponent(() => import('./notify/index.vue'));

export const ErrorAction = createAsyncComponent(() => import('./ErrorAction.vue'));

export const Home = createAsyncComponent(() => import('./Home.vue'));

export const PlatformDropdown = createAsyncComponent(() => import('./PlatformDropdown.vue'));

export { FullScreen };
