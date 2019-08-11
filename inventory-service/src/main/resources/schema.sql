create schema if not exists inventory;
use inventory;

create table if not exists inventory (
	inventory_id int(11) not null auto_increment primary key,
    product_id int(11) not null,
    quantity int(11) not null
);