package cz.zikesjan.integral;

import java.text.ParseException;

public interface Calculator {

	/**
	 * Prida novou funkci do Calculatoru
	 * 
	 * @param name
	 *            identifikator funkce
	 * @param expression
	 *            vyraz reprezentujici dannou funkci
	 * @param variables
	 *            promenne, ktere danna fce pouziva
	 */
	public void addFunction(String name, String expression, char... variables)
			throws IllegalArgumentException;
	
	/**
	 * Nastavi globalni konstantu
	 * 
	 * @param constaName
	 *            nazev konstanty
	 * @param value
	 *            hodnota konstanty
	 */
	public void setConstant(char constaName, double value)
			throws IllegalArgumentException;
	
	/**
	 * Numericky aproximuje integral zadaneho vyrazu
	 * 
	 * @param expression
	 *            vyraz
	 * @param variable
	 *            integrace podle promenne
	 * @param min
	 *            spodni hranice integralu
	 * @param max
	 *            horni hranice integralu
	 * @param delta
	 * @param rl
	 *            Listener, cekajici na vysledek
	 * @throws ParseException
	 * @throws IllegalArgumentException
	 */
	public void integrate(String expression, char variable, double min,
			double max, double delta, ResultListener rl) throws ParseException,
			IllegalArgumentException;

	/**
	 * Vraci definovane funkce
	 * 
	 * @return nazvy funkci
	 */
	public String[] getSupportedFunctions();

}
