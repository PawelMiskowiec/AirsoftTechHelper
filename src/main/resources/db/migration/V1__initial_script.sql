create sequence hibernate_sequence start 1 increment 1;
create table owners
(
    id       int8 not null,
    version  int8 not null,
    city     varchar(255),
    email    varchar(255),
    name     varchar(255),
    phone    varchar(255),
    street   varchar(255),
    zip_code varchar(255),
    primary key (id)
);
create table parts
(
    id       int8 not null,
    version  int8 not null,
    category varchar(255),
    name     varchar(255),
    primary key (id)
);
create table replica_parts
(
    id         int8 not null,
    version    int8 not null,
    created_at timestamp,
    notes      varchar(255),
    updated_at timestamp,
    part_id    int8,
    replica_id int8,
    primary key (id)
);
create table replicas
(
    id                   int8 not null,
    version              int8 not null,
    additional_equipment varchar(255),
    created_at           timestamp,
    description          varchar(255),
    name                 varchar(255),
    status               varchar(255),
    updated_at           timestamp,
    owner_id             int8,
    user_id              int8,
    primary key (id)
);
create table to_dos
(
    id         int8 not null,
    version    int8 not null,
    content    varchar(255),
    title      varchar(255),
    replica_id int8,
    primary key (id)
);
create table users
(
    id         int8    not null,
    version    int8    not null,
    created_at timestamp,
    locked     boolean not null,
    password   varchar(255),
    updated_at timestamp,
    username   varchar(255),
    primary key (id)
);
create table users_roles
(
    user_id int8 not null,
    role    varchar(255)
);
alter table owners
    add constraint UK_dw1w2xj1axp1le5oionrjfk7t unique (email);
alter table users
    add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);
alter table replica_parts
    add constraint FKfx3ayt12bs0v4nuitp49nqx4y foreign key (part_id) references parts;
alter table replica_parts
    add constraint FKp2y75l7j3ukvlcfbql1lx5mrl foreign key (replica_id) references replicas;
alter table replicas
    add constraint FK34jpbdxwqq8t3xhqltxg3bdoo foreign key (owner_id) references owners;
alter table replicas
    add constraint FK7jqhqx7eobxf3qwbd2infhf8l foreign key (user_id) references users;
alter table to_dos
    add constraint FKds98vyowx7667agwal8ge2se3 foreign key (replica_id) references replicas;
alter table users_roles
    add constraint FK2o0jvgh89lemvvo17cbqvdxaa foreign key (user_id) references users;
