/**
   Copyright 2013 Otavio Rodolfo Piske

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
package net.orpiske.ssps.spm.actions;

import net.orpiske.ssps.spm.template.Template;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class Create extends AbstractAction {
	
	private CommandLine cmdLine;
	private Options options;
	
	private boolean isHelp;
	
	private Template template;
	

	@Override
	protected void processCommand(String[] args) {
		CommandLineParser parser = new PosixParser();

		options = new Options();

		options.addOption("h", "help", false, "prints the help");
		options.addOption("i", "installed", false, "searches for installed packages");
		options.addOption("t", "template", true, "template name");
		

		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			help(options, -1);
		}
		
		isHelp = cmdLine.hasOption("help");
		
		
		String name = cmdLine.getOptionValue('t');
		if (name == null) {
			help(options, -1);
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
