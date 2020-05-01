create table accounts (
	id serial not null primary key,
	login varchar(50),
	pass_hash bytea,

	constraint login unique (login)
	);
