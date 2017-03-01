#!/usr/bin/env bash

# Add apt-repos and update
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv EA312927
echo "deb http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.2 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.2.list
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update

# Install Java
sudo apt-get install oracle-java8-installer

# Set JAVA_HOME
if [ "$JAVA_HOME" != '/usr/lib/jvm/java-8-oracle' ]
then
    sudo chmod 777 /etc/environment
    sudo echo "JAVA_HOME=/usr/lib/jvm/java-8-oracle" >> /etc/environment
    source /etc/environment
fi

# Install MongoDB
sudo apt-get install -y mongodb-org
sudo rm -f /etc/systemd/system/mongodb.service
sudo cat > /etc/systemd/system/mongodb.service <<EOT
[Unit]
Description=High-performance, schema-free document-oriented database
After=network.target

[Service]
User=mongodb
ExecStart=/usr/bin/mongod --quiet --config /etc/mongod.conf

[Install]
WantedBy=multi-user.target
EOT
sudo systemctl enable mongodb
sudo systemctl start mongodb

# Install Maven
sudo apt-get install maven

# Clone Git and build
mkdir git
cd git
git clone http://github.com/GBPeters/PubInt
cd PubInt
mvn clean
mvn install -DskipTests


cd ..
cd ..
