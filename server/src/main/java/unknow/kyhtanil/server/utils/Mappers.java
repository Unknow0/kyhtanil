package unknow.kyhtanil.server.utils;

import unknow.kyhtanil.server.component.*;

import com.artemis.*;

public class Mappers
	{
	private final ComponentMapper<StateComp> state;
	private final ComponentMapper<PositionComp> position;
	private final ComponentMapper<VelocityComp> velocity;
	private final ComponentMapper<MobInfoComp> mobInfo;
	private final ComponentMapper<SpawnerComp> spawner;
	private final ComponentMapper<DamageListComp> damage;
	private final ComponentMapper<CalculatedComp> calculated;

	public Mappers(World world)
		{
		state=ComponentMapper.getFor(StateComp.class, world);
		position=ComponentMapper.getFor(PositionComp.class, world);
		velocity=ComponentMapper.getFor(VelocityComp.class, world);
		mobInfo=ComponentMapper.getFor(MobInfoComp.class, world);
		spawner=ComponentMapper.getFor(SpawnerComp.class, world);
		damage=ComponentMapper.getFor(DamageListComp.class, world);
		calculated=ComponentMapper.getFor(CalculatedComp.class, world);
		}

	public ComponentMapper<?> state()
		{
		return state;
		}

	public StateComp state(int e)
		{
		return state.get(e);
		}

	public PositionComp position(int e)
		{
		return position.get(e);
		}

	public VelocityComp velocity(int e)
		{
		return velocity.get(e);
		}

	public MobInfoComp mobInfo(int e)
		{
		return mobInfo.get(e);
		}

	public SpawnerComp spawner(int e)
		{
		return spawner.get(e);
		}

	public DamageListComp damage(int e)
		{
		return damage.get(e);
		}

	public CalculatedComp calculated(int e)
		{
		return calculated.get(e);
		}
	}
