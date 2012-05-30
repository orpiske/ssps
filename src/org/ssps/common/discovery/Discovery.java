/**
  Copyright 2012 Otavio Rodolfo Piske

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package org.ssps.common.discovery;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.ssps.common.discovery.exceptions.InvalidBeanType;

/**
 * This class is used to introspect into a objects and find some method and 
 * type information about them
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class Discovery {
    private static final Logger logger = Logger.getLogger(Discovery.class);

    /**
     * Gets the type of the first parameter of the method 'name' from 'object'
     * 
     * @param object
     *            The object to discover
     * @param name
     *            The name of the method that contains the parameter
     * @return A Class object or null if not found
     * @throws InvalidBeanType
     *             If the input object is null;
     */
    public static Class<?> getFirstParameterType(final Object object,
	    final String name) throws InvalidBeanType {
	if (object == null) {
	    throw new InvalidBeanType("The input object is null");
	}

	Method[] methods = object.getClass().getMethods();

	if (methods == null || methods.length == 0) {
	    logger.warn("No method named " + name + " was found");

	    return null;
	}

	for (int i = 0; i < methods.length; i++) {
	    Method method = methods[i];

	    if (name.equals(method.getName())) {
		Class<?>[] clazz = method.getParameterTypes();

		if (clazz != null && clazz.length > 0) {
		    return clazz[0];
		}
	    }

	}

	logger.warn("No method named " + name + ", with the appropriate "
		+ "parameter types, was found");
	return null;
    }

    /**
     * Removes the underscore from a property name
     * @param name The property name
     * @return The property name without underscore
     */
    private static String getNameWithoutUnderscore(String name) {
	int pos = name.indexOf('_');

	if (pos > -1) {
	    StringBuilder strBuilder = new StringBuilder();

	    strBuilder.append(name.substring(0, pos));

	    if (pos < name.length()) {
		strBuilder.append(name.substring(pos + 1, pos + 2)
			.toUpperCase());

		if (pos + 1 < name.length()) {
		    strBuilder.append(name.substring(pos + 2, name.length()));
		}
	    }

	    String newName = strBuilder.toString();

	    if (newName.indexOf('_') > -1) {
		return getNameWithoutUnderscore(newName);
	    }
	    
	    return newName;
	}

	return name;
    }

    /**
     * Gets the read method name for a given property
     * @param object The object to lookup the read method
     * @param propertyName The name of the property
     * @return The read method name or null if not existent
     * @throws InvalidBeanType If the 
     */
    public static String getReadMethodName(final Object object,
	    final String propertyName) throws InvalidBeanType {
	PropertyDescriptor p;

	try {
	    p = PropertyUtils.getPropertyDescriptor(object, propertyName);

	    if (p != null) {
		Method method = p.getReadMethod();

		return method.getName();
	    }

	    /*
	     * It may be possible that the property name has an underscore so
	     * we perform a small hack to remove it and search again 
	     */
	    int pos = propertyName.indexOf('_');

	    if (pos > -1) {

		String newPropertyName = getNameWithoutUnderscore(propertyName);

		return getReadMethodName(object, newPropertyName);
	    }
	} catch (IllegalAccessException e) {
	    throw new InvalidBeanType("Unauthorized to access the method", e);
	} catch (InvocationTargetException e) {
	    throw new InvalidBeanType("Unable to access method", e);
	} catch (NoSuchMethodException e) {
	    throw new InvalidBeanType("The method does not exist", e);
	}

	return null;
    }
    
    
    /**
     * Gets the write method name for a given property
     * @param object The object to lookup the write method
     * @param propertyName The name of the property
     * @return The read method name or null if not existent
     * @throws InvalidBeanType If the 
     */
    public static String getWriteMethodName(final Object object,
	    final String propertyName) throws InvalidBeanType {
	PropertyDescriptor p;

	try {
	    p = PropertyUtils.getPropertyDescriptor(object, propertyName);

	    if (p != null) {
		Method method = p.getWriteMethod();

		return method.getName();
	    }

	    /*
	     * It may be possible that the property name has an underscore so
	     * we perform a small hack to remove it and search again 
	     */
	    int pos = propertyName.indexOf('_');

	    if (pos > -1) {

		String newPropertyName = getNameWithoutUnderscore(propertyName);

		return getWriteMethodName(object, newPropertyName);
	    }
	} catch (IllegalAccessException e) {
	    throw new InvalidBeanType("Unauthorized to access the method", e);
	} catch (InvocationTargetException e) {
	    throw new InvalidBeanType("Unable to access method", e);
	} catch (NoSuchMethodException e) {
	    throw new InvalidBeanType("The method does not exist", e);
	}

	return null;
    }

}
