create table product_feedbacks
(
    id serial not null primary key,
    user_id int not null,
    product_id int not null,
    feedback text not null,
    date_time timestamp not null,
    foreign key (user_id) references users (id),
    foreign key (product_id) references products (id)
);