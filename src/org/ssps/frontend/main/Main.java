package org.ssps.frontend.main;

import java.io.FileNotFoundException;
import java.net.URL;

import net.orpiske.ssps.common.Artifact;
import net.orpiske.ssps.publish.Publish;
import net.orpiske.ssps.publish.PublishRequest;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.ssps.common.configuration.ConfigurationWrapper;
import org.ssps.common.logger.LoggerUtils;
import org.ssps.frontend.service.publish.PublishService;
import org.ssps.frontend.utils.Constants;


public class Main {
	public static void initLogger() throws FileNotFoundException {
		LoggerUtils.initLogger(Constants.FRONTEND_CONFIG_DIR);
	}

	/**
	 * Initializes the configuration object
	 */
	private static void initConfig() {
		try {
			ConfigurationWrapper.initConfiguration(
					Constants.FRONTEND_CONFIG_DIR, 
					Constants.CONFIG_FILE_NAME);
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(-3);
		} catch (ConfigurationException e) {
			System.err.println(e.getMessage());
			System.exit(-3);
		} 
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Using alternate mode");
		
		try {
			initLogger();
			initConfig();
			
			PublishService publishService = new PublishService();
			
			publishService.executeService();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
			System.exit(-1);
		}
		catch (Exception e) {
			System.err.println("Unable to execute operation: " 
					+ e.getMessage());
		}
		
		
	}

}
