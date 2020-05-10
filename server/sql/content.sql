create function pg_temp.setserial(_tbl regclass) returns void as $func$
begin
	execute format('select setval(pg_get_serial_sequence(%L,''id''), (select max(id) from %1$I))', _tbl);
end
$func$ LANGUAGE plpgsql;

insert into items (id, name, "desc") values
	(1, 'slime remains', 'stricky slime remains')
	;
select pg_temp.setserial('items'::regclass);

insert into mobs (id, name, tex, w, strength, constitution, intelligence, concentration, dexterity) values
	(1, 'blue slime', 'mobs/slime_blue', 8, 1, 1, 1, 1, 1),
	(2, 'red slime', 'mobs/slime_red', 10, 2, 1, 1, 1, 1)
	;
select pg_temp.setserial('mobs');

insert into mobs_loot (mob, item, rate) values
	(1, 1, .5),
	(2, 1, .66)
	;

insert into spawners (x, y, r, max, speed) values
	(500, 500, 400, 20, 1000)
	;

insert into spawner_mobs (spawner, mob, factor) values
	(1, 1, 3),
	(1, 2, 1)
	;
