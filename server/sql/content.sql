
insert into mobs (name, tex, w, strength, constitution, intelligence, concentration, dexterity) values
	('blue slime', 'mobs/slime_blue', 8, 1, 1, 1, 1, 1),
	('red slime', 'mobs/slime_red', 10, 2, 1, 1, 1, 1)
	;

insert into spawners (x, y, r, max, speed) values
	(500, 500, 400, 20, 1000)
	;

insert into spawner_mobs (spawner, mob, factor) values
	(1, 1, 3),
	(1, 2, 1)
	;
