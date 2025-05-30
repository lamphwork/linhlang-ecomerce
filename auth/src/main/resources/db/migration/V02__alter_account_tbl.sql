create table if not exists account
(
    id          varchar(100) primary key,
    username    varchar(300) not null,
    password    varchar(150) not null,
    status      varchar(50)  not null,
    create_time timestamp    not null
);

create table if not exists role
(
    code        varchar(100) primary key,
    description varchar(500)
);

create table if not exists account_role
(
    id         varchar(100) primary key,
    account_id varchar(100) not null,
    role_code  varchar(100) not null
);

create unique index idx_account_role on account_role (account_id, role_code);

insert into role(code, description)
values ('ADMIN', 'Admin role'),
       ('USER', 'User role')

