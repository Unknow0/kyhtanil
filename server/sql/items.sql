create type item_slots as enum ('MAIN_HAND', 'OFF_HAND', 'TWO_HAND', 'HELM', 'GLOVE', 'CHEST', 'BOOTS', 'LEGS, NONE');

create table items (
	id serial not null primary key,
	name varchar(50),
	"desc" varchar(255),
	slot item_slots
	);
