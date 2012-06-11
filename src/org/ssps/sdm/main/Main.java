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
package org.ssps.sdm.main;

import java.io.FileNotFoundException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration.ConfigurationException;
import org.ssps.common.configuration.ConfigurationWrapper;
import org.ssps.common.logger.LoggerUtils;
import org.ssps.sdm.actions.Fetcher;
import org.ssps.sdm.utils.Constants;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class Main {
    
    private static Options options;

    public static void initLogger() throws FileNotFoundException {
	LoggerUtils.initLogger(Constants.SDM_CONFIG_DIR);
    }

    
    public static void help(int code) {
	HelpFormatter formatter = new HelpFormatter();

	formatter.printHelp("sdm", options);
	System.exit(code);
    }
    
    /**
     * Initializes the configuration object
     */
    private static void initConfig() {
	try {
	    ConfigurationWrapper.initConfiguration(
		    Constants.SDM_CONFIG_DIR, Constants.CONFIG_FILE_NAME);
	} catch (FileNotFoundException e) {
	    System.err.println(e.getMessage());
	    System.exit(-3);
	} catch (ConfigurationException e) {
	    System.err.println(e.getMessage());
	    System.exit(-3);
	}
    }
    
    public static CommandLine processCommand(String[] args)
	    throws ParseException {
	// create the command line parser
	CommandLineParser parser = new PosixParser();

	// create the Options
	options = new Options();

	options.addOption("h", "help", false, "prints the help");
	options.addOption(null, "deploy", false, "deploys a deliverable");
	options.addOption(null, "fetch", false, 
		"fetchs a deliverable but does not install it");
	options.addOption(null, "install", false, 
		"install/deploys a previously fetched deliverable");
	
	
	options.addOption("g", "group", true, "deliverable group");
	options.addOption("n", "name", true, "deliverable name");
	options.addOption("v", "version", true, "deliverable version");
	
	options.addOption("r", "repository", true, "repository base address");
	
	
	/*
	options.addOption("D", "destination", true,
		"destination folder for the deliverable");
		
	
	*/
	
	return parser.parse(options, args);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	try { 
	  CommandLine cmdLine = processCommand(args);

	    initLogger();
	    initConfig();

	    if (cmdLine.hasOption('h')) {
		help(1);
	    }
	    
	    
	    if (cmdLine.hasOption("fetch")) {
		Fetcher fetcher = new Fetcher(null, null, null);
		
		String group = cmdLine.getOptionValue('g');
		String name = cmdLine.getOptionValue('n');
		String version = cmdLine.getOptionValue('v');
		String destination = cmdLine.getOptionValue('d');
		
		fetcher.fetch(group, name, version, destination);
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();
	    System.exit(-1);
	}
    }

}
