INSERT IGNORE INTO permission (id, name, description) VALUES
  ('000', 'READ_PERMISSION', 'The allowing read permission'),

  ('111', 'WRITE_ROLE', 'The allowing create role'),
  ('112', 'READ_ROLE', 'The allowing read role'),
  ('113', 'UPDATE_ROLE', 'The allowing update role'),
  ('114', 'DELETE-ROLE', 'The allowing delete role'),

  ('221', 'WRITE_TOPIC', 'The allowing create topic'),
  ('222', 'READ_TOPIC', 'The allowing read topic'),
  ('223', 'UPDATE_TOPIC', 'The allowing update topic'),
  ('224', 'DELETE_TOPIC', 'The allowing delete topic'),

  ('331', 'WRITE_SOURCE', 'The allowing create source'),
  ('332', 'READ_SOURCE', 'The allowing read source'),
  ('333', 'UPDATE_SOURCE', 'The allowing update source'),
  ('334', 'DELETE_SOURCE', 'The allowing delete source'),

  ('441', 'WRITE_NEWS', 'The allowing create news'),
  ('442', 'READ_NEWS', 'The allowing read news'),
  ('443', 'UPDATE_NEWS', 'The allowing update news'),
  ('444', 'DELETE_NEWS', 'The allowing delete news'),

  ('551', 'WRITE_USER', 'The allowing create user'),
  ('552', 'READ_USER', 'The allowing read user'),
  ('553', 'UPDATE_USER', 'The allowing update user'),
  ('554', 'DELETE_USER', 'The allowing delete user');

INSERT IGNORE INTO role (id, name, description, is_delete) VALUES
  ('100', 'ADMIN', 'Administrator role', false),
  ('200', 'USER', 'User role', false);

INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES
  ('100', '000'),
  ('100', '111'),
  ('100', '112'),
  ('100', '113'),
  ('100', '114'),
  ('100', '221'),
  ('100', '222'),
  ('100', '223'),
  ('100', '224'),
  ('100', '331'),
  ('100', '332'),
  ('100', '333'),
  ('100', '334'),
  ('100', '441'),
  ('100', '442'),
  ('100', '443'),
  ('100', '444'),
  ('100', '551'),
  ('100', '552'),
  ('100', '553'),
  ('100', '554'),

  ('200', '222'),
  ('200', '332'),
  ('200', '442'),
  ('200', '442');

INSERT IGNORE INTO user (id, username, full_name, password) VALUES
  ('1111',"admin", "admin", "$2a$12$LYrbFyhjyxlj/Pj.E8r2Ge3N.xpuROS00uDHikMv2xubiilNwT96W");

INSERT IGNORE INTO user_roles (user_id, role_id) VALUES
  ('1111','100');
