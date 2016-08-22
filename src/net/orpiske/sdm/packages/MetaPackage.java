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
 * Base class for meta-packages. Meta packages are packages that do no nothing, but they 
 * contain several dependencies and are used to simplify installation of multiple packages
 * @author Otavio R. Piske <angusyoung@gmail.com>
 *
 */
public class MetaPackage implements Package {

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#fetch(java.lang.String)
	 */
	@Override
	public void fetch(String url) {

	}

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#extract(java.lang.String)
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
	 * @see net.orpiske.sdm.packages.Package#uninstall(java.lang.String)
	 */
	@Override
	public void uninstall(String path) {
		
	}

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see net.orpiske.sdm.packages.Package#cleanup()
	 */
	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

}
