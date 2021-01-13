#bin/bash

docker stop brewday
docker rm brewday
docker image rm gruppobirra2/brewday
docker run -p 8080:8080 -d --name brewday gruppobirra2/brewday:latest

docker stop maximizebrewtoday
docker rm maximizebrewtoday
docker image rm gruppobirra2/maximizebrewtoday
docker run -p 5000:5000 -d --name maximizebrewtoday gruppobirra2/maximizebrewtoday:latest

docker stop brewday-frontend
docker rm brewday-frontend
docker image rm gruppobirra2/brewday-frontend
docker run -p 8080:8080 -d --name brewday-frontend gruppobirra2/brewday-frontend:latest