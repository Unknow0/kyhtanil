new Skill(0, 'default attack') {
	desc : function() {
		return this.name;
	},
	exec : function(self, point, target) {
		var p = api.position(self);
		var r = Math.atan2(point.y - p.y, point.x - p.x)
		target=undefined;
		// TODO weapon range
		
		var ts = api.getMobs(p, 3);
		if (ts) {
			for each (var t in ts) {
				if (t == self)
					continue;
				m = api.position(t);
				var mr = Math.atan2(m.y - p.y, m.x - p.x);

				var diff = r - mr;
				if (-.3 < diff && diff < .3) {
					target = t;
					break;
				}
			}
		}

		// TODO proj for ranged weapon
		if (target)
			api.addDamage(self, api.stat(self).dmg, 0, target);
	}
}
