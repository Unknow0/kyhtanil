create table mobs (
	id serial primary key,
	name varchar(50),
	tex varchar(50),
	w float,

	strength integer,
	constitution integer,
	intelligence integer,
	concentration integer,
	dexterity integer
);
