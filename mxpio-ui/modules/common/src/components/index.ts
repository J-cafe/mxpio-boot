import { formComponentRegister } from './Form/register';
import { vxeComponentRegister } from './VxeTable/register';
import { vFormDesignRegister } from './VFormDesign/index';

export function registerComponent() {
  formComponentRegister();
  vxeComponentRegister();
  vFormDesignRegister();
}
