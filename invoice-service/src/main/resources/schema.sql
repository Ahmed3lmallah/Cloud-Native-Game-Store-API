create schema if not exists invoice;
use invoice;

create table if not exists invoice (
	invoice_id int(11) not null auto_increment primary key,
    customer_id int(11) not null,
    purchase_date date not null
);

create table if not exists invoice_item (
	invoice_item_id int(11) not null auto_increment primary key,
    invoice_id int(11) not null,
    inventory_id int(11) not null,
    quantity int(11) not null,
    unit_price decimal(7,2) not null
);

alter table invoice_item add constraint fk_invoice_item_invoice foreign key (invoice_id) references invoice(invoice_id);