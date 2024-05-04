create table order_details (
    id serial not null primary key,
    order_id integer not null,
    product_stock_details_id integer not null,
    quantity integer not null,
    foreign key (order_id) references orders (id),
    foreign key (product_stock_details_id) references product_stock_details (id)
);