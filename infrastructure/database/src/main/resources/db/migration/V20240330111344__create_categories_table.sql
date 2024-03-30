create table categories (
    id serial not null primary key,
    name varchar(50) not null unique
);