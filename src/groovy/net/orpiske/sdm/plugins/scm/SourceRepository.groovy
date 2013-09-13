/**
 Copyright 2013 Otavio Rodolfo Piske

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
package net.orpiske.sdm.plugins.scm

import net.orpiske.ssps.common.scm.Scm
import net.orpiske.ssps.common.scm.git.GitSCM


class SourceRepository {
    
 
    public void checkout(String url, String path) {
        File filePath = new File(path);
        
        checkout(url, filePath)
    }


    public void checkout(String url, File path) {
        Scm scm = new GitSCM();

        scm.checkout(url, path)
    }
}
