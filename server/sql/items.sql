create table items (
	id serial not null primary key,
	name varchar(50) not null,
	"desc" varchar(255) not null
	);

create table items_stats (
	item int not null,
	stat varchar(20),
	rate double,
	min int,
	max int
	);
