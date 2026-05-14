<template>
  <div>
    <div class="canvas canvas-task-viewer" ref="canvas"></div>
    <div class="button-group-right">
      <EnvironmentOutlined class="bpmn-button" @click="handleReset" />
      <ZoomInOutlined class="bpmn-button mt-2" @click="handleIn" />
      <ZoomOutOutlined class="bpmn-button" @click="handleOut" />
      <DownloadOutlined class="bpmn-button mt-2" @click="handleDownload" />
    </div>
  </div>
</template>

<script lang="ts" setup>
  // 这里只需要预览引入Viewer就可以，如果需要编辑则引入"bpmn-js/lib/Modeler",并且还需要引入左侧编辑栏panel等
  import BpmnViewer from 'bpmn-js/lib/Viewer';
  import { ref, onMounted } from 'vue';

  import {
    EnvironmentOutlined,
    ZoomInOutlined,
    ZoomOutOutlined,
    DownloadOutlined,
  } from '@ant-design/icons-vue';

  import MoveCanvasModule from 'diagram-js/lib/navigation/movecanvas';
  import { dateUtil } from '@mxpio/utils';

  const props = defineProps({
    xml: {
      type: String,
      require: true,
    },
    nodeCodes: {
      // 流程已办理节点id
      type: Object,
      default: () => {
        return {
          completed: [], // 审批通过接口
          pending: [], // 待审批节点
          error: [], // 审批未通过节点
        };
      },
    },
    height: {
      type: [Number, String],
      default: 540,
    },
    activityList: {
      type: Array,
      default: () => {
        return [];
      },
    },
  });

  const canvas = ref<HTMLElement>();
  let zoom = ref(1);
  let elementOverlayIds = ref({});
  let overlays: any;

  let bpmnViewer: any;
  let element = ref(null);

  async function init() {
    // 初始时清除图层
    bpmnViewer && bpmnViewer.destroy();
    canvas.value ? (canvas.value.innerHTML = '') : '';
    zoom.value = 1; // 放大缩小比例
    // 初始化canvas
    bpmnViewer = new BpmnViewer({
      container: canvas.value,
      height: props.height, // 根据实际情况设置高度，宽度的话设置父元素的宽度就可以，会自适应的
      additionalModules: [MoveCanvasModule],
    });

    if (props.xml) {
      const bpmnXmlStr = props.xml; // 从接口获取的xml文件
      const res = await bpmnViewer.importXML(bpmnXmlStr);
      if (res.err) {
        console.error(res.err);
      } else {
        const canvas = bpmnViewer.get('canvas');
        canvas.zoom('fit-viewport', 'auto');
        fillColor();
      }
    }
  }

  function fillColor() {
    const canvas = bpmnViewer.get('canvas');
    bpmnViewer.getDefinitions().rootElements[0].flowElements.forEach((n) => {
      if (!getNodeByTask(n.id)) {
        // 判断该节点是否在已办理列表内
        return;
      }
      const css = getResultCss(n.id);
      if (n.$type === 'bpmn:UserTask') {
        // 用户任务
        canvas.addMarker(n.id, css);

        // 如果节点为待办理、不同意，则不修改后续连线颜色
        if (props.nodeCodes.error.includes(n.id) || props.nodeCodes.pending.includes(n.id)) {
          return '';
        }
        n.outgoing?.forEach((nn) => {
          // 遍历后续线段，设置线段颜色
          canvas.addMarker(nn.id, css);
        });
      } else if (n.$type === 'bpmn:ExclusiveGateway') {
        // 排他网关
        canvas.addMarker(n.id, css);
        n.outgoing?.forEach((nn) => {
          // 遍历后续线段，设置线段颜色
          if (getNodeByTask(nn.targetRef.id)) {
            // 判断该线段来源节点是否在已办理列表内
            canvas.addMarker(nn.id, css);
          }
        });
      } else if (n.$type === 'bpmn:ParallelGateway') {
        // 并行、相容网关
        canvas.addMarker(n.id, css);
        n.outgoing?.forEach((nn) => {
          // 遍历后续线段，设置线段颜色
          if (getNodeByTask(nn.targetRef.id)) {
            // 判断该线段来源节点是否在已办理列表内
            canvas.addMarker(nn.id, css);
          }
        });
      } else if (n.$type === 'bpmn:StartEvent') {
        // 开始节点
        n.outgoing.forEach((nn) => {
          // 开始节点判断输入线段
          canvas.addMarker(nn.id, 'highlight');
          canvas.addMarker(n.id, 'highlight');
        });
      } else if (n.$type === 'bpmn:EndEvent') {
        canvas.addMarker(n.id, 'highlight');
        n.outgoing?.forEach((nn) => {
          // 遍历后续线段，设置线段颜色
          if (getNodeByTask(nn.targetRef.id)) {
            // 判断该线段来源节点是否在已办理列表内
            canvas.addMarker(nn.id, css);
          }
        });
      } else {
        canvas.addMarker(n.id, 'highlight');
        n.outgoing?.forEach((nn) => {
          // 遍历后续线段，设置线段颜色
          if (getNodeByTask(nn.targetRef.id)) {
            // 判断该线段来源节点是否在已办理列表内
            canvas.addMarker(nn.id, css);
          }
        });
      }
    });
  }
  function getNodeByTask(id) {
    return (
      props.nodeCodes.completed.includes(id) ||
      props.nodeCodes.pending.includes(id) ||
      props.nodeCodes.error.includes(id)
    );
  }
  function getResultCss(id) {
    const completeTask = props.nodeCodes.completed.includes(id);
    const todoTask = props.nodeCodes.pending.includes(id);
    const errorTask = props.nodeCodes.error.includes(id);
    if (todoTask) {
      // 审批中
      return 'highlight-todo';
    } else if (completeTask) {
      // 已通过
      return 'highlight';
    } else if (errorTask) {
      return 'highlight-reject';
    }
    return '';
  }
  function handleIn() {
    zoom.value += 0.1;
    var canvas = bpmnViewer.get('canvas');
    canvas.zoom(zoom.value);
  }
  function handleOut() {
    zoom.value -= 0.1;
    var canvas = bpmnViewer.get('canvas');
    canvas.zoom(zoom.value);
  }
  function handleReset() {
    zoom.value = 1;
    bpmnViewer.get('canvas').zoom('fit-viewport', 'auto');
  }
  async function handleDownload() {
    const { err, svg } = await bpmnViewer.saveSVG();
    // 读取异常时抛出异常
    if (err) {
      return console.error(err);
    }
    const { href, filename } = setEncoded('svg', '流程图', svg);
    downloadFunc(href, filename);
  }
  // 文件下载方法
  function downloadFunc(href, filename) {
    if (href && filename) {
      const a = document.createElement('a');
      a.download = filename; // 指定下载的文件名
      a.href = href; //  URL对象
      a.click(); // 模拟点击
      URL.revokeObjectURL(a.href); // 释放URL 对象
    }
  }
  // 根据所需类型进行转码并返回下载地址
  function setEncoded(type, filename = 'diagram', data) {
    const encodedData = encodeURIComponent(data);
    return {
      filename: `${filename}.${type}`,
      href: `data:application/${type === 'svg' ? 'text/xml' : 'bpmn20-xml'};charset=UTF-8,${encodedData}`,
      data: data,
    };
  }
  function initModelListeners() {
    const EventBus = bpmnViewer.get('eventBus');
    // 注册需要的监听事件
    EventBus.on('element.hover', function (eventObj) {
      const element_ = eventObj ? eventObj.element : null;
      elementHover(element_);
    });
    EventBus.on('element.out', function (eventObj) {
      const element_ = eventObj ? eventObj.element : null;
      elementOut(element_);
    });
  }
  // 流程图的元素被 hover
  function elementHover(element_) {
    element.value = element_;
    !elementOverlayIds.value && (elementOverlayIds.value = {});
    !overlays && (overlays = bpmnViewer.get('overlays'));
    // 展示信息
    const activity = props.activityList.find(
      (m) => m.taskDefinitionKey === element.value.id || m.activityId === element.value.id,
    );
    if (!activity) {
      return;
    }
    if (!elementOverlayIds[element.value.id] && element.value.type !== 'bpmn:Process') {
      if (
        !['bpmn:StartEvent', 'bpmn:EndEvent', 'bpmn:ServiceTask', 'bpmn:UserTask'].includes(
          element.value.type,
        )
      ) {
        return false;
      }
      let html = `<div class="element-overlays">无信息</div>`; // 默认值
      if (element.value.type === 'bpmn:StartEvent') {
        html = `<p>发起时间：${dateUtil(activity.endTime).format('YYYY-MM-DD HH:mm:ss')}`;
      } else if (element.value.type === 'bpmn:UserTask') {
        html = `<p>办理人：${activity.assignee ? activity.textMap?.assignee$DICT_TEXT_ : '暂无'}</p>
                  <p>开始时间：${dateUtil(activity.createTime || activity.startTime).format('YYYY-MM-DD HH:mm:ss')}`;
        if (activity.endTime) {
          html += `<p>结束时间：${dateUtil(activity.endTime).format('YYYY-MM-DD HH:mm:ss')}</p>`;
        }
        if (activity.canceled === false || activity.canceled === true) {
          html += `<p>审批意见：${activity.canceled ? `${activity.comment || '不通过'}` : '通过'}</p>`;
        }
      } else if (element.value.type === 'bpmn:ServiceTask') {
        if (activity.createTime) {
          html = `<p>创建时间：${dateUtil(activity.createTime || activity.startTime).format('YYYY-MM-DD HH:mm:ss')}</p>`;
        }
        if (activity.endTime) {
          html += `<p>结束时间：${dateUtil(activity.endTime).format('YYYY-MM-DD HH:mm:ss')}</p>`;
        }
      } else if (element.value.type === 'bpmn:EndEvent') {
        html = `<p>结果：通过</p>`;
        if (activity.endTime) {
          html += `<p>结束时间：${dateUtil(activity.endTime).format('YYYY-MM-DD HH:mm:ss')}</p>`;
        }
      }
      elementOverlayIds[element.value.id] = overlays.add(element_, {
        position: { left: 0, bottom: 0 },
        html: `<div class="element-overlays">${html}</div>`,
      });
    }
  }

  // 流程图的元素被 out
  function elementOut(element) {
    overlays.remove({ element });
    elementOverlayIds[element.id] = null;
  }

  onMounted(() => {
    init();
    // 初始模型的监听器
    initModelListeners();
  });
</script>
<style lang="less">
  @keyframes waiting {
    0% {
      stroke-dashoffset: 1000;
    }
  }

  .canvas-task-viewer {
    .bjs-breadcrumbs {
      display: none;
    }
  }

  .button-group-right {
    display: flex;
    position: absolute;
    z-index: 1000;
    right: 15px;
    bottom: 35%;
    flex-direction: column;
    align-items: center;
    height: 23px;
  }

  .bpmn-button {
    padding: 3px;
    border: 1px solid transparent;
    background-color: #f2f3f5;
    color: #4e5969;
    font-size: 20px;
  }

  // 已办理
  .highlight.djs-shape .djs-visual > :nth-child(1) {
    stroke: rgb(18 202 110) !important;
    fill: rgb(18 202 110) !important;
    fill-opacity: 0.2 !important;
  }

  .highlight.djs-shape .djs-visual > :nth-child(2) {
    stroke: rgb(18 202 110) !important;
    fill: rgb(18 202 110) !important;
    fill-opacity: 0.2 !important;
  }

  .highlight.djs-shape .djs-visual > path {
    stroke: rgb(18 202 110) !important;
    fill: rgb(18 202 110) !important;
    fill-opacity: 0.2 !important;
  }

  .highlight.djs-connection > .djs-visual > path {
    stroke: rgb(18 202 110) !important;
  }

  // 待办理
  .highlight-todo.djs-connection > .djs-visual > path {
    stroke-dasharray: 4px !important;
    stroke: orange !important;
    fill-opacity: 0.2 !important;
  }

  .highlight-todo.djs-shape .djs-visual > :nth-child(1) {
    stroke-dasharray: 4px !important;
    stroke: orange !important;
    fill: orange !important;
    fill-opacity: 0.2 !important;
  }

  .highlight-todo.djs-shape .djs-visual > :nth-child(2) {
    stroke: orange !important;
    fill: orange !important;
    fill-opacity: 0.2 !important;
  }

  .highlight-todo.djs-shape .djs-visual > path {
    stroke: orange !important;
    fill: orange !important;
    fill-opacity: 0.2 !important;
  }

  .highlight-todo.djs-shape .djs-visual > *:not(.djs-label):not(path),
  .highlight-todo.djs-connection > .djs-visual > path {
    animation: waiting 35s linear infinite !important;
  }

  // 不同意
  .highlight-reject.djs-shape .djs-visual > :nth-child(1) {
    stroke: red !important;
    fill: red !important;
    fill-opacity: 0.2 !important;
  }

  .highlight-reject.djs-shape .djs-visual > :nth-child(2) {
    fill: red !important;
  }

  .highlight-reject.djs-shape .djs-visual > path {
    stroke: red !important;
    fill: red !important;
    fill-opacity: 0.2 !important;
  }

  .highlight-reject.djs-connection > .djs-visual > path {
    stroke: red !important;
  }

  .highlight-reject:not(.djs-connection) .djs-visual > :nth-child(1) {
    fill: red !important; /* color elements as green */
  }

  :deep(.highlight-reject.djs-shape .djs-visual > :nth-child(1)) {
    stroke: red !important;
    fill: red !important;
    fill-opacity: 0.2 !important;
  }

  :deep(.highlight-reject.djs-shape .djs-visual > :nth-child(2)) {
    fill: red !important;
  }

  :deep(.highlight-reject.djs-shape .djs-visual > path) {
    stroke: red !important;
    fill: red !important;
    fill-opacity: 0.2 !important;
  }

  :deep(.highlight-reject.djs-connection > .djs-visual > path) {
    stroke: red !important;
  }

  .element-overlays {
    box-sizing: border-box;
    width: 260px;
    padding: 8px;
    border-radius: 4px;
    background: rgb(0 0 0 / 80%);
    color: #fafafa;
  }
</style>
