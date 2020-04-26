create table mob_generators (
	id serial primary key,
	x float not null,
	y float not null,
	r float not null,

	mob integer not null
);
