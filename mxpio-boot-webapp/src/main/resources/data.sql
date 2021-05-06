INSERT INTO `mb_permission`(`id_`, `create_by`, `create_time`, `update_by`, `update_time`, `attribute_`, `resource_id_`, `resource_type_`, `role_id_`) VALUES ('371b95cf-27c8-4c3f-b55d-17ce296aa5a4', NULL, NULL, NULL, NULL, 'ROLE_f51ee61d-089c-47a3-8b98-1e0cba84fa31', '00bc2e5c-aaee-45d1-ac38-7ea8f7e25cdb', 'URL', 'f51ee61d-089c-47a3-8b98-1e0cba84fa31');
INSERT INTO `mb_permission`(`id_`, `create_by`, `create_time`, `update_by`, `update_time`, `attribute_`, `resource_id_`, `resource_type_`, `role_id_`) VALUES ('47cef405-2bdd-4b36-aace-075566eca42d', NULL, NULL, NULL, NULL, 'ROLE_f51ee61d-089c-47a3-8b98-1e0cba84fa31', 'e018800f-e9e9-45d0-ac7a-b727d7db0d96', 'URL', 'f51ee61d-089c-47a3-8b98-1e0cba84fa31');
INSERT INTO `mb_permission`(`id_`, `create_by`, `create_time`, `update_by`, `update_time`, `attribute_`, `resource_id_`, `resource_type_`, `role_id_`) VALUES ('71152b1a-c343-4bcd-85fb-fc5774704be9', NULL, NULL, NULL, NULL, 'ROLE_f51ee61d-089c-47a3-8b98-1e0cba84fa31', 'f4b53563-626c-4d6c-8c6a-f7223249e5b0', 'URL', 'f51ee61d-089c-47a3-8b98-1e0cba84fa31');
INSERT INTO `mb_permission`(`id_`, `create_by`, `create_time`, `update_by`, `update_time`, `attribute_`, `resource_id_`, `resource_type_`, `role_id_`) VALUES ('9939c0a2-fd00-4104-85a6-44a8fa4463f2', NULL, NULL, NULL, NULL, 'ROLE_f51ee61d-089c-47a3-8b98-1e0cba84fa31', 'b583ca44-0374-4f77-a3e3-71546aed6d6b', 'URL', 'f51ee61d-089c-47a3-8b98-1e0cba84fa31');
INSERT INTO `mb_permission`(`id_`, `create_by`, `create_time`, `update_by`, `update_time`, `attribute_`, `resource_id_`, `resource_type_`, `role_id_`) VALUES ('df26c907-cc50-46d2-a031-bc9bf9014f9c', NULL, NULL, NULL, NULL, 'ROLE_f51ee61d-089c-47a3-8b98-1e0cba84fa31', 'd1777592-4441-4513-9204-980492935acd', 'URL', 'f51ee61d-089c-47a3-8b98-1e0cba84fa31');

INSERT INTO `mb_role`(`id_`, `create_by`, `create_time`, `update_by`, `update_time`, `description_`, `name_`) VALUES ('f51ee61d-089c-47a3-8b98-1e0cba84fa31', NULL, '2021-04-20 17:14:15.859000', NULL, NULL, '测试角色', '测试角色');

INSERT INTO `mb_role_granted_authority`(`id_`, `create_by`, `create_time`, `update_by`, `update_time`, `actor_id_`, `role_id_`) VALUES ('faa2fecd-f3a6-4c33-8f25-018d3f5636ea', NULL, '2021-04-20 17:18:50.431000', NULL, NULL, 'admin', 'f51ee61d-089c-47a3-8b98-1e0cba84fa31');

INSERT INTO `mb_url`(`id_`, `create_by`, `create_time`, `update_by`, `update_time`, `description_`, `icon_`, `name_`, `navigable_`, `order_`, `parent_id_`, `path_`, `keep_alive_`, `component_`, `title_`) VALUES ('00bc2e5c-aaee-45d1-ac38-7ea8f7e25cdb', NULL, NULL, NULL, NULL, '基础配置', 'tune', 'gettingStarted', b'1', 1, 'e018800f-e9e9-45d0-ac7a-b727d7db0d96', 'getting-started', b'1', 'router/gettingStarted', '基础配置');
INSERT INTO `mb_url`(`id_`, `create_by`, `create_time`, `update_by`, `update_time`, `description_`, `icon_`, `name_`, `navigable_`, `order_`, `parent_id_`, `path_`, `keep_alive_`, `component_`, `title_`) VALUES ('e018800f-e9e9-45d0-ac7a-b727d7db0d96', NULL, NULL, NULL, NULL, '快速起步', 'design_services', 'start', b'1', 2, '', '/start', b'1', 'Layout', '快速起步');
INSERT INTO `mb_url`(`id_`, `create_by`, `create_time`, `update_by`, `update_time`, `description_`, `icon_`, `name_`, `navigable_`, `order_`, `parent_id_`, `path_`, `keep_alive_`, `component_`, `title_`) VALUES ('f4b53563-626c-4d6c-8c6a-f7223249e5b0', NULL, NULL, NULL, NULL, '主页', 'home', 'home', b'1', 1, NULL, '/', b'1', 'home/home', '主页');
INSERT INTO `mb_url`(`id_`, `create_by`, `create_time`, `update_by`, `update_time`, `description_`, `icon_`, `name_`, `navigable_`, `order_`, `parent_id_`, `path_`, `keep_alive_`, `component_`, `title_`) VALUES ('d1777592-4441-4513-9204-980492935acd', NULL, NULL, NULL, NULL, '用户列表', 'home', 'user', b'1', 1, 'b583ca44-0374-4f77-a3e3-71546aed6d6b', 'system/user', b'1', 'user/userlist', '用户列表');
INSERT INTO `mb_url`(`id_`, `create_by`, `create_time`, `update_by`, `update_time`, `description_`, `icon_`, `name_`, `navigable_`, `order_`, `parent_id_`, `path_`, `keep_alive_`, `component_`, `title_`) VALUES ('b583ca44-0374-4f77-a3e3-71546aed6d6b', NULL, NULL, NULL, NULL, '系统设置', 'home', 'system', b'1', 3, NULL, '/system', b'1', 'Layout', '系统设置');

INSERT INTO `mb_user`(`username_`, `create_by`, `create_time`, `update_by`, `update_time`, `account_non_expired_`, `account_non_locked_`, `administrator_`, `credentials_non_expired_`, `enabled_`, `nickname_`, `password_`, `salt_`) VALUES ('admin', NULL, NULL, NULL, NULL, b'1', b'1', b'0', b'1', b'1', '系统管理员', '{bcrypt}$2a$10$.c/TiWuSmvwitqxBZsF5guQ6qQn08E1b1Aldff/LEb.dkeR0b4RGq', NULL);
INSERT INTO `mb_user`(`username_`, `create_by`, `create_time`, `update_by`, `update_time`, `account_non_expired_`, `account_non_locked_`, `administrator_`, `credentials_non_expired_`, `enabled_`, `nickname_`, `password_`, `salt_`) VALUES ('admin1', NULL, NULL, NULL, NULL, b'1', b'1', b'0', b'1', b'1', '测试', '{bcrypt}$2a$10$.c/TiWuSmvwitqxBZsF5guQ6qQn08E1b1Aldff/LEb.dkeR0b4RGq', NULL);
