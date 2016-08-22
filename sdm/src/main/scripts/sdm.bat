@REM   Copyright 2012 Otavio Rodolfo Piske
@REM
@REM   Licensed under the Apache License, Version 2.0 (the "License");
@REM   you may not use this file except in compliance with the License.
@REM   You may obtain a copy of the License at
@REM
@REM       http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM   Unless required by applicable law or agreed to in writing, software
@REM   distributed under the License is distributed on an "AS IS" BASIS,
@REM   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM   See the License for the specific language governing permissions and
@REM   limitations under the License.

@echo off

set SDM_HOME=%~dp0\..

@REM Set 
if %OS%=="Windows_NT" @setlocal
if %OS%=="WINNT" @setlocal

for %%i in ("%SDM_HOME%"\bin\sdm-*) do set SDM_JAR="%%i"

@java -classpath %SDM_JAR% -Dorg.ssps.sdm.home=%SDM_HOME% net.orpiske.ssps.sdm.main.Main %*