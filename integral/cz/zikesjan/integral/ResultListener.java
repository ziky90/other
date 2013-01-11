package cz.zikesjan.integral;

public interface ResultListener {

	/**
	 * Metoda zavolana Calculatorem pri dopocteni
	 * 
	 * @param result
	 *            vysledek vypoctu
	 */
	public void reportResult(double result);

}
