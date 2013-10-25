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
package net.orpiske.spm.actions

import groovy.text.GStringTemplateEngine
import net.orpiske.net.orpiske.spm.writers.PackageFileWriter
import net.orpiske.net.orpiske.spm.writers.StdoutWriter
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException
import org.apache.commons.cli.PosixParser
import net.orpiske.net.orpiske.spm.writers.Writer;

class Eval extends AbstractAction {
	private CommandLine cmdLine;
	private Options options;
	
	private String templateFile;
	private String source;
	private String groupId;
	private String repository;
	
	private Writer writer;
		
	
	public Eval(String[] args) {
		processCommand(args);
	}

	protected void processCommand(String[] args) {
		CommandLineParser parser = new PosixParser();

		options = new Options();

		options.addOption("h", "help", false, "prints the help");
		options.addOption("t", "templateFile", true, "the path to the templateFile file");
		options.addOption("r", "repository", true, "the path to the repository");
		options.addOption("g", "group-id", true, "the group ID of the file");
		options.addOption("v", "view", false, "view only (does not create the file)");
		options.addOption(null, "source-file", true, "the path to the source file to evaluate");
		
		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			help(options, -1);
		}
		
		source = cmdLine.getOptionValue("source-file");
		templateFile = cmdLine.getOptionValue('t');
		groupId = cmdLine.getOptionValue('g');
		
		if (cmdLine.hasOption('v')) {
			writer = new StdoutWriter();
		}
		else {
			repository = cmdLine.getOptionValue('r');
			writer = new PackageFileWriter(repository);
		}
	}

	
	/**
	 * Runs the action
	 */
	public int run() {
		
		Map<String, String> binding = new HashMap<>();

		for(String key : System.getProperties().keys()) {
			String value = System.getProperty(key);
			binding.put(key, value);
		}

		
		File packageFile = new File(source); 
		String name = RegexUtils.getName(packageFile.getName());
		String version = RegexUtils.getVersion(packageFile.getName());

		binding.put("packageName", name);
		binding.put("packageVersion", version);
		binding.put("packageFile", packageFile.toURI().toString());
		binding.put("packageGroup", groupId);

		File file = new File(templateFile);
		GStringTemplateEngine engine = new GStringTemplateEngine();
		
		def template = engine.createTemplate(file).make(binding);
		
		
		
		writer.write(binding, template);
		
		return 0;
	}
	
}