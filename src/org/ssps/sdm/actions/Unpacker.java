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
package org.ssps.sdm.actions;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.ssps.common.archive.exceptions.SspsArchiveException;
import org.ssps.common.archive.usa.UsaArchive;
import org.ssps.common.configuration.ConfigurationWrapper;

/**
 * Unpacker
 * @author Otavio R. Piske <angusyoung@gmail.com>
 * 
 */
public class Unpacker extends ActionInterface {
	private static final PropertiesConfiguration config = ConfigurationWrapper
			.getConfig();

	private UsaArchive archive = new UsaArchive();
	
	private CommandLine cmdLine;
	private Options options;
	
	public Unpacker(final String[] args) {
		processCommand(args);
	} 


	/* (non-Javadoc)
	 * @see org.ssps.sdm.actions.ActionInterface#help(org.apache.commons.cli.Options, int)
	 */
	@Override
	protected void help(int code) {
		HelpFormatter formatter = new HelpFormatter();

		formatter.printHelp("sdm", options);
		System.exit(code);
		
	}

	/* (non-Javadoc)
	 * @see org.ssps.sdm.actions.ActionInterface#processCommand(java.lang.String[])
	 */
	@Override
	protected void processCommand(String[] args) {
		CommandLineParser parser = new PosixParser();
		
		options = new Options();
		
		options.addOption("h", "help", false, "prints the help");
		options.addOption("f", "file", true, "work file");
		
		try { 
			cmdLine = parser.parse(options, args);
		}
		catch (ParseException e) {
			help(-1);
		}
	}
	
	private void unpack() throws SspsArchiveException {
		String source = cmdLine.getOptionValue('f');
		
		if (source == null) {
			System.err.println("Missing file information");
			help(-1);
		}
		
		final String destination = config.getString("temp.work.dir",
				FileUtils.getTempDirectoryPath() + File.separator + "work");
		archive.unpack(source, destination);
	}

	/* (non-Javadoc)
	 * @see org.ssps.sdm.actions.ActionInterface#run()
	 */
	@Override
	public void run() {
		try {
			if (cmdLine.hasOption('h')) { 
				help(1);
			}
			else {
				unpack();
			}
		} catch (SspsArchiveException e) {
			Logger logger = Logger.getLogger(Unpacker.class);
			
			System.err.println("Unable to fetch resource: " + e.getMessage());
			
			if (logger.isDebugEnabled()) { 
				logger.error("Unable to fetch resource: " + e.getMessage(), e);
			}
		}
		
	}
}
