new Skill(1,"fireball") {
	desc : function(self) {
		return this.name;
	},
	exec : function(self, point, target) {
		var p = api.position(self);
		var r = Math.atan2(point.y - p.y, point.x - p.x)

		var d = new Damage(0, 0, 0, 0, 0, 0, 5, 10, 0, 0, 0, 0);
		api.addProj(self, r, 2, function(target) {
			var p = api.position(target);

			var t = api.getMob(p.x, p.y, 10);
			for (var i = 0; i < t.length; i++)
				api.addDamage(self, t.get(i), 0, d);
		})
	}
}