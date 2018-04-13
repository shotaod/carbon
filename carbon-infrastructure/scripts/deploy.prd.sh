#!/bin/bash

cd `dirname $0`

ROOT_DIR='../..'

# build
echo '--------------------------------------------------'
echo 'Build'
echo '--------------------------------------------------'
mvn clean package -f ${ROOT_DIR}/carbon-reactor-v2/pom.xml

# copy uber jar
echo '--------------------------------------------------'
echo 'Copy uber-jar'
echo '--------------------------------------------------'
scp \
${ROOT_DIR}/carbon-sample-v2/target/carbon-app.jar \
cbn.ggl.ansible:/var/webapp

# run webapp as user[carbon]
echo '--------------------------------------------------'
echo 'Run webapp as user[carbon]'
echo '--------------------------------------------------'
ssh \
cbn.ggl.ansible \
"sudo runuser - carbon -s /bin/bash -c '/etc/init.d/webapp restart'"
