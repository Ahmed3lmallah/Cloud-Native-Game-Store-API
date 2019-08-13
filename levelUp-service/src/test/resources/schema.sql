create schema if not exists game_api_test;
use game_api_test;

create table if not exists level_up (
	level_up_id int(11) not null auto_increment primary key,
    customer_id int(11) not null,
    points int(11) not null,
    member_date date not null
);