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
package org.ssps.spm.actions;

import java.io.FileNotFoundException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.ssps.common.archive.exceptions.SspsArchiveException;
import org.ssps.common.xml.exceptions.XmlDocumentException;
import org.ssps.spm.archive.Archiver;
import org.ssps.spm.dbm.DbmException;

/**
 * This action class is reponsible for creating new SSPS archives
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class ArchiveCreator extends ActionInterface {
	
	private static final Logger logger = Logger.getLogger(ArchiveCreator.class);
	
	private CommandLine cmdLine;
	private Options options;
	
	/**
	 * Constructor
	 * @param args command line arguments
	 */
	public ArchiveCreator(String[] args) {
		processCommand(args);
	}

	

	/* (non-Javadoc)
	 * @see org.ssps.spm.actions.ActionInterface#processCommand(java.lang.String[])
	 */
	@Override
	protected void processCommand(String[] args) {
		CommandLineParser parser = new PosixParser();

		options = new Options();

		options.addOption("h", "help", false, "prints the help");

		options.addOption("f", "file", true, "path to the DBM file");

		try { 
			cmdLine = parser.parse(options, args);
		}
		catch (ParseException e) {
			help(options, -1);
		}
	}
	
	private void archive() throws XmlDocumentException, DbmException, SspsArchiveException {
		String dbmFile = cmdLine.getOptionValue('d');
		
		
		if (dbmFile == null) {
			logger.trace("Using default dbm.xml file");
			
			dbmFile = "./dbm.xml";
		}
	
		Archiver archiver;
		
		try {
			archiver = new Archiver(dbmFile);
			archiver.createArchive();
		}
		catch (FileNotFoundException e) {
			System.err.println("You must inform the path to the DBM file or " + 
					"put a dbm.xml file on the current directory");
			help(options, -1);
		}
		
		
	}

	/* (non-Javadoc)
	 * @see org.ssps.spm.actions.ActionInterface#run()
	 */
	@Override
	public void run() {
		logger.trace("Running action");
		
		try {
			if (cmdLine.hasOption('h')) { 
				help(options, 1);
			}
			else { 
				archive();
				logger.info("Deliverable file created successfully");
			}
		} catch (XmlDocumentException e) {
			System.err.println("Invalid XML document: " + e.getMessage());
		} catch (DbmException e) {
			System.err.println("Invalid DBM document: " + e.getMessage());
		} catch (SspsArchiveException e) {
			System.err.println("Archive error: " + e.getMessage());
		}

	}

}
