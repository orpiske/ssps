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
package net.orpiske.sdm.packages;

/**
 * Base class for source packages
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class SourcePackage implements Package {

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#fetch(java.lang.String)
	 */
	@Override
	public void fetch(String url) {
		
	}

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#unpack(java.lang.String)
	 */
	@Override
	public void extract(String artifactName) {
		
	}

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#prepare()
	 */
	@Override
	public void prepare() {
		
	}

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#build()
	 */
	@Override
	public void build() {

	}

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#verify()
	 */
	@Override
	public void verify() {

	}

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#install()
	 */
	@Override
	public void install() {

	}

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#uninstall()
	 */
	@Override
	public void uninstall() {
		
	}
}
