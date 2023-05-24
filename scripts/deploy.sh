#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/app.pem \
  target/OnlineShop-1.0-SNAPSHOT.jar \
  ec2-user@ec2-14-152-171-126.eu-central-1.compute.amazonaws.com:/home/ec2-user/

echo 'Restart server...'

ssh -i ~/.ssh/app.pem ec2-user@ec2-14-152-171-126.eu-central-1.compute.amazonaws.com << EOF
pgrep java | xargs kill -9
nohup java -jar OnlineShop-1.0-SNAPSHOT.jar > log.txt &
EOF

echo 'Bye'
