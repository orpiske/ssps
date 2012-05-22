package org.ssps.frontend.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import net.orpiske.ssps.common.Artifact;
import net.orpiske.ssps.publish.Publish;
import net.orpiske.ssps.publish.PublishRequest;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.PropertyConfigurator;

public class Main {
	public static void initLogger() throws FileNotFoundException {
		String configDir = System.getProperty("org.ssps.frontend.config.dir");
		
		if (configDir == null) {
			throw new FileNotFoundException("The configuration dir was not found");
		}
		
		PropertyConfigurator.configure(configDir + File.separator 
				+ "log.properties");
	}

	
	public static URL getWSDL() {
		return Main.class.getResource("/wsdl/publish/publish.wsdl");
	}
	
	
	public static void jaxWs() {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		
		factory.getInInterceptors().add(new LoggingInInterceptor());
		factory.getOutInterceptors().add(new LoggingOutInterceptor());
		factory.setServiceClass(Publish.class);
		factory.setAddress("http://localhost:8080/Publish");

		Publish publish = (Publish)factory.create();
		
		PublishRequest publishRequest = new PublishRequest();
		Artifact artifact = new Artifact();	
		artifact.setId("FromClient");
		
		publishRequest.setArtifact(artifact);
		
		publish.publish(publishRequest);
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Using alternate mode");
		
		try {
			initLogger();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
			System.exit(-1);
		}
		
		jaxWs();
		
		// other();
		
	
		
	}

}
