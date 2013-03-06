#!/bin/bash

dir="/home/coder/workspace/mavenSpace/genius-web"

tomcatDir="/data/webserver/tomcat"

tomcatBin=${tomcatDir}"/bin"

tomcatWebApps=${tomcatDir}"/webapps"


#stop tomcat
echo "----stop the tomcat ----"

echo ${tomcatBin}
cd ${tomcatBin}

./shutdown.sh



