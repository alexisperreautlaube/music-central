

cd /Users/alexisperreault/Documents/music-central
mvn versions:set -DnewVersion=1.4.3 && mvn versions:commit
mvn clean install
cd /Users/alexisperreault/Documents/music-central/mc-messaging/mc-msg-consumer
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=mc/mc-msg-consumer:1.4.3
docker tag docker.io/mc/mc-msg-consumer:1.4.3  192.168.1.82:32037/docker.io/mc/mc-msg-consumer:1.4.3
docker push 192.168.1.82:32037/docker.io/mc/mc-msg-consumer:1.4.3

cd /Users/alexisperreault/Documents/music-central
kubectl apply -f kube.yaml
kubectl get pod