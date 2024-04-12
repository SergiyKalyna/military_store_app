alter table product_stock_details
    add constraint stock_availability_check check (stock_availability >= 0);