package utils;

import junit.framework.TestCase;

import org.junit.Test;

public class TestSystemUser extends TestCase{
	
	@Test
	public void testSystemEnv() {

		System.out.println(System.getProperty("user.home"));
		
	}

}
