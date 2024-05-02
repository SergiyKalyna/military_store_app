create table discounts(
    id serial not null primary key,
    user_id int not null,
    discount_code varchar(25) not null,
    discount double precision not null,
    usage_limit int not null,
    expiration_date timestamp not null
)