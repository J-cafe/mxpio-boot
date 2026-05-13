import type { App } from 'vue';
import { Button } from './components/Button';
import VXETable from 'vxe-table';
import { setupI18n } from './locales';
import { setupVxeI18n } from './components/VxeTable';

// 按需注册antd的组件
import {
  // Button as AButton,
  Select,
  Checkbox,
  DatePicker,
  TimePicker,
  Radio,
  Switch,
  Card,
  Tabs,
  Tree,
  Divider,
  TreeSelect,
  Empty,
  Menu,
  Form,
  Input,
  Row,
  Col,
  Spin,
  Space,
  Slider,
  InputNumber,
  Rate,
  Layout,
} from 'ant-design-vue';

export function registerGlobComp(app: App) {
  app.use(Input).use(Button).use(Layout).use(VXETable);
  app
    .use(Select)
    .use(Checkbox)
    .use(DatePicker)
    .use(TimePicker)
    .use(Radio)
    .use(Switch)
    .use(Card)
    .use(Tree)
    .use(TreeSelect)
    .use(Divider)
    .use(Empty)
    .use(Menu)
    .use(Tabs)
    .use(Form)
    .use(Row)
    .use(Col)
    .use(Spin)
    .use(Space)
    .use(Slider)
    .use(InputNumber)
    .use(Rate);
  // .use(AButton);
}

// 初始化全局组件
export function setupGlobComp(app: App) {
  // 初始化多语言配置
  setupI18n();
  // 初始化vxe-table的多语言配置
  setupVxeI18n();
  // 注册全局组件
  registerGlobComp(app);
}
