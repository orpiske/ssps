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
import net.orpiske.sdm.lib.rdc.RuntimeDataLoader;
import net.orpiske.ssps.common.groovy.GroovyClasspathHelper;
import net.orpiske.ssps.common.repository.utils.InstallDirUtils;
import net.orpiske.ssps.common.scm.ScmUrlUtils;
import net.orpiske.ssps.common.utils.URLUtils;

import org.apache.commons.lang.ArrayUtils;
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
		GroovyClasspathHelper classpathHelper = GroovyClasspathHelper.getInstance();
		GroovyClassLoader loader = classpathHelper.getLoader();

		// Loads shared runtime data
		RuntimeDataLoader rdl = new RuntimeDataLoader(file);
		rdl.load();

		// Parses the class
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

		// Instantiate the object
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

	/**
	 * @param groovyObject
	 */
	private long runPhase(GroovyObject groovyObject, String name, Object ... arguments) {
		long start;
		long finish;
		long elapsed;
		start = System.currentTimeMillis();

		System.out.println("");
		printPhaseHeader(name);

		groovyObject.invokeMethod(name, arguments);

		finish = System.currentTimeMillis();
		elapsed = finish - start;
		logger.info("Phase " + name + " run in " + elapsed + " ms");
		return elapsed;
	}


	/*
	 * (non-Javadoc)
	 * @see net.orpiske.sdm.engine.Engine#run(java.io.File)
	 */
	public void run(final File file, String... phases) throws EngineException {
		long total = 0;

		GroovyObject groovyObject = getObject(file);

		Object url = groovyObject.getProperty("url");

		if (phases.length == 0) {
			return;
		}
		
		
		if (ArrayUtils.contains(phases, "fetch")) { 
			total += runPhase(groovyObject, "fetch", url);
		}
		
		String artifactName = null;

		try {
			if (url != null && !url.toString().isEmpty()) {
				artifactName = URLUtils.getFilename(url.toString());
				artifactName = WorkdirUtils.getWorkDir() + File.separator + artifactName;
			}

		} catch (MalformedURLException e) {
			String urlString = url.toString();
			if (!ScmUrlUtils.isValid(urlString)) {
				throw new EngineException("The package URL is invalid: " + e.getMessage(),
						e);
			}

		} catch (URISyntaxException e) {
			throw new EngineException("The URL syntax is invalid: " + e.getMessage(),
					e);
		}


		if (ArrayUtils.contains(phases, "extract")) {
			total += runPhase(groovyObject, "extract", artifactName);
		}

		if (ArrayUtils.contains(phases, "build")) {
			total += runPhase(groovyObject, "build", (Object[]) null);
		}

		if (ArrayUtils.contains(phases, "prepare")) {
			total += runPhase(groovyObject, "prepare", (Object[]) null);
		}

		if (ArrayUtils.contains(phases, "verify")) {
			total += runPhase(groovyObject, "verify", (Object[]) null);
		}

		if (ArrayUtils.contains(phases, "prepare")) {
			total += runPhase(groovyObject, "prepare", (Object[]) null);
		}

		if (ArrayUtils.contains(phases, "install")) {
			total += runPhase(groovyObject, "install", (Object[]) null);
		}

		if (ArrayUtils.contains(phases, "finish")) {
			total += runPhase(groovyObject, "finish", (Object[]) null);
		}

		if (ArrayUtils.contains(phases, "cleanup")) {
			total += runPhase(groovyObject, "cleanup", (Object[]) null);
		}

		printPhaseHeader("install completed");
		logger.info("Installation completed in " + total + " ms");
	}


	/*
	 * (non-Javadoc)
	 * @see net.orpiske.sdm.engine.Engine#run(java.io.File)
	 */
	public void run(final File file) throws EngineException {
		run(file, "fetch", "extract", "build", "prepare", "verify", "install", 
				"finish", "cleanup");
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
