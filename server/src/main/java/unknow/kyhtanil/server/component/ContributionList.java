package unknow.kyhtanil.server.component;

/**
 * list all contribution
 * 
 * @author unknow
 */
public class ContributionList extends CompositeComponent<ContributionList.Constribution> {
	/**
	 * a contribution to a mob damage
	 * 
	 * @author unknow
	 */
	public static class Constribution {
		/** entity source */
		public int source;
		/** value of contribution */
		public int contribution;
		/** remaining duration for this contribution */
		public int ttl;
	}
}
