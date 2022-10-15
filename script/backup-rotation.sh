#!/bin/bash
for filename in /mnt/backup/mongodb/*; do
    string1=$(date +%e -r "$filename")
    if [[ $(find "$filename" -mtime +31 -print) ]]; then
      if [ "$string1" != " 1" ]; then
        echo "Delete $filename"
        $(rm "$filename")
      fi
    elif [[ $(find "$filename" -mtime +7 -print) ]]; then
      string1=$(date +%e -r "$filename")
      #echo "$string1"
      if [ "$string1" != " 1" ] && [ "$string1" != " 5" ] && [ "$string1" != "10" ] && [ "$string1" != "15" ]&& [ "$string1" != "20" ]&& [ "$string1" != "25" ]&& [ "$string1" != "30" ]; then
        echo "delete $filename";
        $(rm "$filename")
      fi
    fi
done