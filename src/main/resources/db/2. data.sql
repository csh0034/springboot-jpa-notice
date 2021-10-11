insert into tb_user values ('user-01', 'system', '2020-11-11 00:00:11', 'system', '2020-11-11 00:00:11', 'user01@gmail.com', true, 'name01', '{bcrypt}$2a$10$NGkfJGB16oYP6oNiECAvaOQU2Rf5uJJjRyuDREAjgNYql41csyGLm', 'ROLE_USER');
insert into tb_user values ('user-02', 'system', '2020-11-11 00:00:22', 'system', '2020-11-11 00:00:22', 'user02@gmail.com', true, 'name02', '{bcrypt}$2a$10$NGkfJGB16oYP6oNiECAvaOQU2Rf5uJJjRyuDREAjgNYql41csyGLm', 'ROLE_USER');
insert into tb_user values ('user-03', 'system', '2020-11-11 00:00:33', 'system', '2020-11-11 00:00:33', 'user03@gmail.com', true, 'name03', '{bcrypt}$2a$10$NGkfJGB16oYP6oNiECAvaOQU2Rf5uJJjRyuDREAjgNYql41csyGLm', 'ROLE_USER');

insert into tb_notice values ('notice-01', 'user-01', '2020-11-11 00:00:11', 'user-01', '2020-11-11 00:00:11', 'Sample Content', 0, 'Sample Title', 'user-01');

insert into tb_attachment values ('att-01', 'user-01', '2020-11-11 00:00:11', 'user-01', '2020-11-11 00:00:11', 'text/plain', 0, 'Sample File1', 10, '/att-01', 'notice-01');
insert into tb_attachment values ('att-02', 'user-01', '2020-11-11 00:00:11', 'user-01', '2020-11-11 00:00:11', 'text/plain', 0, 'Sample File2', 10, '/att-02', 'notice-01');