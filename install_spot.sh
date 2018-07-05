#!/usr/bin/env bash

# mount external drive
sudo mkdir /extdata
sudo mount /dev/xvdf /extdata

# Add apt-repos and update
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv EA312927
echo "deb http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.2 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.2.list
sudo apt-get -y update

# Install Java
sudo apt-get -y install oracle-java8-installer

# Set JAVA_HOME
if [ "$JAVA_HOME" != '/usr/lib/jvm/java-8-oracle' ]
then
    sudo chmod 777 /etc/environment
    sudo echo "JAVA_HOME=/usr/lib/jvm/java-8-oracle" >> /etc/environment
    source /etc/environment
fi

# Install unzip
sudo apt-get -y install unzip

# Download and unpack source data
wget https://dl.dropboxusercontent.com/s/d2kpw8bcg2zbi6n/pubint_transfer.zip
unzip pubint_transfer.zip
sudo mkdir /var/otp
sudo cp -r pubint_transfer/otp/graphs /var/otp/graphs

# Install MongoDB
sudo apt-get install -y mongodb-org
sudo rm -f /etc/systemd/system/mongodb.service
sudo touch /etc/systemd/system/mongodb.service
sudo bash -c 'cat >/etc/systemd/system/mongodb.service' <<EOT
[Unit]
Description=High-performance, schema-free document-oriented database
After=network.target

[Service]
User=gijs
ExecStart=/usr/bin/mongod --quiet --dbpath /data/db

[Install]
WantedBy=multi-user.target
EOT
sudo mv /var/lib/mongodb /extdata/mongodb
sudo ln -s /extdata/db /var/lib/mongodb
sudo chown mongodb:mongodb /extdata/db

sudo systemctl enable mongodb
sudo systemctl start mongodb

# Restore Mongo Dumps
mongorestore -d pubint --archive=pubint_transfer/pubint_agent.zip
mongorestore -d pubint --archive=pubint_transfer/pubint_user.zip
mongorestore -d pubint --archive=pubint_transfer/pubint_anchor.zip

mongorestore -d pubint_v --archive=pubint_transfer/pubint_agent.zip
mongorestore -d pubint_v --archive=pubint_transfer/pubint_user.zip
mongorestore -d pubint_v --archive=pubint_transfer/pubint_anchor.zip

# Download JAR
wget https://dl.dropboxusercontent.com/s/c9tzekjca8bsbhu/PubInt-0.1-SNAPSHOT.jar

# Create service
sudo bash -c 'cat >/etc/systemd/system/pubint_validate.service' <<EOT
[Unit]
Description=PubInt Validator

[Service]
User=gijs
ExecStart=/usr/bin/java -Xmx12g -jar /home/gijs/PubInt_jar/PubInt-0.1-SNAPSHOT.jar runmodel -d -v -c -m 4 -l 5000
WorkingDirectory=/data

[Install]
WantedBy=multi-user.target

EOT

# Clean up
rm -r pubint_transfer
rm -r __MACOSX
rm pubint_transfer.zip#!/usr/bin/env bash