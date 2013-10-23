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
 * Date: 10/23/13
 * Time: 8:18 AM
 * To change this templateFile use File | Settings | File Templates.
 */
class Eval extends AbstractAction {
	private CommandLine cmdLine;
	private Options options;
	
	private String templateFile;
	private String source;
		
	
	public Eval(String[] args) {
		processCommand(args);
	}

	protected void processCommand(String[] args) {
		CommandLineParser parser = new PosixParser();

		options = new Options();

		options.addOption("h", "help", false, "prints the help");
		options.addOption("t", "templateFile", true, "the path to the templateFile file");
		options.addOption(null, "source-file", true, "the path to the source file to evaluate");
		
		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			help(options, -1);
		}
		
		/*
		if (cmdLine.hasOption("name-regex")) {
			nameRegex = cmdLine.getOptionValue("name-regex");
		}

		if (cmdLine.hasOption("version-regex")) {
			nameRegex = cmdLine.getOptionValue("version-regex");
		}
		*/
		
		source = cmdLine.getOptionValue("source-file");
		templateFile = cmdLine.getOptionValue('t');
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
		binding.put("packageFile", packageFile.getCanonicalPath());

		File file = new File(templateFile);
		def engine = new GStringTemplateEngine();
		def template = engine.createTemplate(file).make(binding);
		template.println template.toString();
		
		return 0;
	}
	
}