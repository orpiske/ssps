package org.ssps.common.discovery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.ssps.common.discovery.exceptions.InvalidBeanType;

public class DiscoveryTest {

    @Test
    public void testGetStringCandidateType() {
	SampleBean sampleBean = new SampleBean();
	
	try {
	    Class<?> clazz = Discovery.getFirstParameterType(sampleBean, "setFirst");
	    
	    assertEquals(String.class, clazz);
	} catch (InvalidBeanType e) {
	    e.printStackTrace();
	}
    }
    
    @Test
    public void testGetNullParameterType() {
	SampleBean sampleBean = new SampleBean();
	
	try {
	    Class<?> clazz = Discovery.getFirstParameterType(sampleBean, "getFirst");
	    
	    assertEquals(null, clazz);
	} catch (InvalidBeanType e) {
	    e.printStackTrace();
	}
    }
    
    @Test
    public void testGetIntegerParameterType() {
	SampleBean sampleBean = new SampleBean();
	
	try {
	    Class<?> clazz = Discovery.getFirstParameterType(sampleBean, "setSecond");
	    
	    assertEquals(Integer.class, clazz);
	} catch (InvalidBeanType e) {
	    e.printStackTrace();
	}
    }
    
    
    @Test
    public void testGetStringArrayParameterType() {
	SampleBean sampleBean = new SampleBean();
	
	try {
	    Class<?> clazz = Discovery.getFirstParameterType(sampleBean, "setSixth");
	    
	    assertEquals(String[].class, clazz);
	} catch (InvalidBeanType e) {
	    e.printStackTrace();
	}
    }
    
    @Test(expected = InvalidBeanType.class)
    public void testGetInvalidParameterType() throws InvalidBeanType {
	Class<?> clazz = Discovery.getFirstParameterType(null, "setFirst");
    }
    
    
    @Test
    public void testReadMethodName() throws InvalidBeanType {
	SampleBean sampleBean = new SampleBean();
	
	String name = Discovery.getReadMethodName(sampleBean, "first");
	
	assertEquals("getFirst", name);
    }
    
    @Test
    public void testReadMethodNameCamelCase() throws InvalidBeanType {
	SampleBean sampleBean = new SampleBean();
	
	String name = Discovery.getReadMethodName(sampleBean, "camelCase");
	
	assertEquals("getCamelCase", name);
    }
    
    @Test
    public void testReadMethodNameCamelCaseLower() throws InvalidBeanType {
	SampleBean sampleBean = new SampleBean();
	
	/*
	 * Checks if camelcase != camelCase
	 */
	String name = Discovery.getReadMethodName(sampleBean, "camelcase");
	
	assertNull(name);
    }
    
    @Test
    public void testReadMethodNameWithUnderscore() throws InvalidBeanType {
	SampleBean sampleBean = new SampleBean();
	
	String name = Discovery.getReadMethodName(sampleBean, "with_underscore");
	
	assertEquals("getWithUnderscore", name);
    }

}
