<template>
  <BasicModal
    width="1200px"
    title="日历视图"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <FullCalendar :options="calendarOptions" ref="fullcalendarref">
      <template #eventContent="{ event }">
        <a-popover title="详细信息" trigger="click">
          <template #content>
            <Descriptions bordered style="width: 500px" :column="2">
              <Descriptions.Item label="业务单号：">
                {{ event.extendedProps._data.bizNo }}
              </Descriptions.Item>
              <Descriptions.Item label="业务单行号：">
                {{ event.extendedProps._data.bizLineNo }}
              </Descriptions.Item>
              <Descriptions.Item label="物料编码：">
                {{ event.extendedProps._data.itemCode }}
              </Descriptions.Item>
              <Descriptions.Item label="图号：">
                {{ event.extendedProps._data.drawingNo }}
              </Descriptions.Item>
              <Descriptions.Item label="单位：">
                {{ event.extendedProps._data.textMap.unitCode$DICT_TEXT_ }}
              </Descriptions.Item>
              <Descriptions.Item label="需求数量：">
                {{ event.extendedProps._data.needQuantity }}
              </Descriptions.Item>
              <Descriptions.Item label="需求日期：">
                {{ event.extendedProps._data.needDate }}
              </Descriptions.Item>
              <Descriptions.Item label="计划数量：">
                {{ event.extendedProps._data.quantity }}</Descriptions.Item
              >
              <Descriptions.Item label="计划交付日期：">
                {{ event.extendedProps._data.deliveryDate }}
              </Descriptions.Item>
              <Descriptions.Item label="备注" :span="2">
                {{ event.extendedProps._data.memo }}
              </Descriptions.Item>
            </Descriptions>
          </template>
          <span>{{ event.title }}</span>
        </a-popover>
      </template>
    </FullCalendar>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { Popover as APopover, Descriptions } from 'ant-design-vue';
  import { BasicModal, useModalInner } from '@mxpio/components';
  import { mpsLineListApi, mpsLineSaveApi } from '@mxpio/bizcommon';
  import FullCalendar from '@fullcalendar/vue3';
  import dayGridPlugin from '@fullcalendar/daygrid';
  import interactionPlugin from '@fullcalendar/interaction';
  import locale from '@fullcalendar/core/locales/zh-cn';
  import { dateUtil } from '@mxpio/utils';
  import { CuryTypeEnum } from '@mxpio/enums';

  const componentName = 'FullcalendarModal';
  defineOptions({ name: componentName });

  const emit = defineEmits(['success', 'register']);
  let versionCode = ref<string>('');
  const mainPlanLines = ref<any>([]);
  const matchList = ref<any>([]); //日历数据
  const currentDate = ref(dateUtil().format('YYYY-MM-DD'));
  const fullcalendarref = ref(); //实例

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    versionCode.value = data.code;
    mainPlanLines.value = await getCalendarEvent();
    setTimeout(() => {
      initCalendar();
    }, 200);
  });

  async function handleSubmit() {
    try {
      closeModal();
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  async function getCalendarEvent() {
    const res = await mpsLineListApi(versionCode.value);
    return res || [];
  }

  const eventDrop = async (eventDropInfo) => {
    const data = eventDropInfo.event;
    const row = data.extendedProps?._data;
    try {
      await mpsLineSaveApi([
        {
          ...row,
          crudType: CuryTypeEnum.UPDATE,
          deliveryDate: data.startStr,
        },
      ]);
      emit('success');
    } catch (error) {
      console.log(error);
    }
  };

  //这个calendarOptions是配置项 这里只是部分配置项
  const calendarOptions = computed(() => {
    return {
      plugins: [dayGridPlugin, interactionPlugin], //插件  我目前用的是月视图插件
      initialView: 'dayGridMonth', //视图
      height: '780px',
      locale: locale, //语言汉化
      firstDay: 1, // 设置一周中显示的第一天是哪天，周日是0，周一是1，类推
      editable: true, //事件是否可编辑，可编辑是指可以移动, 改变大小等。
      droppable: true, //是否可拖拽
      headerToolbar: {
        left: '',
        center: 'title',
      },
      eventDrop: eventDrop,
      initialDate: currentDate.value, // 自定义设置背景颜色时一定要初始化日期时间
      events: matchList.value, //绑定展示事件
      selectable: true,
      selectMirror: true,
      dayMaxEvents: true,
      weekends: true,
      eventDurationEditable: false,
      handleWindowResize: true,
      weekNumberCalculation: 'ISO',
      views: {
        agenda: {
          eventLimit: 3,
        },
      },
    };
  });

  const initCalendar = async () => {
    matchList.value = [];
    let evenstList = mainPlanLines.value.map((item: any) => {
      return {
        title: `${item.itemName}:${item.quantity}`,
        start: item.deliveryDate,
        end: item.deliveryDate,
        _data: { ...item },
      };
    });
    matchList.value = [...evenstList];
    currentDate.value = mainPlanLines.value[0]
      ? dateUtil(mainPlanLines.value[0].deliveryDate).format('YYYY-MM-DD')
      : dateUtil().format('YYYY-MM-DD');
    fullcalendarref.value?.getApi().gotoDate(currentDate.value);
  };
</script>
