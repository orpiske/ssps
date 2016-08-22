#!/bin/bash
#		Copyright 2012 Otavio Rodolfo Piske Licensed under the Apache License, 
#		Version 2.0 (the "License"); you may not use this file except in compliance 
#		with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
#		Unless required by applicable law or agreed to in writing, software distributed 
#		under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES 
#		OR CONDITIONS OF ANY KIND, either express or implied. See the License for 
#		the specific language governing permissions and limitations under the License 
#

# To generate the documentation, download XS3P from http://xml.fiforms.org/xs3p/ or
# http://sourceforge.net/projects/xs3p/files/xs3p-1.1.5.zip/download and unpack it 
# into the xs3p directory

mkdir -p ../target/html/

for file in $(ls ../src/main/resources/schema/*.xsd) ; do 
	xsltproc xs3p/xs3p.xsl $file > ../target/html/`basename $file`.html 	
done
 