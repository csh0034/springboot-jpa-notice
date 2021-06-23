drop table if exists `tb_attachment`;
drop table if exists `tb_notice`;
drop table if exists `tb_user`;

create table `tb_attachment` (
    `att_id` varchar(255) not null,
    `created_by` varchar(255),
    `created_dt` timestamp,
    `modified_by` varchar(255),
    `modified_dt` timestamp,
    `content_type` varchar(255),
    `download_cnt` bigint,
    `file_nm` varchar(255),
    `file_size` bigint,
    `saved_file_dir` varchar(255),
    `notice_id` varchar(255),
    primary key (att_id)
);

create table `tb_notice` (
    `notice_id` varchar(255) not null,
    `created_by` varchar(255),
    `created_dt` timestamp,
    `modified_by` varchar(255),
    `modified_dt` timestamp,
    `content` clob,
    `read_cnt` bigint,
    `title` varchar(255),
    `user_id` varchar(255),
    primary key (notice_id)
);

create table `tb_user` (
    `user_id` varchar(255) not null,
    `created_by` varchar(255),
    `created_dt` timestamp,
    `modified_by` varchar(255),
    `modified_dt` timestamp,
    `authority` varchar(255) not null,
    `enabled` boolean not null,
    `login_id` varchar(255) not null,
    `password` varchar(255) not null,
    `username` varchar(255),
    primary key (user_id)
);

create index IDX_NOTICE_TITLE on `tb_notice` (title);
alter table `tb_user` add constraint UK_babp79hoay0m1j87xl3cvpmji unique (login_id);
alter table `tb_attachment` add constraint FKrdqrnxaloq5fm51so1asa2uuo foreign key (notice_id) references `tb_notice`;
alter table `tb_notice` add constraint FKk13e5la5lb855d9agsf9lgsn4 foreign key (user_id) references `tb_user`;