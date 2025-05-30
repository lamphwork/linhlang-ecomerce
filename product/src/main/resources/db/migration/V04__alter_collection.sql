create table seo_data
(
    id          varchar(100) primary key,
    title       varchar(300) not null,
    description varchar(500) not null,
    link        varchar(300),
    link_table  varchar(100) not null,
    link_id     varchar(100) not null
);

create index idx_seo_link_tbl on seo_data (link_table);
create index idx_seo_link_id on seo_data (link_id);

alter table collection
    add column image       varchar(255),
    add column description longtext;
