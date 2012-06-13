package org.ssps.sdm.adm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import net.orpiske.ssps.adm.Adm;

import org.ssps.common.xml.XmlParserUtils;
import org.ssps.common.xml.exceptions.XmlDocumentException;

public class AdmDocument {
	private String path;
	
	private Adm admDocument;
	
	public AdmDocument(final String path) throws XmlDocumentException {
		this.setPath(path);

		try {
			InputStream stream = new FileInputStream(path);

			setDocument(XmlParserUtils.unmarshal(Adm.class, stream));
		} catch (FileNotFoundException e) {
			throw new XmlDocumentException(e.getMessage(), e);
		} catch (JAXBException e) {
			throw new XmlDocumentException("Unable to umarhsall document", e);
		}
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Adm getDocument() {
		return admDocument;
	}

	public void setDocument(Adm admDocument) {
		this.admDocument = admDocument;
	}

	
	
}
