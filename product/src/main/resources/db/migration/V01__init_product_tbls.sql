create table product_image
(
    id         varchar(100) primary key,
    url        varchar(300) not null,
    product_id varchar(100) not null
);

create index idx_product_img on product_image (product_id);

create table product
(
    id            varchar(100) primary key,
    name          varchar(255)   not null,
    image         varchar(300),
    description   longtext,
    price         numeric(15, 2) not null,
    compare_price decimal(15, 2),
    provider_id   varchar(100)   not null,
    category_id   varchar(100)   not null,
    create_time   timestamp      not null,
    update_time   timestamp
);

create index idx_product_price on product (price);
create index idx_product_provider on product (provider_id);
create index idx_product_category on product (category_id);

create table collection
(
    id   varchar(100) primary key,
    name varchar(255) not null
);

create table product_collection
(
    id            varchar(100) primary key,
    product_id    varchar(100) not null,
    collection_id varchar(100) not null
);

create unique index idx_product_collection_uq on product_collection (product_id, collection_id);

create table attribute
(
    code varchar(100) primary key,
    name varchar(255) not null
);

create table variant
(
    id            varchar(100) primary key,
    name          varchar(255) not null,
    image         varchar(300),
    price         decimal(15, 2),
    compare_price decimal(15, 2),
    product_id    varchar(100) not null,
    create_time   timestamp,
    update_time   timestamp
);

create index idx_variant_price on variant (price);
create index idx_variant_product on variant (product_id);

create table properties
(
    id         varchar(100) primary key,
    variant_id varchar(100) not null,
    attribute  varchar(100) not null,
    value      varchar(255) not null
);

create index idx_properties_variant on properties (variant_id);
create index idx_properties_attr on properties (attribute);
create index idx_properties_value on properties (value);

