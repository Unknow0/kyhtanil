package unknow.kyhtanil.server.component;

import com.artemis.Component;
import com.artemis.annotations.PooledWeaver;
import com.badlogic.gdx.utils.IntMap;

/**
 * list all contribution
 * 
 * @author unknow
 */
@PooledWeaver
public class Contribution extends Component {
	/** source -&gt; data */
	public IntMap<D> contributions = new IntMap<>();

	/**
	 * a contribution
	 * 
	 * @author unknow
	 */
	public static class D {
		/** value of contribution */
		public int contribution;
		/** remaining duration for this contribution */
		public float ttl;

		/**
		 * create new D
		 */
		public D() {
		}

		/**
		 * create new D
		 * 
		 * @param ttl
		 */
		public D(float ttl) {
			this.ttl = ttl;
		}
	}

	/**
	 * add a contribution
	 * 
	 * @param source the source source
	 * @param i      the contribution to add
	 * @param ttl    the new ttl
	 */
	public void add(int source, int i, float ttl) {
		D d = contributions.get(source);
		if (d == null)
			contributions.put(source, d = new D(ttl));
		d.contribution += i;
		if (d.ttl < ttl)
			d.ttl = ttl;
	}
}
