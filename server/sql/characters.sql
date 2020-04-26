create table characters (
	id serial not null primary key,
	account integer,
	name varchar(50),
	hp integer,
	mp integer,

	body integer
	);
grant SELECT,UPDATE,INSERT on table characters to kyhtanil;
