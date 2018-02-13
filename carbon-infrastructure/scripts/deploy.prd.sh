#!/bin/bash

cd `dirname $0`

PORT=44422
IDENTITY='~/.ssh/local/tmp'
ROOT_DIR='../..'

# build
echo '--------------------------------------------------'
echo 'Build'
echo '--------------------------------------------------'
mvn clean package -f $ROOT_DIR/carbon-reactor-v2/pom.xml

# copy uber jar
echo '--------------------------------------------------'
echo 'Copy uber-jar'
echo '--------------------------------------------------'
scp \
-P $PORT \
-i $IDENTITY \
$ROOT_DIR/carbon-sample-v2/target/carbon-app.jar \
ansible@localhost:/var/webapp

# run webapp as user[carbon]
CMD='java -jar -Dprofile=stg /var/webapp/carbon-app.jar'
echo '--------------------------------------------------'
echo 'Run webapp as user[carbon]'
echo $CMD
echo '--------------------------------------------------'
ssh \
-p $PORT \
-i $IDENTITY \
ansible@localhost "sudo runuser - carbon -s /bin/bash -c '$CMD'"
