
create table category_blog
(
    id            varchar(100) primary key,
    title          varchar(4000),
    content          longtext,
    feedburner      varchar(100),
    seo_title       varchar(100),
    seo_description      varchar(400),
    seo_url  varchar(1000),
    comment_rule numeric(15, 2) ,
    list_menu varchar(1000),
    status      numeric(15, 2) not null,
    page_interface    varchar(200),
    create_time   timestamp      not null,
    update_time   timestamp
);