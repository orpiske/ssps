package org.ssps.frontend.main;

import java.io.FileNotFoundException;
import java.net.URL;

import net.orpiske.ssps.common.Artifact;
import net.orpiske.ssps.publish.Publish;
import net.orpiske.ssps.publish.PublishRequest;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.ssps.common.configuration.ConfigurationWrapper;
import org.ssps.common.logger.LoggerUtils;
import org.ssps.frontend.archive.Archiver;
import org.ssps.frontend.service.publish.PublishService;
import org.ssps.frontend.utils.Constants;

public class Main {
    private static Options options;

    public static void initLogger() throws FileNotFoundException {
	LoggerUtils.initLogger(Constants.FRONTEND_CONFIG_DIR);
    }

    /**
     * Initializes the configuration object
     */
    private static void initConfig() {
	try {
	    ConfigurationWrapper.initConfiguration(
		    Constants.FRONTEND_CONFIG_DIR, Constants.CONFIG_FILE_NAME);
	} catch (FileNotFoundException e) {
	    System.err.println(e.getMessage());
	    System.exit(-3);
	} catch (ConfigurationException e) {
	    System.err.println(e.getMessage());
	    System.exit(-3);
	}
    }
    
    public static void help(int code) {
	HelpFormatter formatter = new HelpFormatter();

	formatter.printHelp("ssps-frontend", options);
	System.exit(code);
    }

    public static CommandLine processCommand(String[] args)
	    throws ParseException {
	// create the command line parser
	CommandLineParser parser = new PosixParser();

	// create the Options
	options = new Options();

	options.addOption("h", "help", false, "prints the help");
	options.addOption("f", "file", true, "path to the DBM file");

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
 	    
	    
	    if (!cmdLine.hasOption('f')) {
		help(-1);
	    }
	    
	    
	    
	    String dbmFile = cmdLine.getOptionValue('f');
	    
	    Archiver archiver = new Archiver(dbmFile);
	    
	    archiver.createArchive();

	    // PublishService publishService = new PublishService();

	    // publishService.executeService();

	} catch (FileNotFoundException e) {
	    e.printStackTrace();

	    System.exit(-1);
	} catch (Exception e) {
	    System.err
		    .println("Unable to execute operation: " + e.getMessage());
	}

    }

}
