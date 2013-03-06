#!/bin/bash

dir="/home/coder/workspace/mavenSpace/genius-web/src/main/webapp"
tomcatDir="/data/webserver/tomcat"
tomcatWebApps=${tomcatDir}"/webapps/ROOT"


#mv new web files
echo "----move the new file to tomcat ----"

cp ${dir}/*.html ${tomcatWebApps}
cp ${dir}/*.jsp ${tomcatWebApps}
cp ${dir}/css/*.css ${tomcatWebApps}/css/
cp ${dir}/images/*.png ${tomcatWebApps}/images/
cp ${dir}/styles/*.css ${tomcatWebApps}/styles/
cp ${dir}/themes/blue/*.css ${tomcatWebApps}/themes/blue




