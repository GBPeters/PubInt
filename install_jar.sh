#!/usr/bin/env bash

# Add apt-repos and update
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv EA312927
echo "deb http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.2 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.2.list
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get -y update

# Install unzip
sudo apt-get -y install unzip

# Install Java
sudo apt-get -y install oracle-java8-installer

# Set JAVA_HOME
if [ "$JAVA_HOME" != '/usr/lib/jvm/java-8-oracle' ]
then
    sudo chmod 777 /etc/environment
    sudo echo "JAVA_HOME=/usr/lib/jvm/java-8-oracle" >> /etc/environment
    source /etc/environment
fi

# Download and unpack source data
wget https://dl.dropboxusercontent.com/s/d2kpw8bcg2zbi6n/pubint_transfer.zip
unzip pubint_transfer.zip
sudo mkdir /var/otp
sudo cp -r pubint_transfer/otp/graphs /var/otp/graphs
cp pubint_transfer/PubInt-0.1-SNAPSHOT.jar PubInt-0.1-SNAPSHOT.jar

# Install MongoDB
sudo apt-get install -y mongodb-org
sudo rm -f /etc/systemd/system/mongodb.service
sudo touch /etc/systemd/system/mongodb.service
sudo bash -c 'cat >/etc/systemd/system/mongodb.service' <<EOT
[Unit]
Description=High-performance, schema-free document-oriented database
After=network.target

[Service]
User=mongodb
ExecStart=/usr/bin/mongod --quiet --config /etc/mongod.conf

[Install]
WantedBy=multi-user.target
EOT
sudo mkdir /data
sudo mkdir /data/db
sudo chmod 777 /data/db
sudo systemctl enable mongodb
sudo systemctl start mongodb

# Restore Mongo Dumps
mongorestore -d pubint --archive=pubint_transfer/pubint_agent.zip
mongorestore -d pubint --archive=pubint_transfer/pubint_user.zip
mongorestore -d pubint --archive=pubint_transfer/pubint_anchor.zip

mongorestore -d pubint_v --archive=pubint_transfer/pubint_agent.zip
mongorestore -d pubint_v --archive=pubint_transfer/pubint_user.zip
mongorestore -d pubint_v --archive=pubint_transfer/pubint_anchor.zip