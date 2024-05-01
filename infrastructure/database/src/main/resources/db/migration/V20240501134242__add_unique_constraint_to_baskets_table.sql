alter table baskets
    add constraint baskets_product_user_unique unique (user_id, product_stock_details_id);