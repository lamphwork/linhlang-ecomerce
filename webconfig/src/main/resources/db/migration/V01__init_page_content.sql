create table menu_config
(
    id          varchar(100) primary key,
    name        varchar(4000)  not null,
    parent_id    varchar(100),
    path_root    varchar(1000),
    path_type    varchar(200),
    path_link    varchar(1000),
    tag         varchar(1000),
    create_time timestamp      not null,
    update_time timestamp,
    status      numeric(15, 2) not null
);
