#!/usr/bin/env bash

# mount external drive
sudo mkdir /extdata
sudo mount /dev/xvdf /extdata

# Add apt-repos and update
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv EA312927
echo "deb http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.2 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.2.list
sudo add-apt-repository ppa:webupd8team/java
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
wget https://dl.dropboxusercontent.com/s/x8b7pvk90vjr9hv/pubint_transfer.zip
unzip pubint_transfer.zip
sudo mkdir /var/otp
sudo chmod 777 /var/otp
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
User=mongodb
ExecStart=/usr/bin/mongod --quiet --config /etc/mongod.conf --dbpath /extdata/mongodb --wiredTigerEngineConfigString="cache_size=24G"
Restart=always

[Install]
WantedBy=multi-user.target
EOT

sudo chown mongodb:mongodb /extdata/db

sudo systemctl enable mongodb
sudo systemctl start mongodb

# Download JAR
wget https://dl.dropboxusercontent.com/s/c9tzekjca8bsbhu/PubInt-0.1-SNAPSHOT.jar

# Create service
sudo bash -c 'cat >/etc/systemd/system/pubint_create.service' <<EOT
[Unit]
Description=PubInt Prism Creator

[Service]
User=ubuntu
ExecStart=/usr/bin/java -Xmx32g -jar /home/ubuntu/PubInt-0.1-SNAPSHOT.jar createprisms -m 16 -o 4 -v
WorkingDirectory=/home/ubuntu

[Install]
WantedBy=multi-user.target

EOT

# Clean up
rm -r pubint_transfer
rm -r __MACOSX
rm pubint_transfer.zip