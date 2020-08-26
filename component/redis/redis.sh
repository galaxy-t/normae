#!/bin/bash



start() {
  /usr/local/redis/bin/redis-server /usr/local/redis/conf/redis.conf
}

stop() {
  /usr/local/redis/bin/redis-cli -a Rx@2020@ shutdown
}

status() {
  /usr/local/redis/bin/redis-cli -a Rx@2020@ info Server
}

if [ "$1" == "start" ]; then
  start
elif [ "$1" == "stop" ]; then
  stop
elif [ "$1" == "status" ]; then
  status
else
  echo "参数错误"
fi
