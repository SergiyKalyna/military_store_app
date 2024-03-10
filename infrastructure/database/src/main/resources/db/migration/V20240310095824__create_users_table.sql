create table users (
    id serial not null primary key,
    login varchar(30) not null unique,
    password varchar(30) not null,
    first_name varchar(30) not null,
    second_name varchar(30) not null,
    email varchar(50) not null,
    phone varchar(20) not null,
    gender varchar(10) not null,
    birthday_date date not null,
    role varchar(12) not null default 'USER',
    is_banned boolean not null default false
);