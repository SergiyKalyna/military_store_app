create table orders (
    id serial not null primary key,
    user_id integer not null,
    delivery_details_id integer not null,
    total_amount integer not null,
    order_status varchar(20) not null default 'NEW',
    order_date date not null,
    discount double precision,
    shipping_number varchar(20)
);