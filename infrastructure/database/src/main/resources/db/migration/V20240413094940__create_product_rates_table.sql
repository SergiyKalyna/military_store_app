create table product_rates
(
    id         serial           not null primary key,
    user_id    int              not null,
    product_id int              not null,
    rate       double precision not null,
    unique (user_id, product_id),
    foreign key (user_id) references users (id),
    foreign key (product_id) references products (id)
);