#!/bin/bash

set -e

cd /Users/alexisperreault/Documents/music-central
mvn versions:set -DnewVersion=$1 && mvn versions:commit
git add .
git commit -m "release $1"
mvn clean install
cd /Users/alexisperreault/Documents/music-central/mc-server
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=mc/mc-server:$1
docker tag docker.io/mc/mc-server:$1  192.168.1.163:32037/docker.io/mc/mc-server:$1
docker push 192.168.1.163:32037/docker.io/mc/mc-server:$1

sed 's/version/'$1'/g' /Users/alexisperreault/Documents/music-central/script/release/kube.tpl > /Users/alexisperreault/Documents/music-central/script/release/kube.yaml

kubectl apply -f /Users/alexisperreault/Documents/music-central/script/release/kube.yaml
kubectl get pod


