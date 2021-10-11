drop table if exists tb_attachment CASCADE;
drop table if exists tb_notice CASCADE;
drop table if exists tb_user CASCADE;

create table tb_attachment (
    att_id varchar(50) not null,
    created_by varchar(50),
    created_dt timestamp,
    modified_by varchar(50),
    modified_dt timestamp,
    content_type varchar(30),
    download_cnt bigint,
    file_nm varchar(50),
    file_size bigint,
    saved_file_dir varchar(100),
    notice_id varchar(50),
    primary key (att_id)
);

create table tb_notice (
    notice_id varchar(50) not null,
    created_by varchar(50),
    created_dt timestamp,
    modified_by varchar(50),
    modified_dt timestamp,
    content clob,
    read_cnt bigint,
    title varchar(50),
    user_id varchar(50),
    primary key (notice_id)
);

create table tb_user (
    user_id varchar(50) not null,
    created_by varchar(50),
    created_dt timestamp,
    modified_by varchar(50),
    modified_dt timestamp,
    email varchar(30) not null,
    enabled boolean default 0 not null,
    name varchar(20) not null,
    password varchar(100) not null,
    role varchar(15) not null,
    primary key (user_id)
);

alter table tb_user
drop constraint if exists UK_4vih17mube9j7cqyjlfbcrk4m;

alter table tb_user
    add constraint UK_4vih17mube9j7cqyjlfbcrk4m unique (email);

alter table tb_attachment
    add constraint FKrdqrnxaloq5fm51so1asa2uuo
        foreign key (notice_id)
            references tb_notice;

alter table tb_notice
    add constraint FKk13e5la5lb855d9agsf9lgsn4
        foreign key (user_id)
            references tb_user
            on delete cascade;