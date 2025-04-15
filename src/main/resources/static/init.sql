create table users
(
    user_id     bigint auto_increment primary key,
    username    varchar(50)                        not null,
    created_at  datetime default CURRENT_TIMESTAMP null,
    modified_at datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

create table products
(
    product_id  bigint auto_increment primary key,
    prd_name    varchar(100)                       not null,
    prd_price   int                                not null,
    prd_stock   int      default 0                 not null,
    created_at  datetime default CURRENT_TIMESTAMP not null,
    modified_at datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

create table orders
(
    order_id     bigint auto_increment primary key,
    user_id      bigint                                                        not null,
    product_id   bigint                                                        not null,
    order_status enum ('INITIAL', 'PAID', 'CANCELED') default 'INITIAL'         not null,
    total_price  int                                 default 0                 not null,
    quantity     int                                                           not null,
    created_at   datetime                            default CURRENT_TIMESTAMP not null,
    modified_at  datetime                            default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
);

create table payments
(
    payments_id    bigint auto_increment primary key,
    order_id       bigint                               not null,
    payment_method varchar(30)                          not null comment 'e.g., KAKAOPAY',
    payment_status enum ('INITIAL', 'PAID', 'CANCELED') null,
    amount         int default 0                        not null,
    transaction_id varchar(100)                         null,
    paid_at        datetime                             null,
    canceled_at    datetime                             null,
);