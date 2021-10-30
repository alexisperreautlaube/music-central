cd /Users/alexisperreault/Documents/music-central
kubectl apply -f kube.yaml

cd /Users/alexisperreault/Documents/music-central/mc-messaging/mc-msg-consumer
docker build -t mc/mc-msg-consumer .
docker tag mc/mc-msg-consumer:1.0.2  192.168.1.82:32037/mc/mc-msg-consumer:1.0.2
docker push 192.168.1.82:32037/mc/mc-msg-consumer:1.0.2