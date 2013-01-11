package cz.zikesjan.integral;

import java.text.ParseException;

/*
 * just the main class for trying out if it is really working
 */
public class Main {

    public static void main(String[] args) throws ParseException {
                long startTime = System.currentTimeMillis();

                Calculator calc = new CalculatorImpl(1);
                Calculator calc2 = new CalculatorImpl(2);
                Calculator calc3 = new CalculatorImpl(2);
                Calculator calc4 = new CalculatorImpl(2);
                Calculator calc5 = new CalculatorImpl(2);

                //calc.setConstant('p', Math.PI);
                //calc.setConstant('e', Math.E);
                //calc.addFunction("add", "a - b", 'a', 'b');
                //calc.addFunction("trips", "add( a , c )", 'a', 'b', 'c');

                //calc.integrate("8 + ( 3 - trips( 2 * add( add( add( x , x ) , x / 4 ) , add( x , add( x , x ) ) ) , x , add( add( x , x ) , add( x , x ) - 6 ) * 4 ) ^ 5 )", 'x', 0, 1, 0.00001, new MyListener(startTime));


		calc3.setConstant('P', Math.PI);
		calc3.setConstant('E', Math.E);
		calc3.addFunction("ADD", "a + b", 'a', 'b');
		calc3.addFunction("SUB", "a - b", 'a', 'b');
		calc3.addFunction("MUL", "a * b", 'a', 'b');
		calc3.addFunction("DIV", "a / b", 'a', 'b');
		calc3.addFunction("POWER", "a ^ b", 'a', 'b');
		calc3.addFunction("ROOT", "POWER( a , ( 1 / b ) )", 'a', 'b');
		calc3.addFunction("SQUARE", "MUL( a , a )", 'a');
		calc3.addFunction("SQRT", "ROOT( a , 2 )", 'a');
		calc3.addFunction("EXP", "POWER( E , a )", 'a');
		calc3.addFunction("TRIPS", "ADD( ADD( a , b ) , c )", 'a', 'b', 'c');
                calc3.setConstant('e', Math.E);
                calc3.addFunction("exp", "e ^ x", 'x');
                calc4.setConstant('b', 2);
                calc4.addFunction("inc", "a + 1", 'a');

		
               
		try
		{
                        

                        calc4.integrate("( 1 + x ^ 3 * x ^ 2 + inc( x ) - b ) / ( x ^ 3 ^ 2 )", 'x', 1d, 2d, 0.001 , new MyListener(startTime));
			calc3.integrate("8 / TRIPS( x , P , SQUARE( DIV( ADD( EXP( SQRT( x ) ) , P ) , E ) ) )", 'x', 0, 10, 0.00001, new MyListener(startTime) );
                        calc3.integrate("( 9 * 6 ) / TRIPS( x + ADD( 3 , 2 + 8 + 4 ) , -P * -7 / 6 + ( 8 + ( -6 ) ) , SQUARE( DIV( ADD( EXP( SQRT( x + x ) ) , P ) , E + 6 ) ) ) + ( 8 / 52 )", 'x', 0, 10, 0.00001, new MyListener(startTime) );
			calc3.integrate("x ^ 2 + E + x", 'x', 0, 10, 0.001, new MyListener(startTime) );
			calc3.integrate("SQRT( ADD( x + 1 , 2 * 5 ) )", 'x', 0, 10, 0.001, new MyListener(startTime) );
			calc3.integrate("SQRT( x )", 'x', 0, 10, 0.001, new MyListener(startTime) );
			calc3.integrate("SQUARE( SQRT( SQUARE( x ) ) )", 'x', 0, 10, 0.001, new MyListener(startTime) );
                        calc3.integrate("exp( x )", 'x', 0, 1, 0.001, new MyListener(startTime));
                        calc4.integrate("( 1 + x ^ 3 * x ^ 2 + inc( x ) - b ) / ( x ^ 3 ^ 2 )", 'x', 1d, 2d, 0.001 , new MyListener(startTime));
                        calc2.integrate("( ( ( 5 + 3 ) ) )", 'x', 1d, 2d, 0.001 , new MyListener(startTime));
                }

		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

                
    }
}
