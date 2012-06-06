package org.ssps.frontend.service;

import java.net.URL;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;
import org.ssps.common.configuration.ConfigurationWrapper;
import org.ssps.frontend.main.Main;

public abstract class AbstractService<T, Y> {
	private static final PropertiesConfiguration config = ConfigurationWrapper
			.getConfig();
	private static final Logger logger = Logger.getLogger(AbstractService.class);
	
	private JaxWsProxyFactoryBean factory;
	
	
	public AbstractService(final Class<?> serviceClass, final String context) {
		factory = new JaxWsProxyFactoryBean();
		
		factory.getInInterceptors().add(new LoggingInInterceptor());
		factory.getOutInterceptors().add(new LoggingOutInterceptor());
		factory.setServiceClass(serviceClass);
		
		String address = config.getString("deployment.server.address", 
				"localhost");
		String port = config.getString("deployment.services.port", "8080");
		
		factory.setAddress("http://" + address + ":" + port + "/" + context);
		
		URL url = getWSDL();
		
		if (url != null) { 
			factory.setWsdlURL(url.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	protected T createService() {
		return (T) factory.create();
	}
	
	
	protected abstract URL getWSDL();
	
	public abstract Y executeService();

	
}
