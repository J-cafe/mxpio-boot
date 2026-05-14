import type { ComponentType } from '../../Form';
import type { IVFormComponent, IBaseComponentProps } from '../typings/v-form-component';
import { componentMap as VbenCmp, add } from '../../Form';
import { componentMap as Cmp } from '../components';
import { Component } from 'vue';

class FormDesignComponents {
  componentFDMap: Map<string, Component>;
  VbenCmp: Map<string, Component>;
  customComponents: IVFormComponent[];
  customAttrs: IBaseComponentProps;
  constructor() {
    // 设计器控件库
    this.componentFDMap = new Map<string, Component>();
    // vben控件库
    this.VbenCmp = new Map<string, Component>();
    // 自定义组件列表
    this.customComponents = [];
    // 自定义组件属性
    this.customAttrs = {};
  }
  initComponentMap() {
    //注册Ant控件库
    Cmp.forEach((value, key) => {
      this.componentFDMap.set(key, value);
      if (VbenCmp[key] == null) {
        add(key as ComponentType, value);
      }
    });

    // //注册vben控件库
    // VbenCmp.forEach((value, key) => {
    //   this.componentFDMap.set(key, value);
    // });
    this.VbenCmp = VbenCmp;
  }
  getComponent(key: string): Component | undefined {
    // 先从设计器控件库中查找
    let component = this.componentFDMap.get(key);
    if (component == null) {
      // 如果设计器控件库中没有找到，则从vben控件库中查找
      component = this.VbenCmp.get(key);
    }
    return component;
  }
  // 添加自定义组件到设计器控件库
  setComponent(key: string, component: Component) {
    this.componentFDMap.set(key, component);
  }
  // 添加自定义组件到自定义组件列表
  addCustomComponents(components: IVFormComponent) {
    this.customComponents.push(components);
  }
  // 添加自定义组件属性到自定义组件属性列表
  addCustomAttrs(attres: IBaseComponentProps) {
    this.customAttrs = Object.assign(this.customAttrs, attres);
  }
}

const formDesignComponents = new FormDesignComponents();

formDesignComponents.initComponentMap();

export default formDesignComponents;
