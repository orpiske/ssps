package net.orpiske.spm.actions

import groovy.text.GStringTemplateEngine
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException
import org.apache.commons.cli.PosixParser

/**
 * Created with IntelliJ IDEA.
 * User: orpiske
 * Date: 10/22/13
 * Time: 3:03 PM
 * To change this templateFile use File | Settings | File Templates.
 */
class TemplateParser extends AbstractAction{
	private CommandLine cmdLine;
	private Options options;
	
	private String templateFile;
	private String propertiesFile;
	
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

		Properties properties = getProperties();
		Map<String, String> binding = new HashMap<>();


		for(String key : properties.stringPropertyNames()) {
			String value = properties.getProperty(key);
			binding.put(key, value);
		}


		for(String key : System.getProperties().keys()) {
			String value = System.getProperty(key);
			binding.put(key, value);
		}


		def template = engine.createTemplate(file).make(binding);
		template.
				println template.toString();

		return 0;
	}
}
