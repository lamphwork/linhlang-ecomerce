create table page_content
(
    id            varchar(100) primary key,
    title          varchar(4000),
    content          longtext,
    image         varchar(300),
    seo_title       varchar(100),
    seo_description      varchar(400),
    seo_link  varchar(400),
    page_interface    varchar(200),
    menu_link varchar(400),
    status      numeric(15, 2) not null,
    display_time   timestamp      not null,
    is_display numeric(15, 2) not null,
    create_time   timestamp      not null,
    update_time   timestamp
);