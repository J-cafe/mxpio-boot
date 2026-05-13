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
              <Descriptions.Item label="物料编码：">
                {{ event.extendedProps._data.itemCode }}
              </Descriptions.Item>
              <Descriptions.Item label="物料名称：">
                {{ event.extendedProps._data.itemName }}
              </Descriptions.Item>
              <Descriptions.Item label="规格型号：">
                {{ event.extendedProps._data.itemSpec }}
              </Descriptions.Item>
              <Descriptions.Item label="图号：">
                {{ event.extendedProps._data.drawingNo }}
              </Descriptions.Item>
              <Descriptions.Item label="单位：">
                {{ event.extendedProps._data.textMap.unitCode$DICT_TEXT_ }}
              </Descriptions.Item>
              <Descriptions.Item label="数量：">
                {{ event.extendedProps._data.quantity }}
              </Descriptions.Item>
              <Descriptions.Item label="开始日期：">
                {{ event.extendedProps._data.startDate }}
              </Descriptions.Item>
              <Descriptions.Item label="完工日期：">
                {{ event.extendedProps._data.deliverDate }}
              </Descriptions.Item>
              <Descriptions.Item label="主制车间：">
                {{ event.extendedProps._data.textMap.mainWorkshop$DICT_TEXT_ }}
              </Descriptions.Item>
              <Descriptions.Item label="工作中心：">
                {{ event.extendedProps._data.textMap.workCenterCode$DICT_TEXT_ }}
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
  import { pmPageApi, pmSaveApi } from '@mxpio/bizcommon';
  import FullCalendar from '@fullcalendar/vue3';
  import dayGridPlugin from '@fullcalendar/daygrid';
  import interactionPlugin from '@fullcalendar/interaction';
  import locale from '@fullcalendar/core/locales/zh-cn';
  import { dateUtil } from '@mxpio/utils';
  import Criteria from '@mxpio/utils/src/criteria';
  import { OperatorEnum, CuryTypeEnum } from '@mxpio/enums';

  const componentName = 'FullcalendarModal';
  defineOptions({ name: componentName });

  const emit = defineEmits(['success', 'register']);
  const mainPlanLines = ref<any>([]);
  const matchList = ref<any>([]); //日历数据
  const currentDate = ref(dateUtil().format('YYYY-MM-DD'));
  const fullcalendarref = ref(); //实例
  const filters = ref<any>({
    deliverDate: [currentDate.value, currentDate.value],
    startDate: [currentDate.value, currentDate.value],
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async () => {
    setModalProps({ confirmLoading: false });
    setDateRange(currentDate.value);
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
    const param = getQueryParams();
    const res = await pmPageApi(param);
    return res.content || [];
  }

  function getQueryParams() {
    const queryParam = new Criteria();
    queryParam
      .addCriterion('outsourceFlag', OperatorEnum.EQ, '0')
      .or()
      .and()
      .addCriterion('deliverDate', OperatorEnum.GE, filters.value['deliverDate'][0])
      .addCriterion('deliverDate', OperatorEnum.LE, filters.value['deliverDate'][1])
      .end()
      .and()
      .addCriterion('startDate', OperatorEnum.GE, filters.value['startDate'][0])
      .addCriterion('startDate', OperatorEnum.LE, filters.value['startDate'][1])
      .end()
      .end();
    var param: any = Object.assign({}, { criteria: queryParam.getEncode() });
    param.pageNo = 1;
    param.pageSize = 999;
    return param;
  }

  const eventDrop = async (eventDropInfo) => {
    const data = eventDropInfo.event;
    const row = data.extendedProps?._data;
    try {
      await pmSaveApi({
        ...row,
        crudType: CuryTypeEnum.UPDATE,
        deliverDate: dateUtil(data.endStr).startOf('day').format('YYYY-MM-DD'),
        startDate: dateUtil(data.startStr).startOf('day').format('YYYY-MM-DD'),
      });
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
      customButtons: {
        next: {
          text: '下一个月',
          click: function () {
            const getApi = fullcalendarref.value?.getApi();
            getApi.next();
            reloadData(getApi.currentData.currentDate);
          },
        },
        prev: {
          text: '下一个月',
          click: function () {
            const getApi = fullcalendarref.value?.getApi();
            getApi.prev();
            reloadData(getApi.currentData.currentDate);
          },
        },
        today: {
          text: '今天',
          click: function () {
            const getApi = fullcalendarref.value?.getApi();
            getApi.today();
            reloadData(getApi.currentData.currentDate);
          },
        },
      },
    };
  });

  const initCalendar = async () => {
    matchList.value = [];
    let evenstList = mainPlanLines.value.map((item: any) => {
      return {
        title: `${item.itemName}:${item.quantity}`,
        start: dateUtil(item.startDate).startOf('day').format('YYYY-MM-DD HH:mm:ss'),
        end: dateUtil(item.deliverDate).endOf('day').format('YYYY-MM-DD HH:mm:ss'),
        _data: { ...item },
      };
    });
    matchList.value = [...evenstList];
  };

  function setDateRange(date: string) {
    filters.value.deliverDate = [
      dateUtil(date).startOf('months').startOf('day').format('YYYY-MM-DD HH:mm:ss'),
      dateUtil(date).endOf('months').endOf('day').format('YYYY-MM-DD HH:mm:ss'),
    ];

    filters.value.startDate = [
      dateUtil(date).startOf('months').startOf('day').format('YYYY-MM-DD HH:mm:ss'),
      dateUtil(date).endOf('months').endOf('day').format('YYYY-MM-DD HH:mm:ss'),
    ];
  }

  async function reloadData(date: string) {
    setDateRange(date);
    mainPlanLines.value = await getCalendarEvent();
    setTimeout(() => {
      initCalendar();
    }, 200);
  }
</script>
