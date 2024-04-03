create table product_stock_details (
    id serial primary key,
    product_size varchar(10) not null,
    product_id int not null,
    stock_availability int not null default 0,
    foreign key (product_id) references products (id)
);