/bin/sh
rm -rf /tmp/local
mkdir /tmp/local
mongodump --db local --host 192.168.1.163 --out "/tmp/local"
today=$(date +"%Y-%m-%d")
zip -r /mnt/backup/mongodb/kube-mongo-$today /tmp/local/