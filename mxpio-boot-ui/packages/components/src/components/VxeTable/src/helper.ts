import { ComponentType } from './componentType';
import { $t } from '@mxpio/locales';

/**
 * @description: 生成placeholder
 */
export function createPlaceholderMessage(component: ComponentType) {
  if (!component) return;
  if (component.includes('RangePicker')) {
    return [$t('common.chooseText'), $t('common.chooseText')];
  }
  if (component.includes('Input') || component.includes('Complete') || component.includes('Rate')) {
    return $t('common.inputText');
  } else {
    return $t('common.chooseText');
  }
}
