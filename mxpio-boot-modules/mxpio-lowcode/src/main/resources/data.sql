-- 低代码开发菜单
INSERT INTO mb_url (id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_, create_dept, rul_type_) VALUES ('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'admin', '2025-05-19 10:00:00.000000', NULL, NULL, 'blank', NULL, 'code', 1, NULL, 1, '18', '', 'lowcode', '低代码开发', 0, 'A0102', 'M');

-- 模型管理
INSERT INTO mb_url (id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_, create_dept, rul_type_) VALUES ('b2c3d4e5-f6a7-8901-bcde-f12345678901', 'admin', '2025-05-19 10:00:00.000000', NULL, NULL, 'modelList', '数据模型管理', NULL, 1, NULL, 1, '1', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'modelList', '模型管理', 0, 'A0102', 'C');

-- 页面管理
INSERT INTO mb_url (id_, create_by, create_time, update_by, update_time, component_, description_, icon_, keep_alive_, name_, navigable_, order_, parent_id_, path_, title_, outside_, create_dept, rul_type_) VALUES ('c3d4e5f6-a789-0123-bcde-f12345678902', 'admin', '2025-05-19 10:00:00.000000', NULL, NULL, 'pageList', '低代码页面管理', NULL, 1, NULL, 1, '2', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'pageList', '页面管理', 0, 'A0102', 'C');
