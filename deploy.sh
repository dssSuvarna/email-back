#!/bin/sh
# Change to the directory where docker-compose.yml is copied
cd /docs/deploy
# Start the services defined in docker-compose.yml
./cleanup.sh
sudo docker-compose up -d