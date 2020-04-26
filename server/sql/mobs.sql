create table mobs (
	id serial primary key,
	name varchar(50),

	strength integer,
	constitution integer,
	intelligence integer,
	concentration integer,
	dexterity integer
);
grant SELECT,UPDATE,INSERT on table mobs to kyhtanil;
