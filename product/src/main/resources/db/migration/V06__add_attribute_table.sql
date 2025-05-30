alter table attribute rename column code to id;
alter table attribute add column system_attr smallint not null;

create index idx_attr_system on attribute(system_attr);