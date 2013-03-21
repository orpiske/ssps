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
package net.orpiske.ssps.common.repository.utils;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import net.orpiske.ssps.common.repository.PackageInfo;
import net.orpiske.ssps.common.repository.exception.PackageInfoException;

import org.apache.log4j.Logger;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.runtime.metaclass.MissingPropertyExceptionNoStack;

/**
 * The Groovy script engine
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class PackageDataUtils {
	
	private static Logger logger = Logger.getLogger(PackageDataUtils.class);
	
	private PackageDataUtils() {
		
	}
	
	private static GroovyObject getObject(final File file) throws PackageInfoException {
		ClassLoader parent = PackageDataUtils.class.getClassLoader();
		GroovyClassLoader loader = new GroovyClassLoader(parent);
		
		Class<?> groovyClass;
		try {
			groovyClass = loader.parseClass(file);
		} catch (CompilationFailedException e) {
			throw new PackageInfoException("The script has errors: " + e.getMessage(),
					e);
		} catch (IOException e) {
			throw new PackageInfoException("Input/output error: " + e.getMessage(),
					e);
		}
		finally {
			try {
				loader.close();
			} catch (IOException e) {
				
			}
		}

		GroovyObject groovyObject;
		try {
			groovyObject = (GroovyObject) groovyClass.newInstance();
		} catch (InstantiationException e) {
			throw new PackageInfoException("Unable to instantiate object: " 
					+ e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new PackageInfoException("Illegal access: " + e.getMessage(),
					e);
		}
		
		
		return groovyObject;
	}
		

	/*
	 * (non-Javadoc)
	 * @see net.orpiske.sdm.engine.Engine#run(java.io.File)
	 */
	public static void read(final File file, final PackageInfo packageInfo) throws PackageInfoException {
		GroovyObject groovyObject = getObject(file);
	
		String url = groovyObject.getProperty("url").toString();
		
		try { 
			Object o = groovyObject.getProperty("dependencies");
						
			if (o instanceof LinkedHashMap) { 
				@SuppressWarnings("unchecked")
				LinkedHashMap<String, String> dependencies =
					(LinkedHashMap<String, String>) o;
				
				packageInfo.setDependencies(dependencies);
			}
		}
		catch (MissingPropertyExceptionNoStack e) {
			logger.debug("Property " + e.getProperty() + " undefined for " + file.getName());
		}

		packageInfo.setUrl(url);
	}	
}
