package org.ssps.frontend.service.publish;

import java.net.URL;

import net.orpiske.ssps.common.Artifact;
import net.orpiske.ssps.publish.Publish;
import net.orpiske.ssps.publish.PublishRequest;
import net.orpiske.ssps.publish.PublishResponse;

import org.ssps.common.xml.exceptions.XmlDocumentException;
import org.ssps.frontend.archive.dbm.DbmDocument;
import org.ssps.frontend.service.AbstractService;

public class PublishService extends AbstractService<Publish, PublishResponse> {
    private DbmDocument dbmDocument;

    public PublishService(final String dbmFile) throws XmlDocumentException {
	super(Publish.class, "Publish");
	
	dbmDocument = new DbmDocument(dbmFile);
    }

    @Override
    protected URL getWSDL() {
	// return this.getClass().getResource("/wsdl/publish/publish.wsdl");
	return null;
    }

    @Override
    public PublishResponse executeService() {
	Publish service = createService();

	PublishRequest publishRequest = new PublishRequest();
	Artifact artifact = new Artifact();
	artifact.setId(dbmDocument.getDeliverableName());
	artifact.setName(dbmDocument.getDeliverableName());
	artifact.setVersion(dbmDocument.getProjectVersion());

	publishRequest.setArtifact(artifact);
	return service.publish(publishRequest);
    }

}
