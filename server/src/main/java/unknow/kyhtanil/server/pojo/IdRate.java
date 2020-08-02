/**
 * 
 */
package unknow.kyhtanil.server.pojo;

/**
 * a id with some %
 * 
 * @author unknow
 */
public class IdRate {
	/** the id */
	public int id;
	/** the % chance to choose */
	public float rate;

	/**
	 * choose one id at random
	 * 
	 * @param rates all the id rate (the rate sum should == 1)
	 * @return the chosen id
	 */
	public static int gen(IdRate[] rates) {
		double random = Math.random();
		int i = 0;
		IdRate r = rates[0];
		int len = rates.length;
		while (random > r.rate && i < len) {
			random -= r.rate;
			r = rates[++i];
		}
		return r.id;
	}
}
