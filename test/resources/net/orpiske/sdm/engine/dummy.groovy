
import net.orpiske.sdm.common.WorkdirUtils;
import net.orpiske.sdm.packages.BinaryPackage;

import static net.orpiske.sdm.lib.net.Downloader.*;
import static net.orpiske.sdm.lib.Unpack.*;
import static net.orpiske.sdm.lib.io.IOUtil.*;
import static net.orpiske.sdm.lib.Executable.*;

class Dummy extends BinaryPackage {
	def version = "3.0.5"
	def name = "dummy"
	
	def url = "http://some.com/url"

	void fetch(String url) {
		println "Fetch works"		
	}
	
	void extract(String artifactName) {
		println "Extracted successfully called"
	}
	
	
	
	void install(String artifactName) {
		println "Install successfully callsed"
	}	

	
	
}