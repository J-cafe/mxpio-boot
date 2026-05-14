import { formComponentRegister } from './Form/register';
import { vxeComponentRegister } from './VxeTable/register';

export function registerComponent() {
  formComponentRegister();
  vxeComponentRegister();
}
