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

import net.orpiske.spm.common.adm.AdmDocument;
import net.orpiske.spm.publication.WebDavPublicationManager;
import net.orpiske.ssps.adm.Adm;
import net.orpiske.ssps.common.resource.exceptions.ResourceExchangeException;
import net.orpiske.ssps.common.xml.exceptions.XmlDocumentException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * This action class is responsible for publishing deliverables
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class Publisher extends ActionInterface {
	
	private CommandLine cmdLine;
	private Options options;
	
	private String file;
	
	private boolean overwrite;
	
	
	/**
	 * Constructor
	 * @param args command-line arguments
	 */
	public Publisher(final String[] args) {
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
		options.addOption("f", "file", true, "path to the ADM file");
		options.addOption("o", "overwrite", false, 
				"overwrite remote files (default = false)");

		try { 
			cmdLine = parser.parse(options, args);
		}
		catch (ParseException e) {
			help(options, -1);
		}
		
		file = cmdLine.getOptionValue('f');
		if (file == null) {
			file = "./adm.xml";
		}
		
		overwrite = cmdLine.hasOption('o');
	}
	
	private void publish() throws IOException, XmlDocumentException, ResourceExchangeException {
		AdmDocument admDocument = new AdmDocument(file);
		Adm adm = admDocument.getDocument();
		
		WebDavPublicationManager manager = new WebDavPublicationManager(adm);

		manager.upload(file, overwrite);
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
				publish();
			}
		} catch (XmlDocumentException e) {
			System.err.println("Invalid XML document: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Input/output error: " + e.getMessage());
			e.printStackTrace();
		} catch (ResourceExchangeException e) {
			System.err.println("Resource exchange error: " + e.getMessage());
		}
	}

}
