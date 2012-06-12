package org.ssps.common.xml;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlParserUtils {
	
	public static <T> void marshal(Class<T> clazz) {
	        
			/**
			try {
	            // JAXBElement<T> gl = of.createGreetings( grList );
	            //JAXBContext jc = JAXBContext.newInstance( "hello" );
	            Marshaller m = jc.createMarshaller();
	            m.marshal( gl, System.out );
	        } catch( JAXBException jbe ){
	            // ...
	        }
	        */
	}

	public static <T> T unmarshal(Class<T> docClass, InputStream inputStream)
			throws JAXBException 
	{
		String packageName = docClass.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance(packageName);
		Unmarshaller u = jc.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<T> doc = (JAXBElement<T>) u.unmarshal(inputStream);

		return doc.getValue();
	}
}
