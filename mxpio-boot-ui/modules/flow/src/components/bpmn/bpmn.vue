<template>
  <div class="containers">
    <div class="my-process-designer__header">
      <a-button type="primary" @click="uploadClick" style="margin-right: 5px">打开文件</a-button>
      <a-dropdown>
        <a-button type="primary">下载文件</a-button>
        <template #overlay>
          <a-menu>
            <a-menu-item key="1" @click="downloadProcessAsXml"> 下载为XML文件 </a-menu-item>
            <a-menu-item key="2" @click="downloadProcessAsSvg"> 下载为SVG文件</a-menu-item>
            <a-menu-item key="3" @click="downloadProcessAsBpmn"> 下载为BPMN文件</a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
      <a-button type="primary" style="margin-left: 5px" @click="saveClick">保存</a-button>

      <div style="margin-left: 20px">
        <a-tooltip title="缩小视图">
          <a-button :disabled="zoom < 0.2" @click="handleZoomOut">
            <template #icon>
              <Icon icon="ant-design:zoom-out-outlined" :size="15" />
            </template>
          </a-button>
        </a-tooltip>
        <a-button>{{ Math.floor(zoom * 10 * 10) + '%' }}</a-button>
        <a-tooltip title="放大视图">
          <a-button :disabled="zoom > 4" @click="handleZoomIn">
            <template #icon>
              <Icon icon="ant-design:zoom-in-outlined" :size="15" />
            </template>
          </a-button>
        </a-tooltip>
        <a-tooltip title="重置视图并居中">
          <a-button :disabled="zoom > 4" @click="handleReZoom">
            <template #icon>
              <Icon icon="ant-design:environment-filled" :size="15" />
            </template>
          </a-button>
        </a-tooltip>
      </div>

      <div style="margin-left: 20px">
        <a-tooltip title="撤销">
          <a-button @click="handleUndo">
            <template #icon>
              <Icon icon="ant-design:undo-outlined" :size="15" />
            </template>
          </a-button>
        </a-tooltip>
        <a-tooltip title="恢复">
          <a-button @click="handleRedo">
            <template #icon>
              <Icon icon="ant-design:redo-outlined" :size="15" />
            </template>
          </a-button>
        </a-tooltip>
        <a-tooltip title="重新绘制">
          <a-button @click="handleRestart">
            <template #icon>
              <Icon icon="ant-design:cloud-sync-outlined" :size="15" />
            </template>
          </a-button>
        </a-tooltip>
      </div>

      <div style="margin-left: 20px">
        <a-dropdown>
          <a-button @click="handleRestart">
            <template #icon>
              <Icon icon="ant-design:eye-filled" :size="15" />
            </template>
          </a-button>
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="handleProcessXML"> 预览XML </a-menu-item>
              <!-- <a-menu-item key="2" > 预览JSON</a-menu-item> -->
            </a-menu>
          </template>
        </a-dropdown>
        <a-tooltip :title="simulationStatus ? '退出模拟' : '开启模拟'">
          <a-button @click="handleSimulation">
            <template #icon>
              <Icon icon="ant-design:bug-filled" :size="15" />
            </template>
          </a-button>
        </a-tooltip>
      </div>
    </div>
    <div class="canvas" ref="canvas"></div>
    <div id="js-properties-panel" class="panel"></div>
    <!-- <BpmnPanelAntdv ref="BpmnPanelAntdv" class="panel2" :options="options" /> -->
    <!-- 用于打开本地文件-->
    <input
      type="file"
      id="files"
      ref="refFile"
      style="display: none"
      accept=".xml, .bpmn"
      @change="importLocalFile"
    />
    <!-- 预览xml -->
    <BpmXMLModal @register="registerModal" />
  </div>
</template>

<script lang="ts" setup>
  import { useModal, Icon } from '@mxpio/components';
  import { Dropdown as ADropdown, Menu as AMenu, Tooltip as ATooltip } from 'ant-design-vue';
  import { defineProps, ref, onMounted, defineEmits } from 'vue';
  import BpmXMLModal from './bpmnXMLModal.vue';

  import BpmnModeler from 'bpmn-js/lib/Modeler';
  import 'bpmn-js/dist/assets/diagram-js.css';
  import 'bpmn-js/dist/assets/bpmn-js.css';
  import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css';

  import CamundaBpmnModdle from 'camunda-bpmn-moddle/resources/camunda.json';
  import {
    BpmnPropertiesPanelModule,
    BpmnPropertiesProviderModule,
    CamundaPlatformPropertiesProviderModule,
  } from 'bpmn-js-properties-panel';
  import 'bpmn-js-properties-panel/dist/assets/properties-panel.css';

  // 汉化
  import customTranslate from './workflow/customTranslate/customTranslate';
  // 拓展
  import UserTaskContextPad from './contextPad/userTaskContextPadProvider';

  // 模拟流转流程
  import TokenSimulationModule from 'bpmn-js-token-simulation';
  import 'bpmn-js-token-simulation/assets/css/bpmn-js-token-simulation.css';

  // import { BpmnPanelAntdv } from 'bpmn-panel-antdv/lib/bpmn-panel-antdv-mini.umd';
  // import 'bpmn-panel-antdv/lib/bpmn-panel-antdv-mini.css';
  // import Criteria from '@mxpio/utils/src/';

  // import { getQueryParams } from '@mxpio/utils/src/criteria';
  // import { getAction } from '@mxpio/api/src/common/manage';

  const AMenuItem = AMenu.Item;

  var customTranslateModule = {
    translate: ['value', customTranslate],
  };

  const props = defineProps({
    xml: {
      type: String,
      default: '',
    },
    flow: {
      type: Object,
      default: () => {},
    },
  });

  const emit = defineEmits(['save']);

  let bpmnModeler: any;
  const zoom = ref(1);
  const simulationStatus = ref(false);
  const recoverable = ref(false);
  const revocable = ref(false);
  const refFile = ref<HTMLInputElement>();
  const canvas = ref<HTMLElement>();

  const [registerModal, { openModal }] = useModal();

  // const options = ref({
  //   assigneeApi: this.assigneeApi,
  //   candidateUsersApi: this.assigneeApi,
  //   candidateGroupsApi: this.candidateGroupsApi,
  //   formKeyApi: this.formKeyApi,
  // })

  onMounted(() => {
    initBpmn();
  });

  function initBpmn() {
    bpmnModeler = new BpmnModeler({
      // 建模
      container: canvas.value,
      propertiesPanel: {
        parent: '#js-properties-panel', // 添加控制板
      },
      additionalModules: [
        BpmnPropertiesPanelModule,
        BpmnPropertiesProviderModule,
        CamundaPlatformPropertiesProviderModule,
        // 国际化
        customTranslateModule,
        TokenSimulationModule,
        UserTaskContextPad,
      ],
      moddleExtensions: {
        camunda: CamundaBpmnModdle,
      },
    });
    // this.$refs.BpmnPanelAntdv.init(this.bpmnModeler);
    createNewDiagram(props.xml);
  }

  const createNewDiagram = async function (xml?: string) {
    try {
      if (!xml) {
        await bpmnModeler.createDiagram();
        const shape = bpmnModeler.get('canvas')._rootElement;
        const modeling = bpmnModeler.get('modeling');
        if (shape) {
          modeling.updateProperties(shape, {
            isExecutable: true,
            id: props.flow.code,
            name: props.flow.name,
          });
        }
      } else {
        await bpmnModeler.importXML(xml);
      }
      // 回写模板名称及添加事件
      success();
      // 图形自适应
      var canvas = bpmnModeler.get('canvas');
      canvas.zoom(1, 'auto');
      canvas.zoom('fit-viewport', 'auto');
    } catch (error) {
      console.warn(error);
    }
  };

  const success = function () {
    // addBpmnListener();
  };

  // const addBpmnListener = function () {
  //   // 添加绑定事件
  //   try {
  //     // recoverable.value = bpmnModeler.get('commandStack').canRedo();
  //     // revocable.value = bpmnModeler.get('commandStack').canUndo();
  //   } catch (e) {
  //     console.error(`[Process Designer Warn]: ${e.message || e}`);
  //   }
  // };

  const handleUndo = function () {
    // 后退
    bpmnModeler.get('commandStack').undo();
  };

  const handleRedo = function () {
    // 前进
    bpmnModeler.get('commandStack').redo();
  };

  const handleRestart = function () {
    recoverable.value = false;
    revocable.value = false;
    createNewDiagram().then(() => bpmnModeler.get('canvas').zoom(1, 'auto'));
  };

  const handleZoomIn = function () {
    zoom.value = Number((Number(zoom.value) + Number(0.1)).toFixed(2));
    console.log(zoom);
    var canvas = bpmnModeler.get('canvas');
    canvas.zoom(zoom.value);
  };

  const handleZoomOut = function () {
    zoom.value = Number((Number(zoom.value) - Number(0.1)).toFixed(2));
    var canvas = bpmnModeler.get('canvas');
    canvas.zoom(zoom.value);
  };

  const handleReZoom = function () {
    zoom.value = 1;
    bpmnModeler.get('canvas').zoom('fit-viewport', 'auto');
  };

  const saveClick = function () {
    getDiagramXml().then((data) => {
      emit('save', data);
    });
  };

  async function getDiagramXml() {
    try {
      const result = await bpmnModeler.saveXML({ format: true });
      const result2 = await bpmnModeler.saveSVG();
      const { xml } = result;
      const { svg } = result2;
      return {
        xml,
        svg,
      };
    } catch (err) {
      console.log(err);
    }
  }

  const handleProcessXML = function () {
    bpmnModeler.saveXML({ format: true }).then(({ xml }) => {
      openModal(true, {
        bpmnStr: xml,
      });
    });
  };
  const handleSimulation = function () {
    simulationStatus.value = !simulationStatus.value;
    bpmnModeler.get('toggleMode').toggleMode();
  };

  const uploadClick = function () {
    refFile?.value?.click();
  };

  const importLocalFile = function () {
    const file = refFile?.value?.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.readAsText(file);
      reader.onload = function () {
        const xmlStr = this.result;
        xmlStr && createNewDiagram(xmlStr as string);
      };
    }
  };

  async function downloadProcess(type: string, name?: string) {
    try {
      // 按需要类型创建文件并下载
      if (type === 'xml' || type === 'bpmn') {
        const { err, xml } = await bpmnModeler.saveXML();
        // 读取异常时抛出异
        if (err) {
          console.error(`[Process Designer Warn ]: ${err.message || err}`);
        }
        const { href, filename } = setEncoded(type.toUpperCase(), name, xml);
        downloadFunc(href, filename);
      } else {
        const { err, svg } = await bpmnModeler.saveSVG();
        // 读取异常时抛出异常
        if (err) {
          return console.error(err);
        }
        const { href, filename } = setEncoded('SVG', name, svg);
        downloadFunc(href, filename);
      }
    } catch (e) {
      console.error(`[Process Designer Warn ]: ${e.message || e}`);
    }
    // 文件下载方法
    function downloadFunc(href: string, filename: string) {
      if (href && filename) {
        const a = document.createElement('a');
        a.download = filename; // 指定下载的文件名
        a.href = href; //  URL对象
        a.click(); // 模拟点击
        URL.revokeObjectURL(a.href); // 释放URL 对象
      }
    }
  }

  const downloadProcessAsXml = function () {
    downloadProcess('xml');
  };
  const downloadProcessAsBpmn = function () {
    downloadProcess('bpmn');
  };
  const downloadProcessAsSvg = function () {
    downloadProcess('svg');
  };

  const setEncoded = function (type, filename = 'diagram', data) {
    // 当图发生改变的时候会调用这个函数，这个data就是图的xml
    const encodedData = encodeURIComponent(data);
    return {
      filename: `${filename}.${type}`,
      href: `data:application/${type === 'svg' ? 'text/xml' : 'bpmn20-xml'};charset=UTF-8,${encodedData}`,
      data: data,
    };
  };

  // const assigneeApi = async function (parms) {
  //   if (parms) {
  //     const params = getQueryParams(
  //       Object.assign(
  //         {},
  //         {
  //           username: parms.query?.value,
  //           nickname: parms.query?.label,
  //         },
  //         parms.page,
  //       ),
  //     );
  //     const res = await getAction('/user/list', params);
  //     const data: any[] = [];
  //     if (res.success) {
  //       res.result.content.forEach((item) => {
  //         data.push({ label: item.nickname, value: item.username });
  //       });
  //     }
  //     return {
  //       total: res.result.totalElements,
  //       result: data,
  //     };
  //   }
  // };

  // const candidateGroupsApi = async function (parms) {
  //   if (parms) {
  //     const params = getQueryParams(
  //       Object.assign(
  //         {},
  //         {
  //           username: parms.query?.value,
  //           nickname: parms.query?.label,
  //         },
  //         parms.page,
  //       ),
  //     );
  //     const res = await getAction('/role/page', params);
  //     const data: any[] = [];
  //     if (res.success) {
  //       res.result.content.forEach((item) => {
  //         data.push({ label: item.name, value: `ROLE_${item.id}` });
  //       });
  //     }
  //     return {
  //       total: res.result.totalElements,
  //       result: data,
  //     };
  //   }
  // };

  // const formKeyApi = async function (parms) {
  //   if (parms) {
  //     const params = getQueryParams(
  //       Object.assign(
  //         {},
  //         {
  //           lastDefKey: parms.query?.value,
  //           name: parms.query?.label,
  //         },
  //         parms.page,
  //       ),
  //     );
  //     const res = await getAction('/camunda/form/page', params);
  //     const data: any[] = [];
  //     if (res.success) {
  //       res.result.content.forEach((item) => {
  //         data.push({ label: item.name, value: item.lastDefKey });
  //       });
  //     }
  //     return {
  //       total: res.result.totalElements,
  //       result: data,
  //     };
  //   }
  // };
</script>
<style lang="less">
  .containers .djs-palette.two-column.open {
    width: 100px !important;
  }

  .bts-toggle-mode {
    display: none !important;
  }

  .my-process-designer__header {
    display: flex;
    width: 100%;
    min-height: 36px;
    padding-top: 10px;
    padding-left: 10px;

    // .ant-btn-group {
    //   margin: 4px;
    // }

    .align {
      position: relative;

      i {
        &::after {
          content: '|';
          position: absolute;
          transform: rotate(90deg) translate(200%, 60%);
        }
      }
    }

    .align.align-left i {
      transform: rotate(90deg);
    }

    .align.align-right i {
      transform: rotate(-90deg);
    }

    .align.align-top i {
      transform: rotate(180deg);
    }

    .align.align-bottom i {
      transform: rotate(0deg);
    }

    .align.align-center i {
      transform: rotate(90deg);

      &::after {
        transform: rotate(90deg) translate(0, 60%);
      }
    }

    .align.align-middle i {
      transform: rotate(0deg);

      &::after {
        transform: rotate(90deg) translate(0, 60%);
      }
    }
  }

  .bpmn-design-modal {
    .vue-codemirror,
    .CodeMirror {
      height: 100%;
    }
  }
</style>
<style scoped>
  .canvas {
    position: absolute;
    inset: 50px 0 0;
  }

  .panel {
    position: absolute;
    top: 55px;
    right: 0;
    bottom: 0;
    width: 300px;
    overflow: auto;
  }

  .panel2 {
    top: 55px !important;
    height: calc(100% - 55px) !important;
  }

  .containers {
    width: 100%;
    height: calc(100vh - 90px);
    overflow: auto;
    background: white;
    background-color: #fff;
    background-image: linear-gradient(90deg, rgb(220 220 220 / 50%) 6%, transparent 0),
      linear-gradient(rgb(192 192 192 / 50%) 6%, transparent 0);
    background-size: 12px 12px;
    -webkit-tap-highlight-color: rgb(255 255 255 / 0%);
  }
</style>
