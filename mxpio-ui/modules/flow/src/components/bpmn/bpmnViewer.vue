<template>
  <div class="canvas canvas-viewer" ref="canvas"></div>
</template>

<script>
  // 这里只需要预览引入Viewer就可以，如果需要编辑则引入"bpmn-js/lib/Modeler",并且还需要引入左侧编辑栏panel等
  import BpmnViewer from 'bpmn-js/lib/Viewer';
  import MoveCanvasModule from 'diagram-js/lib/navigation/movecanvas';

  export default {
    props: {
      xml: {
        type: String,
        require: true,
      },
      height: {
        type: [Number, String],
        default: 540,
      },
      move: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        bpmnViewer: null,
        scale: 1,
      };
    },
    mounted() {
      this.getImg();
    },
    methods: {
      async getImg() {
        // 初始时清除图层
        this.bpmnViewer && this.bpmnViewer.destroy();
        this.$refs.canvas.innerHTML = '';
        this.scale = 1; // 放大缩小比例
        // 初始化canvas
        const additionalModules = [];
        this.move && additionalModules.push(MoveCanvasModule);
        this.bpmnViewer = new BpmnViewer({
          container: this.$refs.canvas,
          height: this.height, // 根据实际情况设置高度，宽度的话设置父元素的宽度就可以，会自适应的
          additionalModules: additionalModules,
        });
        const bpmnXmlStr = this.xml; // 从接口获取的xml文件
        const { err } = await this.bpmnViewer.importXML(bpmnXmlStr);
        if (err) {
          console.error(err);
        } else {
          const canvas = self.bpmnViewer.get('canvas');
          canvas.zoom('fit-viewport', 'auto');
        }
      },
    },
  };
</script>
<style lang="less">
  .canvas-viewer {
    .bjs-powered-by,
    .bjs-breadcrumbs {
      display: none;
    }
  }
</style>
