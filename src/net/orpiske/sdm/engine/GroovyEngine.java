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
package net.orpiske.sdm.engine;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import net.orpiske.sdm.common.WorkdirUtils;
import net.orpiske.sdm.engine.exceptions.EngineException;
import net.orpiske.ssps.common.utils.URLUtils;

import org.codehaus.groovy.control.CompilationFailedException;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class GroovyEngine implements Engine {
	
	public GroovyEngine() {
		
	}
	
	public void run(final String path) throws EngineException {
		File file = new File(path);
		
		run(file);
	}
	
	public void run(final File file) throws EngineException {
		ClassLoader parent = getClass().getClassLoader();
		GroovyClassLoader loader = new GroovyClassLoader(parent);
		
		Class<?> groovyClass;
		try {
			groovyClass = loader.parseClass(file);
		} catch (CompilationFailedException e) {
			throw new EngineException("The script has errors: " + e.getMessage(),
					e);
		} catch (IOException e) {
			throw new EngineException("Input/output error: " + e.getMessage(),
					e);
		}

		GroovyObject groovyObject;
		try {
			groovyObject = (GroovyObject) groovyClass.newInstance();
		} catch (InstantiationException e) {
			throw new EngineException("Unable to instantiate object: " 
					+ e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new EngineException("Illegal access: " + e.getMessage(),
					e);
		}
		
		
		Object url = groovyObject.getProperty("url");
		groovyObject.invokeMethod("fetch", url);
		
		String artifactName = null;
		
		try {
			artifactName = URLUtils.getFilename(url.toString());
		} catch (MalformedURLException e) {
			throw new EngineException("The package URL is invalid: " + e.getMessage(),
					e);
		} catch (URISyntaxException e) {
			throw new EngineException("The URL syntax is invalid: " + e.getMessage(),
					e);
		}
		
		groovyObject.invokeMethod("extract", WorkdirUtils.getWorkDir() + File.separator + artifactName);
		groovyObject.invokeMethod("build", artifactName);
		groovyObject.invokeMethod("verify", artifactName);
		groovyObject.invokeMethod("install", artifactName);
	

	}
}
