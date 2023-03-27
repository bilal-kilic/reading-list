@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  reading-list startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and READING_LIST_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\reading-list-0.0.1.jar;%APP_HOME%\lib\kompendium-core-1.7.0-SNAPSHOT.jar;%APP_HOME%\lib\swagger-ui-3.47.1.jar;%APP_HOME%\lib\logback-gelf-3.0.0.jar;%APP_HOME%\lib\logback-classic-1.2.3.jar;%APP_HOME%\lib\jquery-3.2.1.jar;%APP_HOME%\lib\ktor-server-webjars-jvm-2.0.2.jar;%APP_HOME%\lib\koin-ktor-3.1.5.jar;%APP_HOME%\lib\ktor-server-compression-jvm-2.0.3.jar;%APP_HOME%\lib\ktor-server-content-negotiation-jvm-2.0.3.jar;%APP_HOME%\lib\ktor-server-cors-jvm-2.0.3.jar;%APP_HOME%\lib\ktor-server-call-logging-jvm-2.0.0-beta-1.jar;%APP_HOME%\lib\ktor-server-data-conversion-jvm-2.0.0-beta-1.jar;%APP_HOME%\lib\ktor-server-cio-jvm-2.0.2.jar;%APP_HOME%\lib\ktor-server-host-common-jvm-2.0.2.jar;%APP_HOME%\lib\ktor-server-core-jvm-2.0.3.jar;%APP_HOME%\lib\ktor-client-okhttp-jvm-2.0.2.jar;%APP_HOME%\lib\ktor-client-logging-jvm-2.0.2.jar;%APP_HOME%\lib\ktor-client-serialization-jvm-2.0.2.jar;%APP_HOME%\lib\ktor-client-json-jvm-2.0.2.jar;%APP_HOME%\lib\ktor-client-core-jvm-2.0.2.jar;%APP_HOME%\lib\config4k-0.4.2.jar;%APP_HOME%\lib\kediatr-koin-starter-1.0.1.jar;%APP_HOME%\lib\kediatr-core-1.0.18.jar;%APP_HOME%\lib\kotlin-reflect-1.6.21.jar;%APP_HOME%\lib\ktor-serialization-kotlinx-json-jvm-2.0.3.jar;%APP_HOME%\lib\ktor-http-cio-jvm-2.0.2.jar;%APP_HOME%\lib\ktor-network-jvm-2.0.2.jar;%APP_HOME%\lib\ktor-websocket-serialization-jvm-2.0.2.jar;%APP_HOME%\lib\ktor-serialization-kotlinx-jvm-2.0.3.jar;%APP_HOME%\lib\ktor-serialization-jvm-2.0.3.jar;%APP_HOME%\lib\ktor-events-jvm-2.0.3.jar;%APP_HOME%\lib\ktor-websockets-jvm-2.0.3.jar;%APP_HOME%\lib\ktor-http-jvm-2.0.3.jar;%APP_HOME%\lib\ktor-utils-jvm-2.0.3.jar;%APP_HOME%\lib\ktor-io-jvm-2.0.3.jar;%APP_HOME%\lib\kotlinx-coroutines-jdk8-1.6.2.jar;%APP_HOME%\lib\kotlinx-coroutines-core-jvm-1.6.2.jar;%APP_HOME%\lib\kotlinx-coroutines-slf4j-1.6.2.jar;%APP_HOME%\lib\log4j-core-2.17.2.jar;%APP_HOME%\lib\jackson-annotations-2.13.3.jar;%APP_HOME%\lib\webjars-locator-core-0.48.jar;%APP_HOME%\lib\jackson-core-2.13.3.jar;%APP_HOME%\lib\jackson-databind-2.13.3.jar;%APP_HOME%\lib\couchbase-lite-java-3.0.0-beta02.jar;%APP_HOME%\lib\commons-codec-1.15.jar;%APP_HOME%\lib\rssreader-2.5.0.jar;%APP_HOME%\lib\ogmapper-1.0.0.jar;%APP_HOME%\lib\rome-1.18.0.jar;%APP_HOME%\lib\koin-test-junit5-3.1.2.jar;%APP_HOME%\lib\koin-test-jvm-3.1.2.jar;%APP_HOME%\lib\koin-core-jvm-3.1.5.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.6.21.jar;%APP_HOME%\lib\logback-core-1.2.3.jar;%APP_HOME%\lib\rome-utils-1.18.0.jar;%APP_HOME%\lib\slf4j-api-1.7.36.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.6.21.jar;%APP_HOME%\lib\config-1.4.1.jar;%APP_HOME%\lib\okhttp-4.9.3.jar;%APP_HOME%\lib\okio-jvm-2.8.0.jar;%APP_HOME%\lib\kotlinx-serialization-json-jvm-1.3.2.jar;%APP_HOME%\lib\kotlinx-serialization-core-jvm-1.3.2.jar;%APP_HOME%\lib\kotlin-test-junit-1.5.10.jar;%APP_HOME%\lib\kotlin-test-1.5.10.jar;%APP_HOME%\lib\kotlin-stdlib-1.6.21.jar;%APP_HOME%\lib\kotlin-test-common-1.5.10.jar;%APP_HOME%\lib\kotlin-test-annotations-common-1.5.10.jar;%APP_HOME%\lib\kotlin-stdlib-common-1.6.21.jar;%APP_HOME%\lib\jansi-2.4.0.jar;%APP_HOME%\lib\log4j-api-2.17.2.jar;%APP_HOME%\lib\jsoup-1.11.3.jar;%APP_HOME%\lib\jdom2-2.0.6.1.jar;%APP_HOME%\lib\classgraph-4.8.115.jar;%APP_HOME%\lib\reflections-0.9.11.jar;%APP_HOME%\lib\junit-platform-engine-1.6.2.jar;%APP_HOME%\lib\junit-jupiter-api-5.6.2.jar;%APP_HOME%\lib\junit-platform-commons-1.6.2.jar;%APP_HOME%\lib\junit-jupiter-engine-5.6.2.jar;%APP_HOME%\lib\annotations-13.0.jar;%APP_HOME%\lib\guava-20.0.jar;%APP_HOME%\lib\javassist-3.21.0-GA.jar;%APP_HOME%\lib\apiguardian-api-1.1.0.jar;%APP_HOME%\lib\junit-4.12.jar;%APP_HOME%\lib\opentest4j-1.2.0.jar;%APP_HOME%\lib\hamcrest-core-1.3.jar


@rem Execute reading-list
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %READING_LIST_OPTS%  -classpath "%CLASSPATH%" bilalkilic.com.ApplicationKt %*

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable READING_LIST_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%READING_LIST_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
