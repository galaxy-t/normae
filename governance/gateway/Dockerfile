# 指定基础镜像，必须为第一个命令
# 格式
#   FROM <image>
#   FROM <image>:<tag>
#   FROM <image>@<digest>
# tag或digest是可选的，如果不使用这两个值时，会使用latest版本的基础镜像
FROM java:8
# MAINTAINER: 维护者信息
MAINTAINER zhouqi
# 指定于外界交互的端口
EXPOSE 8080
# 用于指定持久化目录
# VOLUME /tmp
# 用于指定传递给构建运行时的变量，此处的 JAR_FILE 被配置在 pom 文件中
# ARG JAR_FILE
# 将本地文件添加到容器中，tar类型文件会自动解压(网络压缩资源不会被解压)，可以访问网络资源，类似wget
ADD /target/gateway-1.0-SNAPSHOT.jar app.jar
# 配置容器，使其可执行化
ENTRYPOINT ["java","-jar","/app.jar"]



# -d -v /root/app/gateway:/data -e TZ="Asia/Shanghai" --privileged=true --net=host