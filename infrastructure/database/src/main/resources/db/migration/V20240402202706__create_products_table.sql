create table products (
    id serial primary key,
    name varchar(150) not null,
    description text not null,
    price integer not null,
    subcategory_id int not null,
    size_grid_type varchar(20) not null,
    product_tag varchar(20) not null,
    is_in_stock boolean not null default true,
    foreign key (subcategory_id) references subcategories (id)
);