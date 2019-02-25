new Skill(1,"fireball") {
	desc : function(self) {
		return this.name;
	},
	exec : function(self, point, target) {
		var p = api.position(self);
		var r = Math.atan2(point.y - p.y, point.x - p.x)

		var d = new Damage(0, 0, 0, 0, 0, 0, 5, 10, 0, 0, 0, 0);
		api.addProj(self, r, 5, function(target) {
			var p = api.position(target);

			for each (var t in api.getMobs(p, 10)){
				if(t!=self)
					api.addDamage(self, d, 0, t);
			}
		})
	}
}
