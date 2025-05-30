create table if not exists web_content
(
    id          varchar(100) primary key,
    type        varchar(100) not null,
    title       varchar(500),
    content     varchar(3000),
    image_url   varchar(500),
    order_index integer,
    visible     boolean
);

create index idx_web_content_type on web_content (type)