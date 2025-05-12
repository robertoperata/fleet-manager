CREATE SEQUENCE vehicle_seq INCREMENT BY 50;
CREATE SEQUENCE brand_seq INCREMENT BY 50;

create table if not exists vehicle
(
    id             bigint not null primary key,
    name           varchar(255),
    max_passengers integer,
    wheel_chair    boolean not null,
    brand_id       bigint,
    engine_type    varchar(255)
);

create table if not exists brand
(
    id          bigint not null primary key,
    engine_type varchar(10),
    color       varchar(50),
    model       varchar(100),
    name        varchar(100),
    plate       varchar(10)
);


alter table vehicle
    add constraint fkbfl9i7e2bk7n15h418bk7vtw1
        foreign key (brand_id) references brand;

