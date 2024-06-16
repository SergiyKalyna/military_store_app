create table images (
    id serial not null primary key,
    product_id integer not null,
    google_drive_id varchar(50) not null,
    ordinal_number integer not null,
    foreign key (product_id) references products(id)
)