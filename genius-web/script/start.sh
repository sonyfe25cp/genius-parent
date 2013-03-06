#!/bin/bash
M2_HOME="/usr/share/maven2"
dir="/home/coder/workspace/mavenSpace/genius-web"

tomcatDir="/data/webserver/tomcat"

tomcatBin=${tomcatDir}"/bin"

tomcatWebApps=${tomcatDir}"/webapps"
tomcatWorks=${tomcatDir}"/work"


#rebuild file
echo "----begin to rebuild the project"
cd $dir

mvn clean package

#delete old web files
echo "----delete the old web files ----"
cd $tomcatWebApps
rm -rf ROOT*

cd $tomcatWorks
rm -rf Catalina

#mv new web files
echo "----move the new file to tomcat ----"

mv ${dir}"/target/ROOT.war" ${tomcatWebApps}

#start tomcat
echo "----start the tomcat ----"

echo ${tomcatBin}
cd ${tomcatBin}

./startup.sh

echo "open the browser"
#chromium-browser http://127.0.0.1:8080/

