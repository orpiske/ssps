/**
   Copyright 2012 Otavio Rodolfo Piske

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
package org.ssps.common.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class XmlWriterUtils {
	
	public static <T> void marshal(JAXBElement<T> element, T object, OutputStream stream) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(
				object.getClass().getPackage().getName());
		
		Marshaller m = context.createMarshaller();
		
		m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		m.marshal(element, stream );
	}
	
	
	public static <T> void marshal(JAXBElement<T> element, T object, File file) throws JAXBException, IOException {
		OutputStream stream = new FileOutputStream(file);
		
		try {
			marshal(element, object, stream);
			
			stream.flush();
			stream.close();
		}
		catch (JAXBException e) {
			IOUtils.closeQuietly(stream);
			
			throw e;
		} catch (IOException e) {
			IOUtils.closeQuietly(stream);
			
			throw e;
		}
	}

}
