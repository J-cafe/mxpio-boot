INSERT INTO `mb_user`(`username_`, `create_by`, `create_time`, `update_by`, `update_time`, `account_non_expired_`, `account_non_locked_`, `administrator_`, `credentials_non_expired_`, `enabled_`, `nickname_`, `password_`, `salt_`) VALUES ('admin1', NULL, NULL, NULL, NULL, b'1', b'1', b'0', b'1', b'1', '测试', '{bcrypt}$2a$10$.c/TiWuSmvwitqxBZsF5guQ6qQn08E1b1Aldff/LEb.dkeR0b4RGq', NULL);

-- 20220208 字典管理菜单
INSERT INTO `mb_url` (`id_`, `create_by`, `create_time`, `update_by`, `update_time`, `component_`, `description_`, `icon_`, `keep_alive_`, `name_`, `navigable_`, `order_`, `parent_id_`, `path_`, `title_`) VALUES ('b583ca44-0371-4f77-a3e3-87890aedict5', NULL, NULL, NULL, NULL, 'system/DictList', '字典管理', NULL, b'0', 'dictList', b'1', '1', 'd7d6e2e4e2934f2c9385a623fd98c6f3', 'list/dict-list', '字典管理');

-- 20220209 组件示例菜单
INSERT INTO `mb_url` (`id_`, `create_by`, `create_time`, `update_by`, `update_time`, `component_`, `description_`, `icon_`, `keep_alive_`, `name_`, `navigable_`, `order_`, `parent_id_`, `path_`, `title_`) VALUES ('b583ca44-test-1234-a3e3-87890ae56785', NULL, NULL, NULL, NULL, 'blank', '示例页面', 'question-circle', b'1', 'test', b'0', '1', NULL, 'test', '示例页面');
INSERT INTO `mb_url` (`id_`, `create_by`, `create_time`, `update_by`, `update_time`, `component_`, `description_`, `icon_`, `keep_alive_`, `name_`, `navigable_`, `order_`, `parent_id_`, `path_`, `title_`) VALUES ('b583ca44-demo-1234-a3e3-87890ae56785', NULL, NULL, NULL, NULL, 'test/Demo', '常见组件', NULL, b'0', 'test', b'1', '1', 'b583ca44-test-1234-a3e3-87890ae56785', 'test/demo', '常见组件');

-- 20220210 角色管理菜单
INSERT INTO `mb_url` (`id_`, `create_by`, `create_time`, `update_by`, `update_time`, `component_`, `description_`, `icon_`, `keep_alive_`, `name_`, `navigable_`, `order_`, `parent_id_`, `path_`, `title_`) VALUES ('4e672960-61ed-4c79-b895-52b3a3912bd5', NULL, NULL, NULL, NULL, 'system/RoleList', '角色管理', NULL, b'0', 'RoleList', b'1', '4', 'd7d6e2e4e2934f2c9385a623fd98c6f3', 'RoleList', '角色管理');