#!/usr/bin/env bash

# Add apt-repos and update
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv EA312927
echo "deb http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.2 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.2.list
sudo apt-get -y update

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
User=mongodb
ExecStart=/usr/bin/mongod --quiet --config /etc/mongod.conf

[Install]
WantedBy=multi-user.target
EOT
sudo mkdir /data
sudo mkdir /data/db
sudo chmod 777 /data/db
sudo mv /data/db /extdata/db
sudo ln -s /extdata/db /data/db
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

# Install supervisor and create service
sudo bash -c 'cat >/etc/systemd/system/pubint_create.service' <<EOT
[Unit]
Description=PubInt Prism Creator

[Service]
User=ubuntu
ExecStart=/usr/bin/java -Xmx32g -jar /home/ubuntu/PubInt-0.1-SNAPSHOT.jar createprisms -m 16 -o 4
WorkingDirectory=/home/ubuntu

[Install]
WantedBy=multi-user.target

EOT

# Clean up
rm -r pubint_transfer
rm -r __MACOSX
rm pubint_transfer.zip#!/usr/bin/env bash