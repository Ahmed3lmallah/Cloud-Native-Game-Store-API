create schema if not exists admin_api_security;

use admin_api_security;

create table if not exists users(
	username varchar(50) not null primary key,
	password varchar(100) not null,
	enabled boolean not null
);

create table if not exists authorities (
	username varchar(50) not null,
	authority varchar(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username));
	create unique index ix_auth_username on authorities (username,authority
);

/* Users */
insert into users (username, password, enabled) values 
	('employee'	,	'$2a$10$0OWkVSMefVw3YLge25C13upNk/1Fm2pqRcJYiFd7CmP91xm5mbL/K', true),
	('teamLead'	,	'$2a$10$sFxhZU9.NRpdE/n9iUi4me6lwwd6HunCVbarujfpEw36kQZiiiftC', true),
	('manager'	, 	'$2a$10$4.lio1NN744c9n4/pfTAA.nSDtAdUB1sFIH7YsN7ayGdpvf1slV/2', true),
	('admin'	,	'$2a$10$CmRBB60blw5NChQZOZKv4.oPfPlbP24Ui1aydJte.RELpdltT8VQK', true);

/* Authorities */
insert into authorities (username, authority) values 
	('employee'	, 	'ROLE_EMPLOYEE'),
	('teamLead'	, 	'ROLE_EMPLOYEE'),
	('teamLead'	,	'ROLE_TEAMLEAD'),
	('manager'	, 	'ROLE_EMPLOYEE'),
	('manager'	, 	'ROLE_TEAMLEAD'),
	('manager'	, 	'ROLE_MANAGER'),
	('admin'	, 	'ROLE_EMPLOYEE'),
	('admin'	, 	'ROLE_TEAMLEAD'),
	('admin'	, 	'ROLE_MANAGER'),
	('admin'	, 	'ROLE_ADMIN');