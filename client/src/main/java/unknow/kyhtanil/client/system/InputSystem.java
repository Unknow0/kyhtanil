package unknow.kyhtanil.client.system;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.AspectSubscriptionManager;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.EntityEdit;
import com.artemis.EntitySubscription;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import unknow.kyhtanil.client.component.TargetComp;
import unknow.kyhtanil.client.system.net.Connection;
import unknow.kyhtanil.common.Stats;
import unknow.kyhtanil.common.component.PositionComp;
import unknow.kyhtanil.common.component.SpriteComp;
import unknow.kyhtanil.common.component.StatAgg;
import unknow.kyhtanil.common.component.VelocityComp;
import unknow.kyhtanil.common.pojo.UUID;
import unknow.kyhtanil.common.util.BaseUUIDManager;

public class InputSystem extends BaseSystem implements InputProcessor {
	private static final Logger log = LoggerFactory.getLogger(InputSystem.class);
	private Viewport vp;

	/** key bind */
	public int up = Input.Keys.W;
	public int down = Input.Keys.S;
	public int left = Input.Keys.A;
	public int right = Input.Keys.D;

	public int showStat = Input.Keys.C;

	public int[] bar = new int[] { Input.Keys.NUM_1, Input.Keys.NUM_2, Input.Keys.NUM_3, Input.Keys.NUM_4, Input.Keys.NUM_5, Input.Keys.NUM_6, Input.Keys.NUM_7, Input.Keys.NUM_8, Input.Keys.NUM_9 };

	/** system */
	protected EntitySubscription allPosition;
	protected EntitySubscription target;

	private ComponentMapper<StatAgg> stats;
	private ComponentMapper<VelocityComp> velocity;
	private ComponentMapper<PositionComp> position;
	private ComponentMapper<SpriteComp> sprite;
	private BaseUUIDManager manager;
	private Connection connection;
	private State state;

	/** state */
	private long lastSend = 0;
	private float lastX;
	private float lastY;
	private double dirX = 0;
	private double dirY = 0;

	public InputSystem(Viewport vp) {
		this.vp = vp;
	}

	protected void initialize() {
		AspectSubscriptionManager sm = world.getAspectSubscriptionManager();
		allPosition = sm.get(Aspect.all(PositionComp.class));
		target = sm.get(Aspect.all(TargetComp.class));
	}

	public boolean keyDown(int keycode) {
		if (!checkProcessing())
			return false;
		VelocityComp v = velocity.get(state.entity);
		int moveSpeed = stats.get(state.entity).get(Stats.MOVE_SPEED);

		if (up == keycode || down == keycode || left == keycode || right == keycode) {// TODO take pj speed
			if (up == keycode)
				dirY = 1.;
			else if (down == keycode)
				dirY = -1.;
			else if (left == keycode)
				dirX = -1.;
			else if (right == keycode)
				dirX = 1;

			v.speed = moveSpeed;
			v.direction = (float) Math.atan2(dirY, dirX);
		}
		return true;
	}

	public boolean keyUp(int keycode) {
		if (!checkProcessing())
			return false;
		VelocityComp v = velocity.get(state.entity);
		if (up == keycode || down == keycode || left == keycode || right == keycode) {
			if (up == keycode || down == keycode)
				dirY = 0.;
			else if (left == keycode || right == keycode)
				dirX = 0.;
			if (dirY == 0. && dirX == 0.)
				v.speed = 0f;
			v.direction = (float) Math.atan2(dirY, dirX);
		}
		// else if(showStat==keycode)
		// screen.toggleStat();
		// else if(Input.Keys.ESCAPE==keycode)
		// screen.closeLast();
		else {
			for (int i = 0; i < bar.length; i++) {
				if (bar[i] == keycode) {
					try {
						Vector2 vec = vp.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
						IntBag targets = target.getEntities();
						UUID uuid = null;
						if (!targets.isEmpty()) {
							int t = targets.get(0);
							uuid = manager.getUuid(t);
							log.info("attaque {} {}", t, uuid);
						}
						connection.attack(state.uuid, i, uuid, vec.x, vec.y);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}
		return true;
	}

	public boolean keyTyped(char character) {
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (!checkProcessing())
			return false;
		Vector2 v = vp.unproject(new Vector2(screenX, screenY));
		IntBag targets = target.getEntities();
		for (int i = 0; i < targets.size(); i++) {
			int e = targets.get(i);
			EntityEdit edit = world.getEntity(e).edit();
			edit.remove(TargetComp.class);
		}

		IntBag entities = allPosition.getEntities();

		for (int i = 0; i < entities.size(); i++) {
			int e = entities.get(i);
			PositionComp p = position.get(e);
			SpriteComp s = sprite.get(e);
			if (p.distance(v.x, v.y) < s.w) {
				log.info("target {} ({}, {})", manager.getUuid(e), p.x, p.y);
				EntityEdit edit = world.getEntity(e).edit();
				edit.create(TargetComp.class);
				break;
			}
		}
		return true;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	protected boolean checkProcessing() {
		return state.entity >= 0 && sprite.has(state.entity);
	}

	@Override
	protected void processSystem() {
		SpriteComp s = sprite.get(state.entity);
		PositionComp p = position.get(state.entity);
		Vector2 d = vp.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
		IntBag targets = target.getEntities();
		if (!targets.isEmpty()) {
			PositionComp t = position.get(targets.get(0));
			d.set(t.x, t.y);
		}
		s.rotation = (float) Math.atan2(d.y - p.y, d.x - p.x);

		long now = System.currentTimeMillis();
		if (now - lastSend > 250 && (lastX != p.x || lastY != p.y)) {
			VelocityComp v = velocity.get(state.entity);
			try {
				connection.update(state.uuid, p.x, p.y, v.direction);
			} catch (IOException e) {
			}
			lastX = p.x;
			lastY = p.y;
			lastSend = now;
		}
	}
}
