
-- INSERT INTO mb_role(id_, create_by, create_time, update_by, update_time, description_, name_) VALUES ('f51ee61d-089c-47a3-8b98-1e0cba84fa31', NULL, '2021-04-20 17:14:15.859000', NULL, NULL, '测试角色', '测试角色');
INSERT INTO mb_role (id_, create_by, create_time, update_by, update_time, description_, name_) VALUES ('89b11cb5-7d27-45c4-89f3-60bf4dbf2820', NULL, '2022-07-12 14:26:56.954000', NULL, NULL, NULL, '管理员');


INSERT INTO mb_role_granted_authority(id_, create_by, create_time, update_by, update_time, actor_id_, role_id_) VALUES ('faa2fecd-f3a6-4c33-8f25-018d3f5636ea', NULL, '2021-04-20 17:18:50.431000', NULL, NULL, 'admin', 'f51ee61d-089c-47a3-8b98-1e0cba84fa31');

-- INSERT INTO mb_url (id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_) VALUES ('00bc2e5c-aaee-45d1-ac38-7ea8f7e25cdb', NULL, NULL, NULL, NULL, 'blank', 'Dashboard', 'dashboard', '', 'dashboard', 1, '1', '', 'dashboard', 'Dashboard', 0);
-- INSERT INTO mb_url (id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_) VALUES ('0167d575-230f-4b17-a7e8-79003257ea2e', NULL, NULL, NULL, NULL, 'dashboard/workplace/WorkPlace', '工作台', '', '', 'workplace', 1, '3', '00bc2e5c-aaee-45d1-ac38-7ea8f7e25cdb', 'workplace', '工作台', 0);
-- INSERT INTO mb_url (id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_) VALUES ('05b3c82ddb2536a4a5ee1a4c46b5abef', NULL, NULL, NULL, NULL, 'dashboard/analysis/Analysis', '首页', '', '', 'analysis', 1, '1', '00bc2e5c-aaee-45d1-ac38-7ea8f7e25cdb', 'analysis', '首页', 0);
INSERT INTO mb_url (id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_) VALUES ('d7d6e2e4e2934f2c9385a623fd98c6f3', NULL, NULL, NULL, NULL, 'blank', '系统设置', 'setting', 0, 'isystem', 1, '101', NULL, 'isystem', '系统设置', 0);
INSERT INTO mb_url (id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_) VALUES ('b583ca44-0374-4f77-a3e3-71546aed6d6b', NULL, NULL, NULL, NULL, 'system/UserList', '用户管理', NULL, 0, 'userList', 1, '1', 'd7d6e2e4e2934f2c9385a623fd98c6f3', 'list/userList', '用户管理', 0);
INSERT INTO mb_url (id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_) VALUES ('b583ca44-0371-4f77-a3e3-71546aedert5', NULL, NULL, NULL, NULL, 'system/MenuList', '菜单管理', NULL, 0, 'menuList', 1, '1', 'd7d6e2e4e2934f2c9385a623fd98c6f3', 'list/menuList', '菜单管理', 0);


INSERT INTO mb_user(username_, create_by, create_time, update_by, update_time, account_non_expired_, account_non_locked_, administrator_, credentials_non_expired_, enabled_, nickname_, password_, salt_) VALUES ('admin', NULL, NULL, NULL, NULL, 1, 1, 0, 1, 1, '系统管理员', '{bcrypt}$2a$10$.c/TiWuSmvwitqxBZsF5guQ6qQn08E1b1Aldff/LEb.dkeR0b4RGq', NULL);