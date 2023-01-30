package com.spring.javawspring.errorTest;

import org.junit.Test;

public class CalculatorTest {

	@Test
	public void testAdd() {
		Calculator calc = new Calculator();
		int res = calc.add(10, 20);
		System.out.println("res : " + res);
	}

}
