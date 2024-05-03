create table delivery_details (
    id serial not null primary key,
    city varchar(25) not null,
    post_number integer not null,
    recipient_name varchar(50) not null,
    recipient_phone varchar(50) not null
);