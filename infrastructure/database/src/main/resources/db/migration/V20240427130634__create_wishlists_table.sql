create table wishlists
(
    id serial not null primary key,
    user_id integer not null,
    product_id integer not null,
    foreign key (user_id) references users (id),
    foreign key (product_id) references products (id)
);