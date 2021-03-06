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

import net.orpiske.ssps.common.scm.DefaultCredentials
import net.orpiske.ssps.common.scm.Scm
import net.orpiske.ssps.common.scm.ScmCredentials
import net.orpiske.ssps.common.scm.git.GitSCM
import net.orpiske.ssps.common.scm.svn.SvnSCM


class SourceRepository {
    private String username = null;
    private String password = null;
    
    public SourceRepository(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Creates a new repository provider based on the repository information
     * @param repositoryInfo repository information
     * @return A repository provider object
     */
    public Scm newScm(final String url) {
        if (url.endsWith(".git") || url.startsWith("git://")) {
            return new GitSCM();
        }

        if (url.startsWith("svn://")) {
            return new SvnSCM();
        }

        /* Defaults to SvnProvider because, well, most git repositories end with ".git" */
        return new SvnSCM();

    }
    
    
    private void setCredentials(final Scm scm) {
        ScmCredentials credentials = new DefaultCredentials(username, password);
        
        scm.setCredentials(credentials)
    }
    
 
    public void checkout(String url, String path) {
        File filePath = new File(path);

        checkout(url, filePath)
    }


    public void checkout(String url, File path) {
        Scm scm = newScm(url);

        if (username != null) {
            setCredentials(scm)
        }


        scm.checkout(url, path)
    }
}
