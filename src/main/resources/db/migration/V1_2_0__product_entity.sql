create table product (
    available bit not null,
    price decimal(10,2) not null,
    created_at datetime(6) not null,
    deleted_at datetime(6),
    updated_at datetime(6) not null,
    id binary(16) not null,
    description varchar(511) not null,
    name varchar(255) not null,
    food_category enum ('CHICKEN','DESSERTS','FISH','HAMBURGERS_AND_HOTDOGS','KIDS_MEALS','MEATS','VEGAN_FOOD') not null,
    primary key (id)
) engine=InnoDB;

alter table product
     add constraint name_unique_constraint unique (name);