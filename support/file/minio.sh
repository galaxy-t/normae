#!/bin/bash

# 启动函数
# MINIO_ACCESS_KEY=minioadmin MINIO_SECRET_KEY=minioadmin MinIO服务连接所需要的用户名及密码
# --certs-dir /usr/local/minio/certs 证书存放的目录
# /usr/local/minio/data 上传的文件存放的路径
# /usr/local/minio/minio.log 日志文件路径
start() {
  MINIO_ACCESS_KEY=minioadmin MINIO_SECRET_KEY=minioadmin nohup ./minio --certs-dir /usr/local/minio/certs server /usr/local/minio/data > /usr/local/minio/minio.log 2>&1 &
}

# 停止函数
# 查询到 minio 服务的进程号 , 然后直接强制关机
stop() {
  pid=`ps -ef | grep minio | grep -v grep | awk '{print $2}'`
  kill -9 $pid
  echo "$pid 进程已结束"
}

# 查看运行状态
# 若能正常打印出进程号则代表运行中 , 否则为未运行状态
status() {
  ps -ef | grep minio | grep -v grep | grep -v bash
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
