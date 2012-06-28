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

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.ssps.common.xml.exceptions.XmlDocumentException;
import org.ssps.spm.dbm.DbmException;
import org.ssps.spm.main.PublicationManager;

/**
 * This action is responsible for repository management
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class RepositoryManager extends ActionInterface {
	
	private CommandLine cmdLine;
	private Options options;
	
	/**
	 * Constructor
	 * @param args command-line arguments
	 */
	public RepositoryManager(final String[] args) {
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

	private void delete() throws XmlDocumentException, IOException, DbmException {
		String dbmFile = cmdLine.getOptionValue('f');

		if (dbmFile == null) {
			dbmFile = "./dbm.xml";
		}
		
		PublicationManager manager = new PublicationManager(dbmFile);

		manager.delete();
	}

	/* (non-Javadoc)
	 * @see org.ssps.spm.actions.ActionInterface#run()
	 */
	@Override
	public void run() {
		try {
			if (cmdLine.hasOption('h')) { 
				help(options, 1);
			}
			else { 
				delete();
			}
		} catch (XmlDocumentException e) {
			System.err.println("Invalid XML document: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Invalid DBM document: " + e.getMessage());
		} catch (DbmException e) {
			System.err.println("Invalid DBM document: " + e.getMessage());
		}
	}
}
