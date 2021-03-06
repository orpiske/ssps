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
package net.orpiske.ssps.sdm.main;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Properties;

import groovy.lang.GroovyClassLoader;
import net.orpiske.ssps.common.configuration.ConfigurationWrapper;
import net.orpiske.ssps.common.db.derby.DerbyDatabaseManager;
import net.orpiske.ssps.common.db.version.DbVersionDao;
import net.orpiske.ssps.common.db.version.DbVersionDto;
import net.orpiske.ssps.common.exceptions.SspsException;
import net.orpiske.ssps.common.groovy.GroovyClasspathHelper;
import net.orpiske.ssps.common.logger.LoggerUtils;
import net.orpiske.ssps.common.registry.SoftwareInventoryDao;
import net.orpiske.ssps.common.repository.RepositorySettings;
import net.orpiske.ssps.common.repository.search.cache.PackageCacheDao;
import net.orpiske.ssps.common.utils.Utils;
import net.orpiske.ssps.sdm.actions.AddRepository;
import net.orpiske.ssps.sdm.actions.Installer;
import net.orpiske.ssps.sdm.actions.Search;
import net.orpiske.ssps.sdm.actions.Uninstall;
import net.orpiske.ssps.sdm.actions.Update;
import net.orpiske.ssps.sdm.actions.Upgrade;
import net.orpiske.ssps.sdm.utils.Constants;
import net.orpiske.ssps.sdm.utils.net.ProxyHelper;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.log4j.Logger;

/**
 * Main class
 *
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class Main {
	private static final Logger logger = Logger.getLogger(Main.class);

	private static void initLogger() throws FileNotFoundException {
		LoggerUtils.initLogger(Constants.SDM_CONFIG_DIR);
	}

	private static void help(int code) {
		System.out.println("Usage: sdm <action>\n");

		System.out.println("Actions:");
		System.out.println("   add-repository");
		System.out.println("   install");
		System.out.println("   uninstall");
		System.out.println("   update");
		System.out.println("   upgrade");
		System.out.println("   search");
		System.out.println("----------");
		System.out.println("   help");
		System.out.println("   --version");

		System.exit(code);
	}

	/**
	 * Initializes the configuration object
	 */
	private static void initConfig() {
		try {
			ConfigurationWrapper.initConfiguration(Constants.SDM_CONFIG_DIR,
					Constants.CONFIG_FILE_NAME);
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(-3);
		} catch (ConfigurationException e) {
			System.err.println(e.getMessage());
			System.exit(-3);
		}

		try {
			RepositorySettings.initConfiguration();
		} catch (SspsException e) {
			e.printStackTrace();

			System.err.println(e.getMessage());
			System.exit(-3);
		}
	}

	private static void initDatabase() {
		DbInitializationHelper dbInitializationHelper = new DbInitializationHelper();
		
		dbInitializationHelper.initDatabase();
	}	

	private static void initUserSdmDirectory() {
		File userDirectory = Utils.getSdmDirectoryPathFile();

		if (!userDirectory.exists()) {
			if (!userDirectory.mkdirs()) {
				System.err.println("Unable to create user directory");
				System.exit(-6);
			}
		}
	}

	private static void initProxy() {
		ProxyHelper.setup();
	}

	private static void initGroovyClasspath() {
		GroovyClasspathHelper helper = GroovyClasspathHelper.getInstance();

		File userHome = new File(Utils.getSdmDirectoryPath() + File.separator + "extra" +
				File.separator + "plugins");
		PluginRepositoryHelper.addToGroovyClasspath(userHome);

		String sdmHomePath = System.getProperty(Constants.HOME_PROPERTY);
		File sdmHome = new File(sdmHomePath + File.separator + "extra" +
				File.separator + "plugins");
		PluginRepositoryHelper.addToGroovyClasspath(sdmHome);
	}

	public static void main(String[] args) {
		int ret = 0;
		
		if (args.length == 0) {
			help(1);
		}

		String first = args[0];
		String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

		try {
			initLogger();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		logger.debug("Initializing configuration");
		initConfig();

		if (first.equals("help")) {
			help(1);
		}

		logger.debug("Initializing user directory");
		initUserSdmDirectory();

		logger.debug("Initializing database");
		initDatabase();

		logger.debug("Initializing proxy settings");
		initProxy();

		logger.debug("Initializing groovy classpath");
		initGroovyClasspath();

		if (first.equals("install")) {
			Installer installer = new Installer(newArgs);

			ret = installer.run();
			System.exit(ret);
		}

		if (first.equals("add-repository")) {
			AddRepository addRepository = new AddRepository(newArgs);

			ret =  addRepository.run();
			System.exit(ret);
		}

		if (first.equals("uninstall")) {
			Uninstall uninstall = new Uninstall(newArgs);

			ret =  uninstall.run();
			System.exit(ret);
		}

		if (first.equals("update")) {
			Update update = new Update(newArgs);

			ret =  update.run();
			System.exit(ret);
		}

		if (first.equals("upgrade")) {
			Upgrade upgrade = new Upgrade(newArgs);

			ret = upgrade.run();
			System.exit(ret);
		}

		if (first.equals("search")) {
			Search search = new Search(newArgs);

			ret = search.run();
			System.exit(ret);
			
		}

		if (first.equals("--version")) {
			System.out.println("Simple Software Provisioning System: sdm "
					+ Constants.VERSION);

			System.exit(ret);
		}

		help(1);
	}

	

}
