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
package org.ssps.frontend.service.release;

import java.net.URL;

import net.orpiske.ssps.release.ReleaseRequest;
import net.orpiske.ssps.release.ReleaseResponse;

import org.ssps.frontend.service.AbstractService;

/**
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class ReleaseService extends AbstractService<ReleaseRequest, ReleaseResponse> {

    /**
     * @param serviceClass
     * @param context
     */
    public ReleaseService(final String dbmFile) {
	super(ReleaseRequest.class, "Release");
    }

    /* (non-Javadoc)
     * @see org.ssps.frontend.service.AbstractService#getWSDL()
     */
    @Override
    protected URL getWSDL() {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see org.ssps.frontend.service.AbstractService#executeService()
     */
    @Override
    public ReleaseResponse executeService() {
	// TODO Auto-generated method stub
	return null;
    }

}
