CREATE TABLE characters_body (
	id serial NOT NULL PRIMARY KEY,
	strength integer,
	constitution integer,
	intelligence integer,
	concentration integer,
	dexterity integer,
	points integer,
	xp integer,
	level integer
);
grant SELECT,UPDATE,INSERT on table characters_body to kyhtanil;
