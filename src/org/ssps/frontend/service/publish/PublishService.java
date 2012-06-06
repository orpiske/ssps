package org.ssps.frontend.service.publish;

import java.net.URL;

import net.orpiske.ssps.common.Artifact;
import net.orpiske.ssps.publish.Publish;
import net.orpiske.ssps.publish.PublishRequest;
import net.orpiske.ssps.publish.PublishResponse;

import org.ssps.frontend.service.AbstractService;

public class PublishService extends AbstractService<Publish, PublishResponse> {

	public PublishService() {
		super(Publish.class, "Publish");
	}
	
	@Override
	protected URL getWSDL() {
		//return this.getClass().getResource("/wsdl/publish/publish.wsdl");
		return null;
	}

	@Override
	public PublishResponse executeService() {
		Publish service = createService();
		
		PublishRequest publishRequest = new PublishRequest();
		Artifact artifact = new Artifact();	
		artifact.setId("ssps-0.1.2");
		artifact.setName("ssps-0.1.2");
		artifact.setVersion("0.1.2");
		
		publishRequest.setArtifact(artifact);
		return service.publish(publishRequest);
	}



}
