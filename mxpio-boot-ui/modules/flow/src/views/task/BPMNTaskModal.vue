<template>
  <BasicModal
    title="流程办理"
    wrapClassName="containers"
    :minHeight="500"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @cancel="handleCancel"
  >
    <template #footer>
      <div style="text-align: center">
        <a-button key="1" @click="handleCancel">关闭</a-button>
        <a-button
          type="primary"
          key="2"
          v-if="props.type === 'handle' && task.taskType === 'active'"
          :loading="confirmLoading"
          @click="handleComplete"
          >同意</a-button
        >
        <a-button
          key="3"
          danger
          v-if="props.type === 'handle' && task.taskType === 'active'"
          @click="handleRejectLast"
          >驳回上一节点</a-button
        >
        <a-button
          key="4"
          v-if="props.type === 'handle' && task.taskType === 'active'"
          type="primary"
          danger
          @click="handleRejectFirst"
          >不同意</a-button
        >
        <a-button
          key="5"
          v-if="props.type === 'handle' && task.taskType === 'active'"
          type="primary"
          >委派</a-button
        >
        <a-button
          key="6"
          v-if="props.type === 'handle' && task.taskType !== 'active'"
          type="primary"
          @click="handleClaim"
          >拾取</a-button
        >
        <a-button key="7" v-if="props.hasCancel" @click="handleTaskCancel" danger type="primary"
          >撤回</a-button
        >
      </div>
    </template>
    <a-tabs tabPosition="left">
      <a-tab-pane key="1" tab="表单信息" style="padding: 15px">
        <a-collapse v-model:activeKey="activeKey">
          <a-collapse-panel key="1" header="发起单据">
            <VFormCreate
              ref="vFormCreate"
              key="vFormCreate"
              v-if="showBpmn"
              :formConfig="formConfigDef"
              :formModel="formData"
            />
          </a-collapse-panel>
          <a-collapse-panel key="2" header="待办单据" v-if="task.hasForm">
            <VFormCreate
              ref="vFormNode"
              key="vFormNode"
              v-if="showNodeBpmn"
              :formConfig="nodeFormConfigDef"
              :formModel="nodeFormData"
            />
          </a-collapse-panel>
        </a-collapse>
      </a-tab-pane>
      <a-tab-pane key="2" tab="办理记录">
        <a-list v-if="historicsDatas.length" item-layout="horizontal" :data-source="historicsDatas">
          <template #renderItem="{ item }">
            <a-list-item>
              <template #actions>
                <a v-if="item.hasForm" @click="handleDetail(item)">详情</a>
              </template>
              <a-list-item-meta
                :description="
                  '操作人：' +
                  (item.textMap ? item.textMap.assignee$DICT_TEXT_ : item.assignee) +
                  '，操作时间：' +
                  fomart(item.endTime)
                "
              >
                <template #title>
                  <h2
                    >节点:【{{ item.name }}】,审批意见:【{{
                      item.canceled ? `${item.comment || '不通过'}` : '通过'
                    }}】</h2
                  >
                </template>
                <template #avatar>
                  <a-avatar style="background-color: #87d068">{{ getFirstName(item) }}</a-avatar>
                </template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>
        <a-empty v-else />
      </a-tab-pane>
      <a-tab-pane key="3" tab="流程图">
        <bpmnTaskViewer
          :move="true"
          :xml="bpmnXmlStr"
          :nodeCodes="nodeCodes"
          :activityList="nodeDatas"
        />
      </a-tab-pane>
    </a-tabs>
    <CommentModal @register="registerCommentModal" @success="commentOk" />
    <HistoryFormModalModal @register="registerHistoryFormModal" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner, useModal, VFormCreate } from '@mxpio/components';
  // import VFormCreate from '../../components/form-design/components/VFormCreate/index.vue';
  import bpmnTaskViewer from '../../components/bpmn/bpmnTaskViewer.vue';
  import CommentModal from './CommentModal.vue';
  import HistoryFormModalModal from './HistoryFormModalModal.vue';

  import {
    defDetails,
    getFormModel,
    completeTask,
    rejectlastTask,
    rejectfirstTask,
    getHistorics,
    getActive,
    claimTask,
    cancelTask,
  } from '@mxpio/bizcommon';
  import {
    Avatar as AAvatar,
    Collapse as ACollapse,
    CollapsePanel as ACollapsePanel,
    List as AList,
    Empty as AEmpty,
  } from 'ant-design-vue';

  import { dateUtil } from '@mxpio/utils';

  defineOptions({ name: 'BPMNTaskModal' });

  const AListItemMeta = AList.Item.Meta;
  const AListItem = AList.Item;

  const emit = defineEmits(['success', 'register']);
  const props = defineProps({
    type: {
      type: String,
      default: '', // handle:办理，view: 查看
    },
    hasCancel: {
      type: Boolean,
      default: false,
    },
  });

  const showBpmn = ref(false);
  const showNodeBpmn = ref(false);
  const vFormCreate = ref();
  const vFormNode = ref();

  const activeKey = ref(['1', '2']);
  const task = ref<Recordable>({}); //当前任务信息

  const formData = ref({}); //发起表单数据
  const nodeFormData = ref({}); //节点表单数据

  const formConfigDef = ref(); //发起表单配置信息
  const nodeFormConfigDef = ref(); //节点表单配置信息

  const bpmnXmlStr = ref(); //流程图xml

  const nodeCodes = ref<{
    completed: string[];
    pending: string[];
    error: string[];
  }>({
    // 节点状态
    completed: [],
    pending: [],
    error: [],
  });

  const confirmLoading = ref(false);

  const nodeDatas = ref([]); // 全部节点数据（包含已办理、待办理）

  const historicsDatas = ref([]); // 已办理节点数据

  const [registerCommentModal, { openModal: openModalCommon }] = useModal(); // 不同意、驳回填写原因弹窗
  const [registerHistoryFormModal, { openModal: openModalHistoryForm }] = useModal(); // 节点详情

  const rejectType = ref(); // 不同意、驳回类型

  const [registerModal, { closeModal }] = useModalInner(async (data) => {
    resetData();
    const { record } = data;
    task.value = record;
    await getDefDetails(record);
    if (record.hasForm) {
      getFormModelById(record.id);
    }
    getHistoricsAndActive();
  });

  // 添加重置数据的函数
  const resetData = () => {
    showBpmn.value = false;
    showNodeBpmn.value = false;
    formData.value = {};
    nodeFormData.value = {};
    formConfigDef.value = undefined;
    nodeFormConfigDef.value = undefined;
    bpmnXmlStr.value = undefined;
    nodeCodes.value = {
      completed: [],
      pending: [],
      error: [],
    };
    nodeDatas.value = [];
    historicsDatas.value = [];
    task.value = {};
    activeKey.value = ['1', '2'];
  };

  async function getDefDetails(data: Recordable) {
    //获取任务表单、流程图详情
    try {
      const res = await defDetails(data.processInstanceId);
      formConfigDef.value = res.startFormModelDef?.model
        ? JSON.parse(res.startFormModelDef.model)
        : {};
      formData.value = res.startDatas;
      nodeFormData.value = { ...res.startDatas };
      formConfigDef.value.disabled = true;
      bpmnXmlStr.value = res.source;
      showBpmn.value = true;
    } catch (error) {
      console.log('error', error);
    }
  }

  async function getFormModelById(id: string) {
    try {
      //获取节点表单数据
      const res = await getFormModel(id);
      nodeFormConfigDef.value = res.model ? JSON.parse(res.model) : {};
      showNodeBpmn.value = true;
    } catch (error) {
      console.log('error', error);
    }
  }

  async function getHistoricsAndActive() {
    // 获取历史审批节点及当前激活节点
    try {
      const res = await getHistorics(task.value.processInstanceId);
      const res2 = await getActive(task.value.processInstanceId);
      nodeDatas.value = [].concat(res, res2);

      historicsDatas.value = res.filter((item) => {
        return item.activityType === 'userTask';
      });
      setFlowNodeStatus(res);
      setFlowNodePending(res2);
    } catch (error) {
      console.log('error', error);
    }
  }

  function setFlowNodeStatus(data: [Recordable]) {
    // 获取已审批流程节点状态
    const completed: string[] = [];
    const error: string[] = [];
    data.forEach((item: Recordable) => {
      // 移除重复办理节点，以最后一次为准 -begin
      if (completed.includes(item.activityId)) {
        completed.splice(completed.indexOf(item.activityId), 1);
      }
      if (error.includes(item.activityId)) {
        error.splice(error.indexOf(item.activityId), 1);
      }
      // 移除重复，以最后一次为准 - end
      if (item.canceled) {
        error.push(item.activityId);
      } else {
        completed.push(item.activityId);
      }
    });
    nodeCodes.value.completed = completed;
    nodeCodes.value.error = error;
  }

  function setFlowNodePending(data: Recordable) {
    // 获取已审批流程节点状态
    const pending: string[] = [];
    data.forEach((item) => {
      // 移除重复办理节点，以最后一次为准 -begin
      if (nodeCodes.value.completed.includes(item.taskDefinitionKey)) {
        nodeCodes.value.completed.splice(
          nodeCodes.value.completed.indexOf(item.taskDefinitionKey),
          1,
        );
      }
      if (nodeCodes.value.error.includes(item.taskDefinitionKey)) {
        nodeCodes.value.error.splice(nodeCodes.value.error.indexOf(item.taskDefinitionKey), 1);
      }
      // 移除重复，以最后一次为准 - end
      pending.push(item.taskDefinitionKey);
    });
    nodeCodes.value.pending = pending;
  }

  function getFirstName(data: Recordable) {
    const nickname = data.textMap?.assignee$DICT_TEXT_;
    return nickname?.charAt(0);
  }

  function fomart(date: string) {
    return dateUtil(date).format('YYYY-MM-DD HH:mm:ss');
  }

  async function handleCancel() {
    //关闭
    showBpmn.value = false;
    closeModal();
  }

  async function handleClaim() {
    //拾取
    try {
      await claimTask(task.value.id);
      closeModal();
      emit('success');
    } catch (error) {
      console.log('error', error);
    }
  }

  const handleComplete = async () => {
    // 同意、签核流程
    try {
      let res;
      if (task.value.hasForm) {
        const _data = await vFormNode?.value.validate?.();
        res = await completeTask(task.value.id, _data);
        await vFormNode?.value.bpmnSubmitAfter?.({ state: 'NODE_COMPLETED', data: _data });
      } else {
        res = await completeTask(task.value.id, {});
      }
      // 审批完成回写业务单状态为已审核
      if (res.state === 'COMPLETED') {
        await vFormCreate?.value.bpmnSubmitAfter?.({ state: 'COMPLETED' });
      }
      closeModal();
      emit('success');
    } catch (error) {
      console.log('error', error);
    }
  };

  const handleRejectLast = () => {
    // 驳回上一节点
    try {
      openModalCommon();
      rejectType.value = 'rejectLast';
    } catch (error) {
      console.log('error', error);
    }
  };

  const handleRejectFirst = () => {
    // 不同意
    try {
      openModalCommon();
      rejectType.value = 'rejectFirst';
    } catch (error) {
      console.log('error', error);
    }
  };

  const handleTaskCancel = async () => {
    // 撤回任务
    try {
      await cancelTask(task.value.id);
      await vFormCreate?.value.bpmnSubmitAfter?.({ state: 'INTERNALLY_CANCEL' });
      closeModal();
      emit('success');
    } catch (error) {
      console.log('error', error);
    }
  };

  function handleDetail(data: Recordable) {
    openModalHistoryForm(true, { record: data, formData: formData });
  }

  const commentOk = async (data: Recordable) => {
    try {
      if (rejectType.value === 'rejectFirst') {
        await rejectfirstTask(task.value.id, data);
        // 驳回后回写业务单状态为已终止
        await vFormCreate?.value.bpmnSubmitAfter?.({ state: 'INTERNALLY_TERMINATED' });
      } else if (rejectType.value === 'rejectLast') {
        await rejectlastTask(task.value.id, data);
      }
      closeModal();
      emit('success');
    } catch (error) {
      console.log('error', error);
    }
  };
</script>
<style lang="less">
  .containers {
    .ant-modal .ant-modal-body > .scrollbar {
      padding: 0;
    }

    .ant-modal .ant-modal-header {
      margin-bottom: 0;
    }
  }
</style>
