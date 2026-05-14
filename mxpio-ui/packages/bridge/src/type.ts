import type { Router } from 'vue-router';
import type { VAxiosOptions } from '@mxpio/types';

export interface BridgeDependencies {
  router: Router;
  http: VAxiosOptions;
  env: Readonly<Record<string, string>>; // 环境变量只读化
}
