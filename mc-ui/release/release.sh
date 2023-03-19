#!/bin/bash

set -e
cd /Users/alexisperreault/Documents/music-central/mc-ui

docker build . -t mc/mc-ui:$1
docker tag docker.io/mc/mc-ui:$1  192.168.1.163:32037/docker.io/mc/mc-ui:$1
docker push 192.168.1.163:32037/docker.io/mc/mc-ui:$1

sed 's/version/'$1'/g' /Users/alexisperreault/Documents/music-central/mc-ui/release/kube.tpl > /Users/alexisperreault/Documents/music-central/mc-ui/release/kube.yaml

kubectl apply -f /Users/alexisperreault/Documents/music-central/mc-ui/release/kube.yaml
kubectl get pod


