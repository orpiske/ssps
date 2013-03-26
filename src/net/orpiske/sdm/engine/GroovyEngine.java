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
import net.orpiske.ssps.common.repository.utils.InstallDirUtils;
import net.orpiske.ssps.common.utils.URLUtils;

import org.apache.log4j.Logger;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 * The Groovy script engine
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class GroovyEngine implements Engine {
	
	private static Logger logger = Logger.getLogger(GroovyEngine.class);
	
	public GroovyEngine() {
		
	}
	
	private GroovyObject getObject(final File file) throws EngineException {
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
		
		return groovyObject;
	}
	
	
	private void printPhaseHeader(final String name) {
		System.out.println("------------------------");
		System.out.println(name.toUpperCase());
		System.out.println("------------------------");
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see net.orpiske.sdm.engine.Engine#run(java.io.File)
	 */
	public void run(final File file) throws EngineException {
		long start;
		long finish;
		long total = 0;
		
		GroovyObject groovyObject = getObject(file);
		
		start = System.currentTimeMillis();
		
		Object url = groovyObject.getProperty("url");
		printPhaseHeader("fetch");
		groovyObject.invokeMethod("fetch", url);
		finish = System.currentTimeMillis(); 
		total += (finish - start);
		logger.info("Fetch phase run in " + (finish - start) + " ms");
		
		String artifactName = null;
		
		try {
			if (url != null && !url.toString().equals("")) { 
				artifactName = URLUtils.getFilename(url.toString());
				artifactName = WorkdirUtils.getWorkDir() + File.separator + artifactName;
			}
			
		} catch (MalformedURLException e) {
			throw new EngineException("The package URL is invalid: " + e.getMessage(),
					e);
		} catch (URISyntaxException e) {
			throw new EngineException("The URL syntax is invalid: " + e.getMessage(),
					e);
		}
		
		start = System.currentTimeMillis();
		
		System.out.println("");
		printPhaseHeader("extract");
		groovyObject.invokeMethod("extract", artifactName);
		
		finish = System.currentTimeMillis(); 
		total += (finish - start);
		logger.info("Extract phase run in " + (finish - start) + " ms");
		
		
		start = System.currentTimeMillis();
		
		System.out.println("");;
		printPhaseHeader("build");
		groovyObject.invokeMethod("build", null);
		
		finish = System.currentTimeMillis(); 
		total += (finish - start);
		logger.info("Build phase run in " + (finish - start) + " ms");
		
		start = System.currentTimeMillis();
		
		System.out.println("");
		printPhaseHeader("verify");
		groovyObject.invokeMethod("verify", null);
		
		finish = System.currentTimeMillis(); 
		total += (finish - start);
		logger.info("Verify phase run in " + (finish - start) + " ms");
		
		start = System.currentTimeMillis();
		
		System.out.println("");
		printPhaseHeader("install");
		groovyObject.invokeMethod("install", null);
		
		finish = System.currentTimeMillis(); 
		total += (finish - start);
		logger.info("Install phase run in " + (finish - start) + " ms");
		
		System.out.println("");
		printPhaseHeader("install completed");
		logger.info("Installation completed in " + total + " ms");
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.orpiske.sdm.engine.Engine#run(java.lang.String)
	 */
	public void run(final String path) throws EngineException {
		File file = new File(path);
		
		run(file);
	}
	
	
	/* (non-Javadoc)
	 * @see net.orpiske.sdm.engine.Engine#runCleanup(java.io.File)
	 */
	@Override
	public void runUninstall(File file) throws EngineException {
		long start;
		long finish;
		String installDir = InstallDirUtils.getInstallDir();
		
		GroovyObject groovyObject = getObject(file);
		
		start = System.currentTimeMillis();
		printPhaseHeader("uninstall");
		groovyObject.invokeMethod("uninstall", installDir);
		finish = System.currentTimeMillis(); 
		logger.info("Uninstall phase run in " + (finish - start) + " ms");
		printPhaseHeader("uninstall complete");
	}

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.engine.Engine#runCleanup(java.lang.String)
	 */
	@Override
	public void runUninstall(String path) throws EngineException {
		File file = new File(path);
		
		runUninstall(file);
		
	}

	
}
