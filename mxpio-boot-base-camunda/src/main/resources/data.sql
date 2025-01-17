-- 菜单信息
INSERT INTO mb_url (id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_, create_dept, rul_type_) VALUES ('3271a148-9834-40b3-8748-4b4e541db246', NULL, NULL, NULL, NULL, 'blank', NULL, 'branches', 0, NULL, 1, '15', '', 'flow', '流程管理', 0, 'QS01', 'M');
INSERT INTO mb_url (id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_, create_dept, rul_type_) VALUES ('043e2b6d-7b72-4223-9706-10c50cb4bcb4', NULL, NULL, NULL, NULL, 'blank', NULL, 'fork', 0, NULL, 1, '16', '', 'flowInstance', '流程使用', 0, 'A0102', 'M');

INSERT INTO mb_url (id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_, create_dept, rul_type_) VALUES ('cb95c378-fd49-428e-b155-d7dcaeea7047', NULL, NULL, NULL, NULL, 'flow/process/ProcessStartList', NULL, NULL, 1, NULL, 1, '1', '043e2b6d-7b72-4223-9706-10c50cb4bcb4', 'processStartList', '流程发起', 0, 'A0102', 'C');
INSERT INTO mb_url (id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_, create_dept, rul_type_) VALUES ('f1122e4a-3ded-4596-a9b1-244978434e71', NULL, NULL, NULL, NULL, 'flow/task/MyApplyList', NULL, NULL, 1, NULL, 1, '3', '043e2b6d-7b72-4223-9706-10c50cb4bcb4', 'myApplyList', '我的申请', 0, 'A0102', 'C');
INSERT INTO mb_url (id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_, create_dept, rul_type_) VALUES ('cebf3bf5-6ef5-4aac-a342-f2bec551f3a7', NULL, NULL, NULL, NULL, 'flow/task/MyTaskList', NULL, NULL, 1, NULL, 1, '3', '043e2b6d-7b72-4223-9706-10c50cb4bcb4', 'myTaskList', '我的任务', 0, 'A0102', 'C');
INSERT INTO mb_url (id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_, create_dept, rul_type_) VALUES ('8dbc637b-625c-4749-b022-513834e0266e', NULL, NULL, NULL, NULL, 'flow/form/FormList', NULL, NULL, 1, NULL, 1, '3', '3271a148-9834-40b3-8748-4b4e541db246', 'formList', '表单管理', 0, 'A0102', 'C');
INSERT INTO mb_url (id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_, create_dept, rul_type_) VALUES ('6a72cd9a-fbc5-4e40-9ebe-86280d3ed735', NULL, NULL, NULL, NULL, 'flow/process/ProcessList', NULL, NULL, 1, NULL, 1, '2', '3271a148-9834-40b3-8748-4b4e541db246', 'processList', '流程监控', 0, 'A0102', 'C');
INSERT INTO mb_url (id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_, create_dept, rul_type_) VALUES ('165f7069-a026-48b7-8620-27d60facbb19', NULL, NULL, NULL, NULL, 'flow/bpmn/BpmnList', NULL, NULL, 1, NULL, 1, '1', '3271a148-9834-40b3-8748-4b4e541db246', 'bpmnList', '流程列表', 0, 'QS01', 'C');


-- 流程字典
INSERT INTO mb_dict (id_, create_by, create_time, update_by, update_time, dict_code_, dict_default_value_, dict_desc_, dict_name_, dict_type_, create_dept) VALUES ('a606339a-077a-4000-8022-550aecb49694', 'admin', '2023-06-14 10:16:21.071000', NULL, NULL, 'MB_FLOW_DEPLOY_STATUS', '01', NULL, '流程定义发布状态', '1', 'A0102');
INSERT INTO mb_dict_item (id_, create_by, create_time, update_by, update_time, dict_id_, item_desc_, item_sort_, item_status_, item_text_, item_value_, create_dept) VALUES ('0aba6ded-5fbd-4cd8-a498-3c8de20ff952', 'admin', '2023-06-14 10:17:10.089000', NULL, NULL, 'a606339a-077a-4000-8022-550aecb49694', NULL, '1', '1', '更新', '03', 'A0102');
INSERT INTO mb_dict_item (id_, create_by, create_time, update_by, update_time, dict_id_, item_desc_, item_sort_, item_status_, item_text_, item_value_, create_dept) VALUES ('f036e326-e829-43f4-80ab-a247355273d3', 'admin', '2023-06-14 10:17:01.329000', NULL, NULL, 'a606339a-077a-4000-8022-550aecb49694', NULL, '1', '1', '发布', '02', 'A0102');
INSERT INTO mb_dict_item (id_, create_by, create_time, update_by, update_time, dict_id_, item_desc_, item_sort_, item_status_, item_text_, item_value_, create_dept) VALUES ('4fbf448e-e8ae-4497-8370-a38926467bb6', 'admin', '2023-06-14 10:16:50.525000', NULL, NULL, 'a606339a-077a-4000-8022-550aecb49694', NULL, '1', '1', '新增', '01', 'A0102');


INSERT INTO mb_dict(id_, create_by, create_time, update_by, update_time, dict_code_, dict_default_value_, dict_desc_, dict_name_, dict_type_, create_dept) VALUES ('69ce7483-86c7-4768-bde8-b4db17e8d669', 'admin', '2024-03-25 10:09:05', NULL, NULL, 'MB_BPMN_DEPLOY_CODE', NULL, NULL, '流程发布状态', '1', 'QS01');
INSERT INTO mb_dict_item(id_, create_by, create_time, update_by, update_time, dict_id_, item_desc_, item_sort_, item_status_, item_text_, item_value_, create_dept) VALUES ('35236434-b87f-4d9e-b8cc-2d99cbb305a9', 'admin', '2024-03-25 10:10:19', NULL, NULL, '69ce7483-86c7-4768-bde8-b4db17e8d669', NULL, '1', '1', '已修改', '03', 'QS01');
INSERT INTO mb_dict_item(id_, create_by, create_time, update_by, update_time, dict_id_, item_desc_, item_sort_, item_status_, item_text_, item_value_, create_dept) VALUES ('202140e6-48f4-4982-8359-2642bfda62e3', 'admin', '2024-03-25 10:09:44', NULL, NULL, '69ce7483-86c7-4768-bde8-b4db17e8d669', NULL, '1', '1', '已发布', '02', 'QS01');
INSERT INTO mb_dict_item(id_, create_by, create_time, update_by, update_time, dict_id_, item_desc_, item_sort_, item_status_, item_text_, item_value_, create_dept) VALUES ('bc42e78d-b9e1-4b3f-9f8e-313146b2718d', 'admin', '2024-03-25 10:09:27', NULL, NULL, '69ce7483-86c7-4768-bde8-b4db17e8d669', NULL, '1', '1', '开立', '01', 'QS01');

-- 流程待办按流程编码传参查询待办菜单
INSERT INTO mb_url(id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_, create_dept, rul_type_) VALUES ('e8082677-c319-4388-96cd-d19b486e821b', 'admin', '2024-06-04 11:11:30.591000', NULL, NULL, 'flow/task/MyTaskList', NULL, NULL, 1, NULL, 0, 4, '043e2b6d-7b72-4223-9706-10c50cb4bcb4', '/flowInstance/myTaskListByCode/:code', '我的待办', 0, 'QS01', 'C');

INSERT INTO mb_dict(id_, create_by, create_time, update_by, update_time, dict_code_, dict_default_value_, dict_desc_, dict_name_, dict_type_, create_dept) VALUES ('3139e634-4b87-4467-8113-0821f1de8ea0', 'admin', '2025-01-15 09:29:35', NULL, NULL, 'MB_BPMN_BIZ_TYPE', NULL, NULL, '流程业务分类', '2', 'QS010700');


drop table if exists V_BPMN_ALL_TASKS;
CREATE OR REPLACE VIEW V_BPMN_ALL_TASKS AS
select
    t.* ,
    p.START_TIME_ as proc_start_time_,
    p.PROC_DEF_KEY_ as process_definition_key_,
    v.TEXT_ as proc_title_,
    w.TEXT_ as biz_type_,
    TIMESTAMPDIFF(day,t.create_time_,now()) as create_days
from
    (
        select
            distinct
            res.ID_ as VIEW_ID_,
            RES.REV_,
            RES.ID_,
            RES.NAME_,
            RES.PARENT_TASK_ID_,
            RES.DESCRIPTION_,
            RES.PRIORITY_,
            RES.CREATE_TIME_,
            RES.OWNER_,
            RES.ASSIGNEE_,
            RES.DELEGATION_,
            RES.EXECUTION_ID_,
            RES.PROC_INST_ID_,
            RES.PROC_DEF_ID_,
            (
                select
                    NAME_
                from
                    ACT_RE_PROCDEF F
                where
                    F.ID_ = RES.PROC_DEF_ID_ ) PROC_DEF_NAME_,
            RES.CASE_EXECUTION_ID_,
            RES.CASE_INST_ID_,
            RES.CASE_DEF_ID_,
            RES.TASK_DEF_KEY_,
            RES.DUE_DATE_,
            RES.FOLLOW_UP_DATE_,
            RES.SUSPENSION_STATE_,
            RES.TENANT_ID_,
            '' as CANDIDATE_USER,
            '' as CANDIDATE_GROUP,
            (
                select
                    TEXT_
                from
                    ACT_RU_VARIABLE V
                where
                    V.EXECUTION_ID_ = RES.EXECUTION_ID_
                  and V.PROC_INST_ID_ = RES.PROC_INST_ID_
                  and V.PROC_DEF_ID_ = RES.PROC_DEF_ID_
                  and V.NAME_ = 'createBy') as PROC_START_USER_ID_,
            IFNULL((select TEXT_ from ACT_RU_VARIABLE V where V.EXECUTION_ID_ = RES.EXECUTION_ID_ and V.PROC_INST_ID_ = RES.PROC_INST_ID_ and V.PROC_DEF_ID_ = RES.PROC_DEF_ID_ and V.NAME_ = '$BPMN_SORT_FLAG_'), '0') as BPMN_SORT_FLAG_
        from
            ACT_RU_TASK RES
        where
            RES.SUSPENSION_STATE_ = 1
        union all
        select
            distinct
            CONCAT( res.ID_, i.ID_ ) as VIEW_ID_,
            RES.REV_,
            RES.ID_,
            RES.NAME_,
            RES.PARENT_TASK_ID_,
            RES.DESCRIPTION_,
            RES.PRIORITY_,
            RES.CREATE_TIME_,
            RES.OWNER_,
            RES.ASSIGNEE_,
            RES.DELEGATION_,
            RES.EXECUTION_ID_,
            RES.PROC_INST_ID_,
            RES.PROC_DEF_ID_,
            (
                select
                    NAME_
                from
                    ACT_RE_PROCDEF F
                where
                    F.ID_ = RES.PROC_DEF_ID_ ) PROC_DEF_NAME_,
            RES.CASE_EXECUTION_ID_,
            RES.CASE_INST_ID_,
            RES.CASE_DEF_ID_,
            RES.TASK_DEF_KEY_,
            RES.DUE_DATE_,
            RES.FOLLOW_UP_DATE_,
            RES.SUSPENSION_STATE_,
            RES.TENANT_ID_,
            I.USER_ID_ as CANDIDATE_USER,
            I.GROUP_ID_ as CANDIDATE_GROUP,
            (
                select
                    TEXT_
                from
                    ACT_RU_VARIABLE V
                where
                    V.EXECUTION_ID_ = RES.EXECUTION_ID_
                  and V.PROC_INST_ID_ = RES.PROC_INST_ID_
                  and V.PROC_DEF_ID_ = RES.PROC_DEF_ID_
                  and V.NAME_ = 'createBy') as PROC_START_USER_ID_,
            IFNULL((select TEXT_ from ACT_RU_VARIABLE V where V.EXECUTION_ID_ = RES.EXECUTION_ID_ and V.PROC_INST_ID_ = RES.PROC_INST_ID_ and V.PROC_DEF_ID_ = RES.PROC_DEF_ID_ and V.NAME_ = '$BPMN_SORT_FLAG_'), '0') as BPMN_SORT_FLAG_
        from
            ACT_RU_TASK RES
                left join ACT_RU_IDENTITYLINK I on
                I.TASK_ID_ = RES.ID_
        where
            RES.ASSIGNEE_ is null
          and I.TYPE_ = 'candidate'
          and RES.SUSPENSION_STATE_ = 1
    ) t
        left join ACT_HI_PROCINST p on
        t.PROC_INST_ID_ = p.PROC_INST_ID_
        left join act_hi_varinst v on
        t.PROC_INST_ID_ = v.PROC_INST_ID_
            and v.NAME_ = '$BPMN_TITLE_'
        left join act_hi_varinst w on
        t.PROC_INST_ID_ = w.PROC_INST_ID_
            and w.NAME_ = '$BPMN_BIZ_TYPE_';


