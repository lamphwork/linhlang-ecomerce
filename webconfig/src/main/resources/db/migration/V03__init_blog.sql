create table blog
(
    id            varchar(100) primary key,
    title          varchar(4000),
    content          longtext,
    create_user      varchar(100),
    blog_category    varchar(100),
    blog_quote        longtext,
    seo_title       varchar(100),
    seo_description      varchar(400),
    seo_url  varchar(1000),
    canonical_url varchar(1000),
    meta_index  varchar(100),
    meta_follow varchar(100),
    display_time   timestamp ,
    is_display numeric(15, 2) ,
    image         varchar(300),
    image_description varchar(1000),
    tag             varchar(1000),
    status      numeric(15, 2) not null,
    create_time   timestamp      not null,
    update_time   timestamp
);