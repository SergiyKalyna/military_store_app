create table baskets (
    id serial not null primary key,
    user_id integer not null,
    product_stock_details_id integer not null,
    quantity integer not null,
    foreign key (user_id) references users(id),
    foreign key (product_stock_details_id) references product_stock_details(id)
)