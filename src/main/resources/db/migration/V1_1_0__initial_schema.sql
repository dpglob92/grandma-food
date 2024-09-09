create table client (
    created_at datetime(6) not null,
    deleted_at datetime(6),
    updated_at datetime(6) not null,
    phone varchar(11) not null,
    document_id varchar(15) not null,
    id binary(16) not null,
    delivery_address varchar(500) not null,
    email varchar(255) not null,
    name varchar(255) not null,
    document_type enum ('CC','TE','TI') not null,
    primary key (id)
) engine=InnoDB;

alter table client
       add constraint document_id_unique_constraint unique (document_id);

alter table client
   add constraint email_unique_constraint unique (email);