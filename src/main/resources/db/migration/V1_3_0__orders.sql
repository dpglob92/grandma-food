create table restaurant_order (
    created_at datetime(6) not null,
    deleted_at datetime(6),
    delivered_at datetime(6),
    updated_at datetime(6) not null,
    client_id binary(16) not null,
    id binary(16) not null,
    additional_information varchar(511) not null,
    primary key (id)
) engine=InnoDB;

create table restaurant_order_item (
    quantity integer not null,
    id bigint not null auto_increment,
    order_id binary(16) not null,
    product_id binary(16) not null,
    primary key (id)
) engine=InnoDB;


alter table restaurant_order
   add constraint FKq87se48j5jpj3cg6a93bmckr2
   foreign key (client_id)
   references client (id);

alter table restaurant_order_item
   add constraint FK70no1ysca3711v6u47agrx647
   foreign key (order_id)
   references restaurant_order (id);

alter table restaurant_order_item
   add constraint FKinuypsfm5owlr8p8h2o9gcxuq
   foreign key (product_id)
   references product (id);