create table subcategories (
    id serial not null primary key,
    name varchar(50) not null unique,
    category_id int not null,
    foreign key (category_id) references categories (id)
);