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
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException
import org.apache.commons.cli.PosixParser

import net.orpiske.net.orpiske.spm.writers.Writer;

import net.orpiske.spm.actions.AbstractAction;

class TemplateParser extends AbstractAction {
	private CommandLine cmdLine;
	private Options options;
	
	private String templateFile;
	private String propertiesFile;

	private Writer writer;
	
	public TemplateParser(String[] args) {		
		processCommand(args);
	}

	protected void processCommand(String[] args) {
		CommandLineParser parser = new PosixParser();

		options = new Options();

		options.addOption("h", "help", false, "prints the help");
		options.addOption("t", "templateFile", true, "the full path to the templateFile file");
		options.addOption("p", "properties", true, "the full path to the properties file");
		
		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			help(options, -1);
		}
		
		templateFile = cmdLine.getOptionValue('t');
		
		if (templateFile == null) {
			System.err.println "Template file is required"
			help(options, -1);
		}
		
		propertiesFile = cmdLine.getOptionValue('p');
		
	}


	private Properties getProperties() {
		Properties properties = new Properties();
		properties.load(new FileInputStream(propertiesFile));

		return properties;
	}


	public int run() {
		File file = new File(templateFile);
		def engine = new GStringTemplateEngine();
		Map<String, String> binding = new HashMap<>();
		
		if (propertiesFile != null) { 
			Properties properties = getProperties();

	
			for(String key : properties.stringPropertyNames()) {
				String value = properties.getProperty(key);
				binding.put(key, value);
			}
		}

		for(String key : System.getProperties().keys()) {
			String value = System.getProperty(key);
			binding.put(key, value);
		}

		def template = engine.createTemplate(file).make(binding);

		writer.write(binding, template);

		return 0;
	}
}
