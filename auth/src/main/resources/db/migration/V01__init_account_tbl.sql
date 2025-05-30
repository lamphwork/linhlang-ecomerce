create table if not exists account
(
    id          varchar(100) primary key,
    username    varchar(300) not null,
    password    varchar(150) not null,
    status      varchar(50)  not null,
    create_time timestamp    not null
);

create index idx_username_uq on account (username);
create index idx_account_status on account (status);
