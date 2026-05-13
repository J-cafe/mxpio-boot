import { useBridge } from '@mxpio/bridge';

enum Api {
  // The address does not exist
  Error = '/error',
}

/**
 * @description: Trigger ajax error
 */

export const fireErrorApi = () => {
  const { http } = useBridge();
  return http.get({ url: Api.Error });
};
