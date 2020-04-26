create table spawners (
	id serial primary key,
	x float not null,
	y float not null,
	r float not null,

	mobs integer[] not null
);
