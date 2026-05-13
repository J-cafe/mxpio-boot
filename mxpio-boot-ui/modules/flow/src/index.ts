import BPMNTaskModal from './views/task/BPMNTaskModal.vue';
import { registerCustomForms } from '@mxpio/components';
import CustomOrderForm from './example/custom/CustomOrderForm.vue';
import CustomNodeForm from './example/custom/CustomNodeForm.vue';

const flow = {
  bpmn: () => import('./views/bpmn/index.vue'),
  form: () => import('./views/form/index.vue'),
  processStart: () => import('./views/processStart/index.vue'),
  myTask: () => import('./views/task/index.vue'),
  myApply: () => import('./views/myApply/index.vue'),
  process: () => import('./views/process/index.vue'),
};

export default flow;

export { BPMNTaskModal };

registerCustomForms([
  {
    key: 'CustomOrderForm',
    label: '自定义业务单发起单据',
    component: CustomOrderForm,
  },
  {
    key: 'CustomNodeForm',
    label: '自定义业务单节点表单',
    component: CustomNodeForm,
  },
]);
